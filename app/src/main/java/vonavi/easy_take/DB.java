package vonavi.easy_take;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Валентин on 25.10.2015.
 */
public class DB {

    private final Context mCtx;
    private DBHelper mDBHelper;
    private SQLiteDatabase myDB;

    private static final String DB_NAME = "myDB";
    private static final int DB_VERSION = 14;
    private static final String DB_TABLE_MAIN = "films";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TITLE = "title";

    private static final String DB_TABLE_SC = "sceneTable";
    public static final String TITLE_ID = "title_id";
    public static final String SCENE = "scene";
    public static final String ID_SC = "_id";

    private static final String DB_TABLE_SH = "shotTable";
    public static final String SCENE_ID = "scene_id";
    public static final String SHOT = "shot";
    public static final String ID_SH = "_id";

     private static final String DB_TABLE = "cinema";
     public static final String ID_COLUMNE = "_id";;
     public static final String SHOT_ID = "shot_id";
     public static final String TAKE_COLUMNE = "take";
     public static final String CAM1_COLUMNE = "cam1";
     public static final String CAM2_COLUMNE = "cam2";
     public static final String CAM3_COLUMNE = "cam3";
     public static final String REC1_COLUMNE = "rec1";
     public static final String REC2_COLUMNE = "rec2";
     public static final String REC3_COLUMNE = "rec3";
     public static final String DIRECTOR_COLUMNE = "director";
     public static final String DATE_COLUMNE = "date";


    final String LOG_TAG = "myLogs";



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

    // получить данные из таблицы DB_TABLE_SC с указанием фильма и с сортировкой по имени
    public Cursor getDataInScenes(long t_id) {
        return myDB.query(DB_TABLE_SC, null , TITLE_ID + " = " + t_id, null , null, null, SCENE);
    }

    //получить данные из таблицы DB_TABLE_SH  с указанием фильма, сцены и сортировкой по имени
    public Cursor getDataInShots(long scene_id) {
        return myDB.query(DB_TABLE_SH, null, SCENE_ID + " = " + scene_id, null, null, null, SHOT);
    }

    //получить данные из таблицы DB_TABLE с указанием фильма, сцены, кадра и сортировкой по имени
    public Cursor getData(long shot_id) {
        return myDB.query(DB_TABLE, null, SHOT_ID + " = " + shot_id, null, null, null, TAKE_COLUMNE);
    }

    //получить номер следующего дубля
    public int getNextTake(long shot_id) {
        Cursor c = getData(shot_id);
        int columnIndex_id = c.getColumnIndex(TAKE_COLUMNE);
        int lastTake;
        if (c.moveToLast()) {
            lastTake = c.getInt(columnIndex_id);
        } else {
            lastTake = 0;
        }
        return lastTake + 1;
    }


    //получить id фильма из таблицы DB_TABLE_MAIN
    public long getTitleID(String t) {
        Cursor c;
        c = myDB.query(DB_TABLE_MAIN, new String[]{COLUMN_ID, COLUMN_TITLE}, COLUMN_TITLE + " = '" + t + "'", null, null, null, null);
        int columnIndex_id = c.getColumnIndex(COLUMN_ID);
        c.moveToFirst();
        long t_id = c.getInt(columnIndex_id);
        return t_id;
    }

    //получить id сцены из таблицы DB_TABLE_SC
    public long getSceneID(String sc, long title_id) {
        Cursor c;
        c = myDB.query(DB_TABLE_SC, new String[]{SCENE, ID_SC, TITLE_ID}, TITLE_ID + " = " + title_id + " AND " + SCENE + " = '" + sc + "'", null, null, null, null);
        int columnIndex_id = c.getColumnIndex(ID_SC);
        c.moveToFirst();
        long sc_id = c.getInt(columnIndex_id);
        return sc_id;
    }

    //получить id кадра из таблицы DB_TABLE_SH
    public long getShotID(String sh, long   scene_id) {
        Cursor c;
        c = myDB.query(DB_TABLE_SH, null , SCENE_ID + " = " + scene_id + " AND " + SHOT + " = '" + sh + "'", null, null, null, null);
        int columnIndex_id = c.getColumnIndex(ID_SH);
        c.moveToFirst();
        long sh_id = c.getInt(columnIndex_id);
        return sh_id;
    }

