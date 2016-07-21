package com.project.mzglinicki96.deltaDraw.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.project.mzglinicki96.deltaDraw.R;
import com.project.mzglinicki96.deltaDraw.adapters.ListItemTouchHelper;
import com.project.mzglinicki96.deltaDraw.adapters.PictureListRecycleAdapter;
import com.project.mzglinicki96.deltaDraw.database.DatabaseHelper;
import com.project.mzglinicki96.deltaDraw.database.PictureModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by mzglinicki.96 on 27.03.2016.
 */
public class DatabaseActivity extends AppCompatActivity implements PictureListRecycleAdapter.ClickListener, SearchView.OnQueryTextListener {

    @Inject
    DatabaseHelper databaseHelper;

    private PictureListRecycleAdapter adapter;
    private RecyclerView recyclerView;
    private List<PictureModel> pictureModels;
    private final List<PictureModel> pictureToRemove = new ArrayList<>();

    private EditText pictureTitleEditField;
    private EditText pictureAuthorEditField;
    private ImageButton acceptChangesImageBtn;
    private ImageButton cancelChangesImageBtn;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MyApplication)getApplication()).getComponent().inject(this);
        createNewScreen();
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        super.onCreateOptionsMenu(menu);
        final MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.lib_menu, menu);

        final MenuItem item = menu.findItem(R.id.lib_menu_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {

        switch (item.getItemId()) {
            case R.id.lib_menu_settings:
                startActivity(MainSettingsActivity.class, null);
                return true;
            case R.id.lib_menu_credits:
                startActivity(CreditsActivity.class, null);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(int position) {
        startActivity(DrawCreatingActivity.class, databaseHelper.getDataFromRow(position));
    }

    @Override
    public void onLongClick(final View view, final PictureModel model) {

        final List<View> normalViewItems = getNormalViewItems(view);
        final List<View> editViewItems = getEditViewItems(view);

        changeVisibility(normalViewItems);
        changeVisibility(editViewItems);

        acceptChangesImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseHelper.updateAllData(pictureTitleEditField.getText().toString(), pictureAuthorEditField.getText().toString(), model.getPoints(), model.getId());
                model.setAuthor(pictureAuthorEditField.getText().toString());
                model.setName(pictureTitleEditField.getText().toString());
                adapter.notifyDataSetChanged();
                changeVisibility(editViewItems);
                changeVisibility(normalViewItems);
            }
        });

        cancelChangesImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeVisibility(editViewItems);
                changeVisibility(normalViewItems);
            }
        });
    }

    @Override
    public void onDeleteItem(final PictureModel model, final int position) {

        if (!pictureToRemove.isEmpty()) {
            pictureToRemove.remove(pictureToRemove.get(0));
        }
        pictureToRemove.add(model);

        final Snackbar snackbar = Snackbar
                .make(recyclerView, R.string.deleted_snackbar, Snackbar.LENGTH_LONG)
                .setAction(R.string.undo, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onUndoClick(model, position);
                    }
                })
                .setCallback(new Snackbar.Callback() {
                    @Override
                    public void onDismissed(Snackbar snackbar, int event) {
                        super.onDismissed(snackbar, event);
                        if (!pictureToRemove.isEmpty()) {
                            databaseHelper.deleteRecord(pictureToRemove.get(0).getId());
                        }
                        pictureToRemove.remove(model);
                    }
                });
        snackbar.show();

        pictureModels.remove(position);
        adapter.notifyItemRemoved(position);
        pictureToRemove.add(model);
        checkIfAdapterIsEmpty();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        adapter.animateDrawList(filter(pictureModels, query));
        recyclerView.scrollToPosition(0);
        return true;
    }

    @Override
    public void onBackPressed() {
        closeApplication();
    }

    private void changeVisibility(final List<View> itemsToChange) {
        for (View item : itemsToChange) {
            item.setVisibility(item.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
        }
    }

    private void onUndoClick(final PictureModel model, final int position) {
        pictureModels.add(position, model);
        databaseHelper.insertData(model.getName(), model.getAuthor(), model.getPoints());
        adapter.notifyItemInserted(position);
        recyclerView.scrollToPosition(position);
        pictureToRemove.remove(model);
        checkIfAdapterIsEmpty();
    }

    private List<View> getNormalViewItems(final View view) {
        final TextView pictureTitleField = (TextView) view.findViewById(R.id.pictureTitleField);
        final TextView pictureAuthorField = (TextView) view.findViewById(R.id.pictureAuthorField);
        final TextView pictureDateField = (TextView) view.findViewById(R.id.pictureDateField);

        final List<View> normalViewItems = new ArrayList<>();
        normalViewItems.add(pictureTitleField);
        normalViewItems.add(pictureAuthorField);
        normalViewItems.add(pictureDateField);
        return normalViewItems;
    }

    private List<View> getEditViewItems(final View view) {
        pictureTitleEditField = (EditText) view.findViewById(R.id.pictureTitleEditField);
        pictureAuthorEditField = (EditText) view.findViewById(R.id.pictureAuthorEditField);
        acceptChangesImageBtn = (ImageButton) view.findViewById(R.id.acceptChangesImageBtn);
        cancelChangesImageBtn = (ImageButton) view.findViewById(R.id.cancelChangesImageBtn);

        final List<View> editViewItems = new ArrayList<>();
        editViewItems.add(pictureTitleEditField);
        editViewItems.add(pictureAuthorEditField);
        editViewItems.add(acceptChangesImageBtn);
        editViewItems.add(cancelChangesImageBtn);
        return editViewItems;
    }

    private void createNewScreen() {
        setContentView(R.layout.activity_database);

        pictureModels = databaseHelper.getPicturesData();
        adapter = new PictureListRecycleAdapter(this, pictureModels);
        adapter.setClickListener(this);

        checkIfAdapterIsEmpty();
        setupRecyclerView();
        createItemTouchHelper();
        setupFloatingButton();
    }

    private void checkIfAdapterIsEmpty() {
        final TextView emptyListMessage = (TextView) findViewById(R.id.emptyListMessage);
        assert emptyListMessage != null;
        emptyListMessage.setVisibility(adapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
    }

    private void setupRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.pictureListRecycleView);
        assert recyclerView != null;
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void startActivity(final Class newActivity, @Nullable final Bundle bundle) {

        final Intent intent = new Intent(DatabaseActivity.this, newActivity);

        if (newActivity.equals(DrawCreatingActivity.class) && bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
        overridePendingTransition(R.animator.trans_left_in, R.animator.trans_left_out);
        finish();
    }

    private void setupFloatingButton() {

        final FloatingActionButton floatingButton = (FloatingActionButton) findViewById(R.id.floating_button);

        assert floatingButton != null;
        floatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFloatingBtnAlert(getString(R.string.start_draw_title), DatabaseActivity.this);
            }
        });
    }

    private List<PictureModel> filter(final List<PictureModel> models, String query) {
        query = query.toLowerCase();

        final List<PictureModel> filteredModelList = new ArrayList<>();
        for (PictureModel model : models) {
            final String pictureName = model.getName().toLowerCase();
            final String pictureAuthor = model.getAuthor().toLowerCase();
            final String createDate = model.getDate().toLowerCase();
            if (pictureName.contains(query) || pictureAuthor.contains(query) || createDate.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

    private void createItemTouchHelper() {
        final ListItemTouchHelper callback = new ListItemTouchHelper(adapter, recyclerView);
        final ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void closeApplication() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(getString(R.string.close_app_dialog))
                .setPositiveButton(R.string.yes_btn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        System.exit(0);
                    }
                })
                .setNegativeButton(R.string.no_btn, null);
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void showFloatingBtnAlert(final String message, final Context context) {

        final View view = LayoutInflater.from(context).inflate(R.layout.new_picture_dialog, null);

        final AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setMessage(message)
                .setView(view)
                .setPositiveButton(R.string.start_draw_btn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setStartDrawClickListener(view, context);
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .setCancelable(false);
        final AlertDialog alert = builder.create();
        alert.show();
    }

    private void setStartDrawClickListener(final View view, final Context context) {

        final AppCompatEditText pictureName = (AppCompatEditText) view.findViewById(R.id.textPictureName);
        final AppCompatEditText pictureAuthor = (AppCompatEditText) view.findViewById(R.id.textAuthor);
        final String name = pictureName.getText().toString();
        final String author = pictureAuthor.getText().toString();

        if (!name.isEmpty() && !author.isEmpty()) {
            databaseHelper.insertData(name, author, null);
            startActivity(DrawCreatingActivity.class, databaseHelper.getLastSavedRow());
        }
    }
}