package vonavi.easy_take;


//import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
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

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;


public class StartActivity extends FragmentActivity implements LoaderCallbacks<Cursor> {

    Button buttonNew;

    Button buttonCloseNew;
    Button buttonCreateNew;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        setRequestedOrientation(SCREEN_ORIENTATION_PORTRAIT);

        db = new DB(this);
        db.open();

        String[] from = new String[] {DB.COLUMN_TITLE};
        int[] to = new int[] {R.id.filmChoose};



        filmChooseList = (ListView) findViewById(android.R.id.list);
        filmChooseItem = (LinearLayout) findViewById(R.id.filmChooseItem);
        filmChoose = (TextView) findViewById(R.id.filmChoose);

        scAdapter = new SimpleCursorAdapter(this, R.layout.filmchooseitem, null, from, to,0);
        filmChooseList.setAdapter(scAdapter);

        getSupportLoaderManager().initLoader(0, null, this);

        filmChooseList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

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

                db.addRecInFilms(Title);
                getSupportLoaderManager().getLoader(0).forceLoad();
                buttonCloseNew.callOnClick();
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
