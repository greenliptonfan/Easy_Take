package vonavi.easy_take;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

/**
 * Created by Валентин on 07.11.2015.
 */

public class TableActivity extends Activity {


    ExpandableListView elCinema;
    DB db;

    Button btnRet;
    ListView shotView;

    FrameLayout takeLayout;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        context = TableActivity.this;

        elCinema = (ExpandableListView) findViewById(R.id.elCinema);
        db = new DB(this);

        db.open();

        takeLayout = (FrameLayout) findViewById(R.id.takeLayout);
                takeLayout.setVisibility(View.VISIBLE);
                elCinema.setVisibility(View.INVISIBLE);

                String[] from = new String[]{db.TAKE_COLUMNE, db.CAM1_COLUMNE, db.CAM2_COLUMNE, db.CAM3_COLUMNE,
                        db.REC1_COLUMNE, db.REC2_COLUMNE, db.REC3_COLUMNE, db.DIRECTOR_COLUMNE, db.DATE_COLUMNE};
                int[] to = new int[]{R.id.takeView, R.id.cam1View, R.id.cam2View, R.id.cam3View,
                        R.id.rec1View, R.id.rec2View, R.id.rec3View, R.id.dirView, R.id.dateView};


                String SceneT = getIntent().getExtras().getString("sceneT");
                String ShotT = getIntent().getExtras().getString("shotT");

        Cursor cursor1 = db.getData(db.getShotID(ShotT,
                db.getSceneID(SceneT,
                        db.getTitleID(getIntent().getExtras().getString("title")))));


        SimpleCursorAdapter scAdapterSH = new SimpleCursorAdapter(TableActivity.this,
                R.layout.takeitem, cursor1, from, to, 0);
        shotView = (ListView) findViewById(R.id.shotView);
        shotView.setAdapter(scAdapterSH);


        btnRet = (Button) findViewById(R.id.btnRet);
        btnRet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }

}