    // добавить запись в DB_TABLE_MAIN
    public void addRecInFilms(String title) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_TITLE, title);
        myDB.insert(DB_TABLE_MAIN, null, cv);
    }

    // добавить запись в DB_TABLE_SC
    public void addRecInScenes(long id_title, String scene) {
        ContentValues cv = new ContentValues();
        cv.put(TITLE_ID, id_title);
        cv.put(SCENE, scene);
        myDB.insert(DB_TABLE_SC, null, cv);
    }

    // добавить запись в DB_TABLE_SH
    public void addRecInShots(long id_scene, String shot) {
        ContentValues cv = new ContentValues();
        cv.put(SCENE_ID, id_scene);
        cv.put(SHOT, shot);
        myDB.insert(DB_TABLE_SH, null, cv);
    }

    // добавить запись в DB_TABLE
    public void addRec(long id_shot, int take, String cam1, String cam2,String cam3,String rec1,String rec2,String rec3,String dir, String date) {
        ContentValues cv = new ContentValues();
        cv.put(SHOT_ID, id_shot);
        cv.put(TAKE_COLUMNE, take);
        cv.put(CAM1_COLUMNE, cam1);
        cv.put(CAM2_COLUMNE, cam2);
        cv.put(CAM3_COLUMNE, cam3);
        cv.put(REC1_COLUMNE, rec1);
        cv.put(REC2_COLUMNE, rec2);
        cv.put(REC3_COLUMNE, rec3);
        cv.put(DIRECTOR_COLUMNE, dir);
        cv.put(DATE_COLUMNE, date);
        myDB.insert(DB_TABLE, null, cv);
    }

    // удалить запись из DB_TABLE_MAIN
    public void delRecInFilms(String title) {
        myDB.delete(DB_TABLE_MAIN, "title = '" + title + "'", null);
    }

    // удалить запись из DB_TABLE_SC
    public void delRecInScenes(String scene) {
        myDB.delete(DB_TABLE_SC, "scene = '" + scene + "'", null);
    }

    // удалить запись из DB_TABLE_SH
    public void delRecInShots(String shot) {
        myDB.delete(DB_TABLE_SH, "shot = '" + shot + "'", null);
    }

    // удалить запись из DB_TABLE
    public void delRec(int take) {
        myDB.delete(DB_TABLE, "take = " + take, null);
    }


    public class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("create table if not exists " + DB_TABLE_MAIN + "(" +
                    COLUMN_ID + " integer primary key autoincrement," +
                    COLUMN_TITLE + " text not null" + "); ");

            db.execSQL("create table if not exists " + DB_TABLE_SC + "(" + ID_SC + " integer primary key autoincrement," +
                            TITLE_ID + " integer references " + DB_TABLE_MAIN + "(" + COLUMN_ID + ") on delete cascade," +
                            SCENE + " text); ");
            db.execSQL("create table if not exists " + DB_TABLE_SH + "(" + ID_SH + " integer primary key autoincrement," +
                            SCENE_ID + " integer references " + DB_TABLE_SC + "(" + ID_SC + ") on delete cascade," +
                            SHOT + " text); ");
            db.execSQL("create table if not exists " + DB_TABLE + "(" + ID_COLUMNE + " integer primary key autoincrement," +
                            SHOT_ID + " integer references " + DB_TABLE_SH + "(" + ID_SH + ") on delete cascade," +
                            TAKE_COLUMNE + " integer," + CAM1_COLUMNE + " text," + CAM2_COLUMNE + " text," + CAM3_COLUMNE + " text," +
                            REC1_COLUMNE + " text," + REC2_COLUMNE + " text," + REC3_COLUMNE + " text," +
                            DIRECTOR_COLUMNE + " text, " + DATE_COLUMNE + " text); ");
            Log.d(LOG_TAG, "onCreate DB");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.d(LOG_TAG, " --- onUpgrade database from " + oldVersion
                    + " to " + newVersion + " version --- ");
            db.execSQL("drop table if exists " + DB_TABLE);
            onCreate(db);
        }

    }



}
