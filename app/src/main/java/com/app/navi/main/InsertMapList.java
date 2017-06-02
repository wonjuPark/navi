package com.app.navi.main;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.app.navi.R;
import utility.CalDistance;
import utility.DbManager;
import utility.GPSManager;
import utility.OpenAPITask;
import utility.ViewDetailAdapter;
import vo.AddressVO;
import vo.ListVO;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by 95016056 on 2017-05-29.
 */

public class InsertMapList extends AppCompatActivity {
    static final int REQ_ADD_CONTACT = 1 ;
    ListView listview = null;
    ViewDetailAdapter vdAdapter = null;
    ArrayList<AddressVO> aList = new ArrayList<AddressVO>();
    AddressVO addressVO;
    CalDistance calDistance;
    OpenAPITask get;
    DbManager manager;
    ArrayList<AddressVO> sortList  = new ArrayList<AddressVO>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.insert_main);
        manager = new DbManager(this, "NAVIDB1.db", null, 1);

        findViewById(R.id.insertAddress).setOnClickListener(mClickListener);
        findViewById(R.id.makePriority).setOnClickListener(mClickListener_priority);
        findViewById(R.id.save).setOnClickListener(mClickListener_save);



        vdAdapter = new ViewDetailAdapter();
        listview = (ListView)findViewById(R.id.map_id);
        listview.setAdapter(vdAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                // get item
                ListVO item = (ListVO) parent.getItemAtPosition(position);
                Toast.makeText(getApplicationContext(), item.getViewtext(), Toast.LENGTH_LONG).show();
            }
        });
    }

    Button.OnClickListener mClickListener = new View.OnClickListener(){
        public void  onClick(View v){
            Intent intent=new Intent(InsertMapList.this,SearchAddress.class);
            startActivityForResult(intent, REQ_ADD_CONTACT);
        }
    };

    Button.OnClickListener mClickListener_priority = new View.OnClickListener(){
        public void  onClick(View v){
            GPSManager gps = new GPSManager(InsertMapList.this);
            calDistance = new CalDistance();
            double la = 0.0;
            double lo = 0.0;
            get = new OpenAPITask();
            StringBuffer sJson  = new StringBuffer();
            String apikey  = ((MyApplication)getApplication()).getNaviAPIKey();
            try {
                if (gps.isGetLocation()) {
                   // String temp = String.valueOf(gps.getLatitude()) + "," + String.valueOf(gps.getLongitude());
                   // sJson = get.execute(temp, apikey, "3", ((MyApplication) getApplication()).getNaviDiv()).get();
                    AddressVO aVO = new AddressVO();//parseJSON(sJson);
                    aVO.setLat(gps.getLatitude());
                    aVO.setLon(gps.getLongitude());

                    sortList = calDistance.getDistancePriority(aList, aVO);

                    vdAdapter.removeAll();
                    listview.clearChoices();
                    for(AddressVO vo : sortList) {
                        vdAdapter.addItem(vo.getIndex(), vo.getMapName(), vo.getRoadAddrPart1() + "\\n" + vo.getDesc());
                    }
                    vdAdapter.notifyDataSetChanged();
                } else {
                    gps.showSettingsAlert();
                }


            }catch(Exception e){
                e.printStackTrace();
            }
        }
    };

    Button.OnClickListener mClickListener_save = new View.OnClickListener(){
        public void  onClick(View v){
            SQLiteDatabase db = manager.getWritableDatabase();

            MyApplication ma = (MyApplication)getApplication();
            ContentValues row;
            try {
                db.beginTransaction();

                row = new ContentValues();
                row.put("NAME", ((EditText)findViewById(R.id.mapListName)).getText().toString());
                db.insert("MapList", null, row);

                Cursor cursor = db.rawQuery("SELECT _id FROM MapList WHERE NAME = '" + row.get("NAME") + "';", null);

                int keyId = 0;
                if(cursor.moveToFirst())
                    keyId = cursor.getInt(0);

                for(AddressVO vo : sortList) {
                    row = new ContentValues();
                    row.put("REL_ID", keyId);
                    row.put("NAME", vo.getRoadAddrPart1());
                    row.put("X", vo.getLon());
                    row.put("Y", vo.getLat());
                    row.put("SEQ", vo.getIndex());
                    db.insert("OptMap", null, row);
                }

                db.setTransactionSuccessful();
                Toast.makeText(getApplicationContext(), "API 키 값이 저장되었습니다.", Toast.LENGTH_LONG).show();

            }catch(SQLiteException ex){
                ex.printStackTrace();
            }finally {
                db.endTransaction();
                db.close();
                Toast.makeText(getApplicationContext(), "API 키 값이 저장되지 않았습니다.",Toast.LENGTH_LONG).show();
            }

        }
    };


    private AddressVO parseJSON(StringBuffer sb) throws JSONException {
        JSONObject jOb = new JSONObject(sb.toString());
        JSONObject jRe = jOb.getJSONObject("coordinate");
        AddressVO addressVO = new AddressVO();
        addressVO.setLat(Double.valueOf(jRe.getString("lat")));
        addressVO.setLon(Double.valueOf(jRe.getString("lon")));


        return addressVO;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == REQ_ADD_CONTACT) {
            if (resultCode == RESULT_OK) {
                addressVO = (AddressVO)intent.getSerializableExtra("data");

                vdAdapter.addItem(0, addressVO.getMapName() , addressVO.getRoadAddrPart1() + "\\n" + addressVO.getDesc());

                aList.add(addressVO);

                vdAdapter.notifyDataSetChanged();
            }
        }
    }

}
