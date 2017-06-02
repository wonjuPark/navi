package com.app.navi.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.app.navi.R;
import utility.DbManager;
import utility.OpenAPITask;
import utility.ViewAdapter;
import vo.AddressVO;
import vo.ListVO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by 95016056 on 2017-05-30.
 */

public class SearchAddress extends AppCompatActivity {
    ListView listview = null;
    ViewAdapter vAdapter = null;
    DbManager manager;
    Intent intent;
    OpenAPITask get;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_main);

        /* 데이터베이스 연결 */
        manager = new DbManager(this, "NAVIDB1.db", null, 1);


        findViewById(R.id.search_button).setOnClickListener(mClickListener);


        vAdapter = new ViewAdapter();

        listview = (ListView)findViewById(R.id.address_list);
        listview.setAdapter(vAdapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
             if(((EditText)findViewById(R.id.mapName)).getText().length() > 0){
                 Toast.makeText(getApplicationContext(), "명칭을 입력해주세요.", Toast.LENGTH_LONG).show();
                 return;
             }
                // get item
            intent = new Intent();
            AddressVO addressVO = new AddressVO();
            get = new OpenAPITask();
            String apikey = ((MyApplication)getApplication()).getNaviAPIKey();
            ListVO item = (ListVO) parent.getItemAtPosition(position);
            StringBuffer sJson  = new StringBuffer();
            try {
                sJson = get.execute(item.getViewtext().toString(), apikey, "1", ((MyApplication)getApplication()).getNaviDiv()).get();
                addressVO  = parseRadJSON(sJson);
            }catch(Exception e)
            {
                e.printStackTrace();
            }

            addressVO.setRoadAddrPart1(item.getViewtext());
            addressVO.setZipNo(item.getNumber());
            addressVO.setMapName(((EditText)findViewById(R.id.mapName)).getText().toString());
            addressVO.setDesc(((EditText)findViewById(R.id.memo)).getText().toString());

            intent.putExtra("data",  addressVO);
            setResult(RESULT_OK, intent);
            finish();

            }
        });
    }

    Button.OnClickListener mClickListener = new View.OnClickListener(){
        public void  onClick(View v){
            EditText text = (EditText)findViewById(R.id.address_name);
            text.setText("봉천로4길");
            if(text.getText().toString() == "" || text.getText().length() <= 2){
                Toast.makeText(getApplicationContext(), "주소명을 입력 부탁드립니다.(최소 2자리 이상)", Toast.LENGTH_LONG).show();
                return ;
            }
           // vAdapter = new ViewAdapter();

            get = new OpenAPITask();
            StringBuffer sb = new StringBuffer();
            String apikey = ((MyApplication)getApplication()).getJusoAPIKey();
            try {
                sb = get.execute(text.getText().toString(), apikey, "2", "").get();
                ArrayList<AddressVO> list = parseJSON(sb);
                if(list.size() > 0) {
                    for (AddressVO vo : list) {
                        vAdapter.addItem(vo.getRoadAddrPart1(), vo.getJibunAddr(), vo.getZipNo());
                    }
                }else {
                    Toast.makeText(getApplicationContext(), "검색된 주소가 없습니다.", Toast.LENGTH_LONG).show();
                }
                vAdapter.notifyDataSetChanged();
            }catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    };

    private ArrayList<AddressVO> parseJSON(StringBuffer sb) throws JSONException {
        JSONObject jOb = new JSONObject(sb.toString());
        JSONObject jRe = jOb.getJSONObject("results");
        JSONArray jArr = jRe.getJSONArray("juso");
        ArrayList<AddressVO> list = new ArrayList<AddressVO>();

        AddressVO addressVO = new AddressVO();

        for (int i = 0; i < jArr.length(); i++) {
            addressVO = new AddressVO();
            addressVO.setZipNo(jArr.getJSONObject(i).get("zipNo").toString());
            addressVO.setRoadAddrPart1(jArr.getJSONObject(i).get("roadAddrPart1").toString());
            addressVO.setJibunAddr(jArr.getJSONObject(i).get("jibunAddr").toString());

            list.add(addressVO);
        }
        return list;
    }

    private AddressVO parseRadJSON(StringBuffer sb) throws JSONException {
        JSONObject jOb = new JSONObject(sb.toString());
        JSONObject jRe = jOb.getJSONObject("coordinateInfo");
        JSONArray jArr = jRe.getJSONArray("coordinate");
        ArrayList<AddressVO> list = new ArrayList<AddressVO>();

        AddressVO addressVO = new AddressVO();

        addressVO.setLat(Double.valueOf(jArr.getJSONObject(0).get("newLat").toString()));
        addressVO.setLon(Double.valueOf(jArr.getJSONObject(0).get("newLon").toString()));

        return addressVO;
    }


}
