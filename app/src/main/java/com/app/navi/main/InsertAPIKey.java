package com.app.navi.main;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.navi.R;
import utility.DbManager;

/**
 * Created by 95016056 on 2017-05-30.
 */

public class InsertAPIKey extends AppCompatActivity {
    DbManager manager;
    LinearLayout linearLayout;
    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manager = new DbManager(this, "NAVIDB1.db", null, 1);
        setContentView(R.layout.apikey_main);


        linearLayout = (LinearLayout)findViewById(R.id.dynamicArea);
        init();

        findViewById(R.id.apisave).setOnClickListener(mClickListener_save);
        findViewById(R.id.apicancel).setOnClickListener(mClickListener_cancle);
    }

    private void init(){
        linearLayout.destroyDrawingCache();
        SQLiteDatabase db = manager.getWritableDatabase();
        Cursor cs = db.rawQuery("SELECT _id, NAME, KEY FROM APIKey", null);
        try {
            while (cs.moveToNext()) {
                TextView textView = new TextView(InsertAPIKey.this);
                textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                textView.setTextSize(15);
                textView.setText(cs.getString(1));
                linearLayout.addView(textView);

                EditText editText = new EditText(InsertAPIKey.this);
                editText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                editText.setTextSize(13);
                editText.setText(cs.getString(2));
                editText.setPrivateImeOptions(cs.getString(0));
                editText.setId(i++);
                linearLayout.addView(editText);
            }
        }catch(SQLiteException ex){
            ex.printStackTrace();
        }finally {
            cs.close();
            db.close();
        }
    }

    Button.OnClickListener mClickListener_save = new View.OnClickListener(){
        public void  onClick(View v){
            SQLiteDatabase db = manager.getWritableDatabase();
            try {
                db.beginTransaction();
                for (int j = 0; j < i; j++) {
                    EditText text = (EditText) findViewById(j);
                    ContentValues values = new ContentValues();
                    values.put("KEY", text.getText().toString());
                    db.update("APIKey",values, "_id=?", new String[] {text.getPrivateImeOptions().toString()} );
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

    Button.OnClickListener mClickListener_cancle = new View.OnClickListener(){
        public void  onClick(View v){
        finish();
        }
    };


}
