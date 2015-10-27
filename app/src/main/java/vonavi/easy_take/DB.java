package vonavi.easy_take;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Валентин on 25.10.2015.
 */
public class DB {

    private final Context mCtx;
    private DBHelper mDBHelper;
    private SQLiteDatabase myDB;

    private static final String DB_NAME = "myDB";
    private static final int DB_VERSION = 1;
    private static final String DB_TABLE_MAIN = "films";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TITLE = "title";

    /**
     public static final String DATABASE_TABLE = "cinema";
     public static final String TITLE_COLUMNE = "Title";
     public static final String SCENE_COLUMNE = "Scene";
     public static final String SHOT_COLUMNE = "Shot";
     public static final String TAKE_COLUMNE = "Take";
     public static final String CAM1_COLUMNE = "Cam1";
     public static final String CAM2_COLUMNE = "Cam2";
     public static final String CAM3_COLUMNE = "Cam3";
     public static final String REC1_COLUMNE = "Rec1";
     public static final String REC2_COLUMNE = "Rec2";
     public static final String REC3_COLUMNE = "Rec3";
     public static final String DIRECTOR_COLUMNE = "Director";
     public static final String DATE_COLUMNE = "Date";

     **/
    public DB(Context ctx) {
        mCtx = ctx;
    }

    // открыть подключение
    public void open() {
        mDBHelper = new DBHelper(mCtx, DB_NAME, null, DB_VERSION);
        myDB = mDBHelper.getWritableDatabase();
    }

    // закрыть подключение
    public void close() {
        if (mDBHelper!=null) mDBHelper.close();
    }

    // получить все данные из таблицы DB_TABLE_MAIN
    public Cursor getAllDataInFilms() {
        return myDB.query(DB_TABLE_MAIN, null, null, null, null, null, null);
    }

    // добавить запись в DB_TABLE
    public void addRecInFilms(String title) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_TITLE, title);
        myDB.insert(DB_TABLE_MAIN, null, cv);
    }

    // удалить запись из DB_TABLE
    public void delRecInFilms(long id) {
        myDB.delete(DB_TABLE_MAIN, COLUMN_ID + " = " + id, null);
    }

    public class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("create table " + DB_TABLE_MAIN + "(" +
                    COLUMN_ID + " integer primary key autoincrement," +
                    COLUMN_TITLE + " text not null" + ");");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }

    }



}
