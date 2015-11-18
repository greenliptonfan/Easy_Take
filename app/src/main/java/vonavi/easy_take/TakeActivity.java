package vonavi.easy_take;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Objects;

/**
 * Created by Валентин on 28.10.2015.
 */
public class TakeActivity extends FragmentActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    Context context;

    LinearLayout activityTLt;

    Button button_start;
    Button button_table;
    boolean button_start_clicked;

    TextView title_show;
    TextView take_show;

    EditText cam1_edit;
    EditText cam2_edit;
    EditText cam3_edit;

    EditText rec1_edit;
    EditText rec2_edit;
    EditText rec3_edit;

    TextView scene_edit;
    TextView shot_edit;
    EditText director_edit;

    FrameLayout chooseS;
    ListView sListView;
    EditText addEditText;
    Button btnAdd;

    SimpleCursorAdapter sceneAdapter;
    DB db;

    AlertDialog.Builder ad;

    static int s;

    static String tt;
    static String sc;
    static Boolean firstlyClicked = true;

    final String LOG_TAG = "myLogs";

    @Override
    protected void onCreate(Bundle savedInstantState) {
        super.onCreate(savedInstantState);
        setContentView(R.layout.activity_take);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        firstlyClicked = true;
        s = 0;
        tt = getIntent().getExtras().getString("title");
        title_show = (TextView) findViewById(R.id.title_show);
        take_show = (TextView) findViewById(R.id.take_show);
        button_start = (Button) findViewById(R.id.button_start);
        cam1_edit = (EditText) findViewById(R.id.cam1_edit);
        cam2_edit = (EditText) findViewById(R.id.cam2_edit);
        cam3_edit = (EditText) findViewById(R.id.cam3_edit);

        rec1_edit = (EditText) findViewById(R.id.rec1_edit);
        rec2_edit = (EditText) findViewById(R.id.rec2_edit);
        rec3_edit = (EditText) findViewById(R.id.rec3_edit);

        scene_edit = (TextView) findViewById(R.id.scene_edit);
        shot_edit = (TextView) findViewById(R.id.shot_edit);
        director_edit = (EditText) findViewById(R.id.director_edit);

        title_show.setText(getIntent().getExtras().getString("title"));

        //отображение текущей даты
        final TextView date_show = (TextView) findViewById(R.id.date_show);
        long date = System.currentTimeMillis();

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        String dateString = sdf.format(date);
        date_show.setText(dateString);

        //выбор сцены и кадра
        scene_edit.setEnabled(true);
        shot_edit.setEnabled(false);
        button_start.setEnabled(false);

        chooseS = (FrameLayout) findViewById(R.id.chooseS);
        sListView = (ListView) findViewById(R.id.slistView);
        addEditText = (EditText) findViewById(R.id.addEditText);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        activityTLt = (LinearLayout) findViewById(R.id.activityLL);

        db = new DB(this);
        db.open();

        context = TakeActivity.this;

        take_show.setText("-");
        //выбор сцены
        scene_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shot_edit.setText("-");
                take_show.setText("-");
                shot_edit.setEnabled(true);
                button_start.setEnabled(false);
                chooseS.setVisibility(View.VISIBLE);
                String[] fromSC = new String[]{DB.SCENE};
                int[] toSC = new int[]{R.id.sceneChoose};
                s = 0;
                sceneAdapter = new SimpleCursorAdapter(TakeActivity.this, R.layout.scenechooseitem, null, fromSC, toSC, 0);
                sListView.setAdapter(sceneAdapter);
                if (firstlyClicked) {
                    getSupportLoaderManager().initLoader(0, null, TakeActivity.this);
                    firstlyClicked = false;
                }
                getSupportLoaderManager().getLoader(0).forceLoad();

            }

        });
        //выбор кадра
        shot_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseS.setVisibility(View.VISIBLE);
                s = 1;
                String[] fromSH = new String[]{DB.SHOT};
                int[] toSH = new int[]{R.id.sceneChoose};

                sceneAdapter = new SimpleCursorAdapter(TakeActivity.this, R.layout.scenechooseitem, null, fromSH, toSH, 0);
                sListView.setAdapter(sceneAdapter);
                getSupportLoaderManager().getLoader(0).forceLoad();
            }
        });
        //добавление сцены/кадра
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long title_id;
                String title = title_show.getText().toString();
                title_id = db.getTitleID(title);
                if (s == 0) {
                    //добавляем сцену
                    String addT = addEditText.getText().toString();
                    if (addT.length() != 0) {
                        db.addRecInScenes(title_id, addT);
                        getSupportLoaderManager().getLoader(0).forceLoad();
                        addEditText.setText("");
                    } else {
                        Toast.makeText(TakeActivity.this, "Вы выбрали пустое значение", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    //добавляем кадр
                    long scene_id = db.getSceneID(sc, title_id);
                    String addS = addEditText.getText().toString();
                    if (addS.length() != 0) {
                        db.addRecInShots(scene_id, addS);
                        getSupportLoaderManager().getLoader(0).forceLoad();
                        addEditText.setText("");
                    } else {
                        Toast.makeText(TakeActivity.this, "Вы выбрали пустое значение", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        //отображение сцены/кадра и текущего дубля
        sListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String FilmT = title_show.getText().toString();
                String FilmSC = scene_edit.getText().toString();

                TextView v = (TextView) view.findViewById(R.id.sceneChoose);
                final String sss = v.getText().toString();
                if (s == 0) {
                    scene_edit.setText(sss);
                    sc = sss;
                } else {
                    shot_edit.setText(sss);
                    int stri = db.getNextTake(db.getShotID(sss, db.getSceneID(FilmSC, db.getTitleID(FilmT))));
                    if (stri != 0) {
                        take_show.setText(Integer.toString(stri));
                    } else {
                        take_show.setText("-");
                    }
                    button_start.setEnabled(true);
                }
                chooseS.setVisibility(View.INVISIBLE);
            }
        });
        //удаление сцены/кадра
        sListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                TextView v = (TextView) view.findViewById(R.id.sceneChoose);
                final String FilmS = v.getText().toString();
                String title = "Удаление";
                String message1 = "Вы собиаретесь удалить ";
                String message2 = FilmS + " навсегда? Все данные будут потеряны.";
                String btnOkString = "Да";
                String btnCancelString = "Отмена";
                String message = "";
                ad = new AlertDialog.Builder(context);
                ad.setTitle(title);
                if (s == 0) {
                   message = "сцену ";
                } else {
                   message = "кадр ";
                }
                ad.setMessage(message1 + message + message2);
                ad.setPositiveButton(btnOkString, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {
                        if (s == 0) {
                            db.delRecInScenes(FilmS);
                        } else {
                            db.delRecInShots(FilmS);
                        }
                        getSupportLoaderManager().getLoader(0).forceLoad();
                        Toast.makeText(context, "Удалено",
                                Toast.LENGTH_LONG).show();
                    }
                });
                ad.setNegativeButton(btnCancelString, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {
                        Toast.makeText(context, "Удаление отменено", Toast.LENGTH_SHORT)
                                .show();
                    }
                });

                ad.show();

                getSupportLoaderManager().getLoader(0).forceLoad();

                return false;
            }
        });


        //реализация кнопки мотор
        button_start_clicked = false;

        button_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Chronometer chr = (Chronometer) findViewById(R.id.chr);
                String FilmT = title_show.getText().toString();
                String FilmSC = scene_edit.getText().toString();
                String FilmSH = shot_edit.getText().toString();
                if (!button_start_clicked) {
                    chr.stop();
                    chr.setBase(SystemClock.elapsedRealtime());
                    chr.start();
                    button_start_clicked = true;
                    button_start.setText("СТОП!");
                    //защитить поля от изменений
                    cam1_edit.setEnabled(false);
                    cam2_edit.setEnabled(false);
                    cam3_edit.setEnabled(false);
                    rec1_edit.setEnabled(false);
                    rec2_edit.setEnabled(false);
                    rec3_edit.setEnabled(false);
                    scene_edit.setEnabled(false);
                    shot_edit.setEnabled(false);
                    director_edit.setEnabled(false);
                    Log.d(LOG_TAG, "3");
                    //добавление новой записи в таблицу TABLE_MAIN
                    int take = Integer.parseInt(take_show.getText().toString());
                    Log.d(LOG_TAG, "2");
                    db.addRec(
                            db.getShotID(FilmSH, db.getSceneID(FilmSC, db.getTitleID(FilmT))),
                            take,
                            cam1_edit.getText().toString(),
                            cam2_edit.getText().toString(),
                            cam3_edit.getText().toString(),
                            rec1_edit.getText().toString(),
                            rec2_edit.getText().toString(),
                            rec3_edit.getText().toString(),
                            director_edit.getText().toString(),
                            date_show.getText().toString());
                    Cursor cc = db.getData(db.getShotID(FilmSH, db.getSceneID(FilmSC, db.getTitleID(FilmT))));
                    cc.moveToFirst();
                    int indexC_id = cc.getColumnIndex("director");
                    String que = cc.getString(indexC_id);
                    Log.d(LOG_TAG, "1" + que);
                } else {
                    chr.stop();
                    chr.setBase(SystemClock.elapsedRealtime());
                    button_start_clicked = false;
                    button_start.setText("МОТОР!");
                    int stri = db.getNextTake(db.getShotID(FilmSH, db.getSceneID(FilmSC, db.getTitleID(FilmT))));
                    if (stri != 0) {
                        take_show.setText(Integer.toString(stri));
                    } else {
                        take_show.setText("-");
                    }
                    //открыть доступ к полям
                    cam1_edit.setEnabled(true);
                    cam2_edit.setEnabled(true);
                    cam3_edit.setEnabled(true);
                    rec1_edit.setEnabled(true);
                    rec2_edit.setEnabled(true);
                    rec3_edit.setEnabled(true);
                    scene_edit.setEnabled(true);
                    shot_edit.setEnabled(true);
                    director_edit.setEnabled(true);
                }
            }
        });
        //реализация кнопки таблица
        final String FilmT = title_show.getText().toString();
        button_table = (Button) findViewById(R.id.button_table);
        button_table.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                String SceneT = scene_edit.getText().toString();
                String ShotT = shot_edit.getText().toString();
                if (SceneT.equals("-")) {
                    Toast toastSc = Toast.makeText(context, "Сцена не выбрана", Toast.LENGTH_LONG);
                    toastSc.show();
                } else {
                    if (ShotT.equals("-")) {
                        Toast toastSh = Toast.makeText(context, "Кадр не выбран", Toast.LENGTH_LONG);
                        toastSh.show();
                    } else {
                        int nextTakeT = db.getNextTake(db.getShotID(ShotT,
                        db.getSceneID(SceneT,
                                db.getTitleID(getIntent().getExtras().getString("title")))));
                        if (nextTakeT!=1) {
                            Intent intent = new Intent(TakeActivity.this, TableActivity.class);
                            intent.putExtra("title", FilmT);
                            intent.putExtra("sceneT", SceneT);
                            intent.putExtra("shotT", ShotT);
                            startActivity(intent);
                        } else {
                            Toast toastNF = Toast.makeText(context, "Вы еще не снимали этот кадр", Toast.LENGTH_LONG);
                            toastNF.show();
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        db.close();
    }

    //cursorLoader
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new MyCursorLoaderTake(this, db);
    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        sceneAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    static class MyCursorLoaderTake extends CursorLoader {
        DB db;

        public MyCursorLoaderTake(Context context, DB db) {
            super(context);
            this.db=db;
        }

        @Override
        public Cursor loadInBackground() {
            Cursor cursor = null;
            long title_id = db.getTitleID(tt);
            if (s == 0) {
                cursor = db.getDataInScenes(title_id);
            } else {
                long scene_id = db.getSceneID(sc, title_id);
                cursor = db.getDataInShots(scene_id);
            }

            return cursor;
        }
    }
}
