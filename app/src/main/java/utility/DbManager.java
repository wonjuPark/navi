package utility;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by 95016056 on 2017-05-26.
 */

public class DbManager extends SQLiteOpenHelper {
    private Context context;
    //private SQLiteDatabase sqliteDB = null;


    public DbManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, name, factory, version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db){

        db.execSQL("CREATE TABLE SetMenu(_id INTEGER primary key autoincrement, NAME TEXT)");
        db.execSQL("CREATE TABLE Navi(_id TEXT, NAME TEXT, USEYN INTEGER)");

        db.execSQL("CREATE TABLE APIKey(_id TEXT, NAME TEXT, KEY TEXT);");

        db.execSQL("CREATE TABLE MapList(_id INTEGER primary key autoincrement, NAME TEXT)");


        db.execSQL("CREATE TABLE OptMap(_id INTEGER primary key autoincrement, REL_ID INTEGER, NAME TEXT, X text, Y text, SEQ INTEGER)");


        db.execSQL("INSERT INTO SetMenu (NAME)  VALUES ('내비선택');");
        db.execSQL("INSERT INTO SetMenu (NAME) VALUES ('API Key 세팅');");

        db.execSQL("INSERT INTO Navi (_id, NAME, USEYN) VALUES ('TAPI', 'T_MAP', 1);");
        db.execSQL("INSERT INTO Navi (_id, NAME, USEYN) VALUES ('KAPI', 'KAKAO_NAVI', 0);");

        db.execSQL("INSERT INTO APIKey (_id, NAME, KEY) VALUES ('TAPI', 'T_MAP_API', '8c7c0140-221f-35c9-997d-a3506f8748ef');");
        db.execSQL("INSERT INTO APIKey (_id, NAME, KEY) VALUES ('HAPI', 'JUSO_API', 'U01TX0FVVEgyMDE3MDUzMTE0NDAxNzIxNTYy');");


    }

    @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
