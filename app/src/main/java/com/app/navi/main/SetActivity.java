package com.app.navi.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.app.navi.R;

import java.util.ArrayList;

/**
 * Created by pirate on 2017-06-02.
 */

public class SetActivity extends AppCompatActivity {


    private ArrayList<String> arrayMenu = new ArrayList<String>();
    //DbManager manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_main);
        //manager = new DbManager(this, "NAVIDB1.db", null, 1);
      //  setArrayMenu();

       // ListView listMenu = (ListView) this.findViewById(R.id.menulist);
      //  ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayMenu);
       // listMenu.setAdapter(adapter);

      //  listMenu.setOnItemClickListener(listener);
    }

  /*  *//* 메뉴 클릭 이벤트 *//*
    OnItemClickListener listener= new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // 메뉴 선택별 Popup 메뉴 변경
            switch(position){
                case 0:
                    setNavigation();
                    break;
                case 1:
                    setApiKey();
                    break;
                default:
                    Toast.makeText(SettingActivity.this, arrayMenu.get(position), Toast.LENGTH_SHORT).show();
            }
        }
    };*/


   /* *//* 네비 선택 *//*
    private void setNavigation(){
        String items[] = {};
        int i = 0;
        SQLiteDatabase db = manager.getWritableDatabase();
        Cursor cs = db.rawQuery("SELECT _id, NAME FROM Navi", null);

        try{
            items = new String[cs.getCount()];
            while(cs.moveToNext()){
                items[i++] = cs.getString(1);
            }
        }catch(SQLiteException ex){
            ex.printStackTrace();
        }finally {
            cs.close();
            db.close();
        }

        AlertDialog.Builder ab = new AlertDialog.Builder(SettingActivity.this);
        ab.setTitle("네비선택");
        ab.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton){
            }
        }).setPositiveButton("선택", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton){
                //Toast.makeText(SettingActivity.this, String.valueOf(dialog.), Toast.LENGTH_LONG).show();
            }

        }).setNegativeButton("취소",  new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });
        ab.show();
    }

    *//* 네비 API  Key 등록 *//*
    private void setApiKey(){
        Intent intent = new Intent(SettingActivity.this, InsertAPIKey.class);
        startActivity(intent);
    }

    *//* 메뉴 항목 생성 *//*
    private void setArrayMenu(){
        SQLiteDatabase db = manager.getWritableDatabase();
        Cursor cs = db.rawQuery("SELECT _id, NAME FROM SetMenu", null);
        try {
            while (cs.moveToNext()) {
                arrayMenu.add(cs.getString(1));
            }
        }catch(SQLiteException ex){
            ex.printStackTrace();
        }finally {
            cs.close();
            db.close();
        }

    }*/
}
