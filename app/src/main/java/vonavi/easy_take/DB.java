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
