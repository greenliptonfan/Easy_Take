package vonavi.easy_take;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;


public class StartActivity extends FragmentActivity implements LoaderCallbacks<Cursor> {

    Context context;

    Button buttonNew;

    Button buttonCloseNew;
    Button buttonCreateNew;

    FrameLayout workModeChoose;
    Button buttonOpenTake;
    Button buttonOpenTable;
    Button buttonCloseWorkModeChoose;

    ListView filmChooseList;

    EditText newFilmTitle;
    TextView filmTitle;

    LinearLayout filmChooseItem;
    TextView filmChoose;

    FrameLayout newFilmLayout;
    LinearLayout activityLL;

    DB db;
    SimpleCursorAdapter scAdapter;

    AlertDialog.Builder ad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        setRequestedOrientation(SCREEN_ORIENTATION_PORTRAIT);

        context = StartActivity.this;
//открываем БД
        db = new DB(this);
        db.open();
        String[] from = new String[] {DB.COLUMN_TITLE};
        int[] to = new int[] {R.id.filmChoose};

        filmChooseList = (ListView) findViewById(android.R.id.list);
        filmChooseItem = (LinearLayout) findViewById(R.id.filmChooseItem);
        filmChoose = (TextView) findViewById(R.id.filmChoose);

//заполняем ListView имеющимеся фильмами
        scAdapter = new SimpleCursorAdapter(this, R.layout.filmchooseitem, null, from, to,0);
        filmChooseList.setAdapter(scAdapter);

        getSupportLoaderManager().initLoader(0, null, this);

        workModeChoose = (FrameLayout) findViewById(R.id.workModeChoose);
        buttonOpenTake = (Button) findViewById(R.id.buttonOpenTake);
        //buttonOpenTable = (Button) findViewById(R.id.buttonOpenTable);
        buttonCloseWorkModeChoose = (Button) findViewById(R.id.buttonCloseWorkModeChoose);
        filmTitle = (TextView) findViewById(R.id.filmTitle);
//открытие WorkChooseMode
        filmChooseList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView v = (TextView) view.findViewById(R.id.filmChoose);
                final String FilmT = v.getText().toString();


                activityLL.setVisibility(View.GONE);
                workModeChoose.setVisibility(View.VISIBLE);
                filmTitle.setText(FilmT);

                buttonOpenTake.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(StartActivity.this, TakeActivity.class);
                        intent.putExtra("title",FilmT);
                        startActivity(intent);
                        workModeChoose.setVisibility(View.INVISIBLE);
                        activityLL.setVisibility(View.VISIBLE);
                    }
                });

                /*buttonOpenTable.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(StartActivity.this, TableActivity.class);
                        intent.putExtra("title",FilmT);
                        startActivity(intent);
                        workModeChoose.setVisibility(View.INVISIBLE);
                        activityLL.setVisibility(View.VISIBLE);
                    }
                });
*/
                buttonCloseWorkModeChoose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        workModeChoose.setVisibility(View.INVISIBLE);
                        activityLL.setVisibility(View.VISIBLE);
                    }
                });

            }
        });
//удаление элемента
        filmChooseList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                TextView v = (TextView) view.findViewById(R.id.filmChoose);
                final String FilmT = v.getText().toString();

                String title = "Удаление фильма";
                String message = "Вы собиаретесь удалить фильм " + FilmT + " навсегда? Все данные будут потеряны.";
                String btnOkString = "Да";
                String btnCancelString = "Отмена";

                ad = new AlertDialog.Builder(context);
                ad.setTitle(title);
                ad.setMessage(message);
                ad.setPositiveButton(btnOkString, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {
                        db.delRecInFilms(FilmT);
                        getSupportLoaderManager().getLoader(0).forceLoad();
                        Toast.makeText(context, "Фильм удален",
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

                return false;
            }
        });

//создание нового фильма
        newFilmLayout = (FrameLayout) findViewById(R.id.newFilmLayout);
        buttonNew = (Button) findViewById(R.id.buttonNew);
        activityLL = (LinearLayout) findViewById(R.id.activityLL);
        newFilmTitle = (EditText) findViewById(R.id.newFilmTitle);
        buttonNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityLL.setVisibility(View.GONE);
                newFilmLayout.setVisibility(View.VISIBLE);
                newFilmTitle.setText("");

            }
        });

        buttonCreateNew = (Button) findViewById(R.id.buttonCreateNew);
        buttonCloseNew = (Button) findViewById(R.id.buttonCloseNew);

        buttonCreateNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Title = newFilmTitle.getText().toString();
                if (Title.length() != 0) {
                    db.addRecInFilms(Title);
                    getSupportLoaderManager().getLoader(0).forceLoad();
                    buttonCloseNew.callOnClick();
                } else {
                    Toast.makeText(StartActivity.this, "Вы выбрали пустое название", Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonCloseNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newFilmLayout.setVisibility(View.INVISIBLE);
                activityLL.setVisibility(View.VISIBLE);
            }
        });



    }

    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_start, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new MyCursorLoader(this, db);
    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        scAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    static class MyCursorLoader extends CursorLoader {
        DB db;

        public MyCursorLoader(Context context, DB db) {
            super(context);
            this.db=db;
        }

        @Override
        public Cursor loadInBackground() {
            Cursor cursor = db.getAllDataInFilms();
            return cursor;
        }
    }
}
