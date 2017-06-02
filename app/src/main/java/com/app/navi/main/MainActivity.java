package com.app.navi.main;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.app.navi.R;

import java.util.ArrayList;

import utility.CalDistance;
import utility.DbManager;
import utility.GPSManager;
import utility.ViewAdapter;
import vo.AddressVO;

public class MainActivity extends AppCompatActivity {
    LocationManager locationManager;
    TextView te;
    DbManager manager;
    ListView listview = null;
    ViewAdapter vAdapter = null;
    public static Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        manager = new DbManager(this, "NAVIDB1.db", null, 1);

        init();

        vAdapter = new ViewAdapter();
        listview = (ListView)findViewById(R.id.mapList);
        listview.setAdapter(vAdapter);

        setting();


        te = (TextView)findViewById(R.id.location);
        findViewById(R.id.gps).setOnClickListener(mClickListener1);
        findViewById(R.id.insertButton).setOnClickListener(mClickListener2);
        findViewById(R.id.tMap).setOnClickListener(mClickListener3);
        findViewById(R.id.bTest).setOnClickListener(mClickListener4);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.optionmenu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(id) {
            case R.id.menu_set:
                Intent intent=new Intent(MainActivity.this,SetActivity.class);
                startActivity(intent);
                finish();
            case R.id.end:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    /* init Functnion */
    public  void init(){
        SQLiteDatabase db = manager.getWritableDatabase();
        MyApplication ma = (MyApplication)getApplication();
        try {
            Cursor cs = db.rawQuery("SELECT _id, NAME FROM Navi WHERE USEYN = 1", null);
            try {
                while (cs.moveToNext()) {
                    ma.setNaviDiv(cs.getString(0));
                }
            } catch (SQLiteException ex) {
                ex.printStackTrace();
            } finally {
                cs.close();
            }

            cs = db.rawQuery("SELECT KEY FROM APIKey WHERE _id = '" + ((MyApplication) getApplication()).getNaviDiv() + "'", null);
            try {
                while (cs.moveToNext()) {
                    ma.setNaviAPIKey(cs.getString(0));
                }
            } catch (SQLiteException ex) {
                ex.printStackTrace();
            } finally {
                cs.close();
            }

            cs = db.rawQuery("SELECT KEY FROM APIKey WHERE _id = 'HAPI'", null);

            try {
                while (cs.moveToNext()) {
                    ma.setJusoAPIKey(cs.getString(0));
                }
            } catch (SQLiteException ex) {
                ex.printStackTrace();
            } finally {
                cs.close();
            }
        }catch (SQLiteException ex) {
            ex.printStackTrace();
        } finally {
            db.close();
        }
    }
    public void setting(){
        SQLiteDatabase db = manager.getWritableDatabase();

        Cursor cs = db.rawQuery("SELECT _id, NAME FROM MapList", null);
        try {
            while (cs.moveToNext()) {
                vAdapter.addItem(cs.getString(0), String.valueOf(cs.getString(1)),"");
            }
            vAdapter.notifyDataSetChanged();
        } catch (SQLiteException ex) {
            ex.printStackTrace();
        } finally {
            cs.close();
        }
    }

    /* Event */
    Button.OnClickListener mClickListener1 = new View.OnClickListener(){
        public void  onClick(View v){
            GPSManager gps = new GPSManager(MainActivity.this);
            if(gps.isGetLocation()){
                double la = gps.getLatitude();
                double lo = gps.getLongitude();

                te.setText(String.valueOf(la) + ", " + String.valueOf(lo));
            }else{
                gps.showSettingsAlert();
            }
        }
    };

    Button.OnClickListener mClickListener2 = new View.OnClickListener(){
        public void  onClick(View v){
            Intent intent=new Intent(MainActivity.this,InsertMapList.class);
            startActivity(intent);
            finish();
        }
    };

    Button.OnClickListener mClickListener3 = new View.OnClickListener(){
        public void  onClick(View v){
            Intent intent=new Intent(MainActivity.this,tMapTest.class);
            startActivity(intent);
            finish();
        }
    };

    Button.OnClickListener mClickListener4 = new View.OnClickListener(){
        public void  onClick(View v){
            ArrayList<AddressVO> aList = new ArrayList<AddressVO>();
            AddressVO temp = new AddressVO();
            AddressVO cur = new AddressVO();

            temp.setLon(126.923744);
            temp.setLat(37.489519);
            temp.setZipNo("08706");
            aList.add(temp);

            temp = new AddressVO();
            temp.setLon(126.982233);
            temp.setLat(37.477700);
            temp.setZipNo("06693");
            aList.add(temp);
            temp = new AddressVO();
            temp.setLon(127.013483);
            temp.setLat(37.526731);
            temp.setZipNo("06232");
            aList.add(temp);
            temp = new AddressVO();
            temp.setLon(127.104444);
            temp.setLat(37.513750);
            temp.setZipNo("05551");
            aList.add(temp);

            cur.setLon(126.964219);
            cur.setLat(37.354258);

            CalDistance calDistance = new CalDistance();

            calDistance.getDistancePriority(aList, cur);


        }
    };
}
