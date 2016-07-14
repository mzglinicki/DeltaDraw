package com.project.mzglinicki96.deltaDraw.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.project.mzglinicki96.deltaDraw.Constants;
import com.project.mzglinicki96.deltaDraw.R;
import com.project.mzglinicki96.deltaDraw.adapters.CustomViewPager;
import com.project.mzglinicki96.deltaDraw.adapters.PagerAdapter;
import com.project.mzglinicki96.deltaDraw.database.DatabaseHelper;
import com.project.mzglinicki96.deltaDraw.PointListHolder;
import com.project.mzglinicki96.deltaDraw.eventBus.GimBus;
import com.project.mzglinicki96.deltaDraw.eventBus.OnCreatePictureEvent;
import com.project.mzglinicki96.deltaDraw.fragments.DrawerFragment;
import com.project.mzglinicki96.deltaDraw.fragments.DrawerOnScreen;
import com.project.mzglinicki96.deltaDraw.fragments.EditorFragment;
import com.project.mzglinicki96.deltaDraw.fragments.FragmentParent;
import com.pgssoft.gimbus.EventBus;
import com.pgssoft.gimbus.Subscribe;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class DrawCreatingActivity extends AppCompatActivity {

    private final DatabaseHelper drawDatabase = DatabaseHelper.getInstance(this);
    private final EventBus gimBus = GimBus.getInstance();
    private List<Point> coordinatesList;
    private List<Point> initialList;

    private int id = 0;
    private boolean isSaved;
    private String name;
    private String author;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupUI();
    }

    @Override
    protected void onStart() {
        super.onStart();
        gimBus.register(this);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        super.onCreateOptionsMenu(menu);
        final MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.saved:
                saved();
                return true;
            case R.id.clearLastShape:
                clearLastShape();
                return true;
            case R.id.lib_menu_clear:
                clear();
                return true;
            case R.id.share:
                share();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        checkChanges();
    }

    @Override
    protected void onStop() {
        super.onStop();
        gimBus.unregister(this);
    }

    public void setupUI() {
        final CustomViewPager viewPager = (CustomViewPager) findViewById(R.id.viewpager);
        assert viewPager != null;
        viewPager.setAdapter(new PagerAdapter(this, getSupportFragmentManager(), createFragments()));

        final TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        assert tabLayout != null;
        tabLayout.setupWithViewPager(viewPager);
    }

    private void startActivity(final Class newActivity) {

        final Intent intent = new Intent(DrawCreatingActivity.this, newActivity);
        startActivity(intent);
        boolean isDatabaseActivity = newActivity.equals(DatabaseActivity.class);
        if(isDatabaseActivity){
            finish();
        }
        overridePendingTransition(isDatabaseActivity ? R.animator.trans_right_in : R.animator.trans_left_in, isDatabaseActivity ? R.animator.trans_right_out : R.animator.trans_left_out);
    }

    private void checkChanges() {

        try {
            if (initialList == null && coordinatesList != null) {
                askForSaving();
            } else {
                if (initialList.equals(coordinatesList) && isSaved) {
                    startActivity(DatabaseActivity.class);
                } else {
                    askForSaving();
                }
            }
        } catch (NullPointerException e) {
            startActivity(DatabaseActivity.class);
        }
    }

    private void askForSaving() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(getString(R.string.ask_for_save_changes))
                .setPositiveButton(R.string.yes_btn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        saved();
                        startActivity(DatabaseActivity.class);
                    }
                })
                .setNegativeButton(R.string.no_btn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(DatabaseActivity.class);
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void clear() {
        final DrawerOnScreen drawer = (DrawerOnScreen) findViewById(R.id.canvas);
        assert drawer != null;
        drawer.clearAll();
        GimBus.getInstance().post(new OnCreatePictureEvent(coordinatesList));
    }

    private void clearLastShape() {
        final DrawerOnScreen drawer = (DrawerOnScreen) findViewById(R.id.canvas);
        assert drawer != null;
        drawer.clearLastShape();
        GimBus.getInstance().post(new OnCreatePictureEvent(coordinatesList));
    }

    private void saved() {
        drawDatabase.updateAllData(name, author, serializeListOfPoints(), id);
        Toast.makeText(this, R.string.saved_toast, Toast.LENGTH_SHORT).show();
        initialList = coordinatesList;
        isSaved = true;
    }

    private void share() {
        PointListHolder.getInstance().setCoordinatesList(coordinatesList);
        startActivity(BluetoothConnectionActivity.class);
    }

    private String serializeListOfPoints() {
        final JSONObject listInJSON = new JSONObject();
        try {
            listInJSON.put(Constants.KEY_LIST, coordinatesList);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return listInJSON.toString();
    }

    private Bundle getExtras() {
        final Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            isSaved = true;
            name = bundle.getString(Constants.KEY_NAME);
            author = bundle.getString(Constants.KEY_AUTHOR);
            id = bundle.getInt(Constants.KEY_POSITION);
        } else {
            isSaved = false;
            name = getIntent().getStringExtra(Constants.KEY_NAME);
            author = getIntent().getStringExtra(Constants.KEY_AUTHOR);
        }
        return bundle;
    }

    private FragmentParent[] createFragments() {

        final FragmentParent drawerFragment = new DrawerFragment();
        drawerFragment.setArguments(getExtras());

        final FragmentParent editorFragment = new EditorFragment();
        editorFragment.setArguments(getExtras());

        return new FragmentParent[]{drawerFragment, editorFragment};
    }

    private void setInitialList(List<Point> initialList) {
        if (isSaved) {
            this.initialList = initialList;
            isSaved = false;
        }
    }

    @Subscribe
    public void listOfPointsHolder(final OnCreatePictureEvent list) {
        this.coordinatesList = list.getCoordinatesList();
        setInitialList(coordinatesList);
    }
}