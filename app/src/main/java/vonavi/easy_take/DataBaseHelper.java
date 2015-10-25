package vonavi.easy_take;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;

/**
 * Created by Валентин on 21.10.2015.
 */
public class DataBaseHelper extends SQLiteOpenHelper {
    private static String DB_NAME = "cinemadb";
    private static String DB_PATH = "/data/data/vonavi.easy_take/databases/";
    private final Context mContext;
    private SQLiteDatabase myDB;

    private static final String COLUMN_ID = "_id";
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

    public static final String DATABASE_TABLE_MAIN = "films";


    public DataBaseHelper(Context context) {
        super(context, DB_NAME, null, 1);
        this.mContext = context;
    }

    public void createDataBase() throws IOException{
        boolean dbExist = checkDataBase();
        if (dbExist) {

        }
        else
        {
            this.getReadableDatabase();

            try{
                copyDataBase();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }
    }

    private boolean checkDataBase() {
        SQLiteDatabase checkDB = null;

        try {
            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
        } catch  (SQLiteException e) {

        }
            if (checkDB != null) {
                checkDB.close();
            }

        return checkDB != null ? true : false;
    }

    private void copyDataBase() throws IOException {
        InputStream myInput = mContext.getAssets().open(DB_NAME);
        String outFileName = DB_PATH + DB_NAME;
        OutputStream myOutput = new FileOutputStream(outFileName);

        //перемещаем байты из входящего файла в исходящий
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, length);
        }

        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    public void openDataBase() throws SQLException {
        String myPath = DB_PATH + DB_NAME;
        myDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
    }

    @Override
    public synchronized void close() {
        if(myDB != null)
            myDB.close();
        super.close();
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public Cursor getTitles() {
        return myDB.query(DATABASE_TABLE_MAIN, new String[] {TITLE_COLUMNE, COLUMN_ID}, null,null,TITLE_COLUMNE, null, null, null );
    }

}
