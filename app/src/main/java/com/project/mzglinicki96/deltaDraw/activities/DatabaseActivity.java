package com.project.mzglinicki96.deltaDraw.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.project.mzglinicki96.deltaDraw.DialogActionListener;
import com.project.mzglinicki96.deltaDraw.DialogWindow;
import com.project.mzglinicki96.deltaDraw.R;
import com.project.mzglinicki96.deltaDraw.adapters.PictureListRecycleAdapter;
import com.project.mzglinicki96.deltaDraw.adapters.PictureListTouchHelper;
import com.project.mzglinicki96.deltaDraw.adapters.PictureListViewHolder;
import com.project.mzglinicki96.deltaDraw.database.DatabaseHelper;
import com.project.mzglinicki96.deltaDraw.database.PictureModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.view.View.GONE;
import static android.view.View.OnClickListener;
import static android.view.View.VISIBLE;
import static android.widget.Toast.LENGTH_SHORT;

/**
 * Created by mzglinicki.96 on 27.03.2016.
 */
public class DatabaseActivity extends AppCompatActivity implements PictureListRecycleAdapter.ClickListener, SearchView.OnQueryTextListener {

    @Inject
    protected DatabaseHelper databaseHelper;
    @Bind(R.id.recycleView)
    protected RecyclerView recyclerView;
    @Bind(R.id.emptyListMessage)
    protected TextView emptyListMessage;

    private boolean editing = false;
    private PictureListRecycleAdapter adapter;
    private List<PictureModel> pictureModels;
    private final List<PictureModel> pictureToRemove = new ArrayList<>();

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((MyApplication) getApplication()).getComponent().inject(this);
        setContentView(R.layout.activity_database);
        ButterKnife.bind(this);

        createNewScreen();
    }

    private void createNewScreen() {

        pictureModels = databaseHelper.getPicturesData();
        adapter = new PictureListRecycleAdapter(this, pictureModels);
        adapter.setClickListener(this);
        handleEmptyAdapter();
        setupRecyclerView();
        new ItemTouchHelper(new PictureListTouchHelper(adapter)).attachToRecyclerView(recyclerView);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.lib_menu, menu);

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
    public void onLongClick(final View view, final PictureModel model, final PictureListViewHolder holder) {

        if (editing) {
            Toast.makeText(this, R.string.acceptPreviousChanges, LENGTH_SHORT).show();
        } else {
            onRecordEdit(true, holder.getListViewItems());
            holder.getPictureTitleEditField().setText(holder.getPictureTitleField().getText().toString());
            holder.getPictureAuthorEditField().setText(holder.getAuthorField().getText().toString());
            onAcceptChangesClick(model, holder);
            onCancelClick(holder);
        }
    }

    @Override
    public void onDeleteItem(final PictureModel model, final int position) {

        Snackbar.make(recyclerView, R.string.deleted_snackbar, Snackbar.LENGTH_LONG)
                .setAction(R.string.undo, new OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        onUndoClick(model, position);
                    }
                })
                .addCallback(new Snackbar.Callback() {
                    @Override
                    public void onDismissed(final Snackbar snackbar, final int event) {
                        if (!pictureToRemove.isEmpty()) {
                            databaseHelper.deleteRecord(pictureToRemove.get(0).getId());
                        }
                        pictureToRemove.remove(model);
                    }
                }).show();

        pictureModels.remove(position);
        adapter.notifyItemRemoved(position);
        pictureToRemove.add(model);
        handleEmptyAdapter();
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
        new DialogWindow(this, R.string.close_app_dialog, R.string.yes_btn, R.string.no_btn, null, true, new DialogActionListener() {
            @Override
            public void onPositiveBtnClick() {
                System.exit(0);
            }
        });
    }

    @OnClick(R.id.floatingButton)
    public void onFABClick() {

        final View view = LayoutInflater.from(this).inflate(R.layout.new_picture_dialog, null);
        new DialogWindow(this, R.string.start_draw_title, R.string.start_draw_btn, R.string.cancel, view, false, new DialogActionListener() {
            @Override
            public void onPositiveBtnClick() {
                startDraw(view);
            }
        });
    }

    public boolean onAcceptChangesClick(final PictureModel model, final PictureListViewHolder holder) {

        holder.getAcceptChangesImageBtn().setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View v) {
                final String newTitle = holder.getPictureTitleEditField().getText().toString();
                final String newAuthor = holder.getPictureAuthorEditField().getText().toString();
                if (newTitle.isEmpty() || newAuthor.isEmpty()) {
                    return;
                }
                databaseHelper.updateAllData(newTitle, newAuthor, model.getPoints(), model.getId());
                model.setName(newTitle);
                model.setAuthor(newAuthor);
                adapter.notifyDataSetChanged();
                onRecordEdit(false, holder.getListViewItems());
            }
        });
        return true;
    }

    public boolean onCancelClick(final PictureListViewHolder holder) {

        holder.getCancelChangesImageBtn().setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View v) {
                onRecordEdit(false, holder.getListViewItems());
            }
        });
        return true;
    }

    private void onRecordEdit(final boolean isRecordEditing, final List<View> databaseRecords) {
        editing = isRecordEditing;

        for (final View item : databaseRecords) {
            item.setVisibility(item.getVisibility() == VISIBLE ? GONE : VISIBLE);
        }
    }

    private void onUndoClick(final PictureModel model, final int position) {
        pictureModels.add(position, model);
        adapter.notifyItemInserted(position);
        recyclerView.scrollToPosition(position);
        pictureToRemove.remove(model);
        handleEmptyAdapter();
    }

    private void handleEmptyAdapter() {
        emptyListMessage.setVisibility(adapter.getItemCount() == 0 ? VISIBLE : GONE);
    }

    private void setupRecyclerView() {
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

    private List<PictureModel> filter(final List<PictureModel> models, String query) {
        query = query.toLowerCase();

        final List<PictureModel> filteredModelList = new ArrayList<>();
        for (final PictureModel model : models) {
            final String pictureName = model.getName().toLowerCase();
            final String pictureAuthor = model.getAuthor().toLowerCase();
            final String createDate = model.getDate().toLowerCase();
            if (pictureName.contains(query) || pictureAuthor.contains(query) || createDate.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

    private void startDraw(final View view) {

        final AppCompatEditText pictureTitle = (AppCompatEditText) view.findViewById(R.id.textPictureTitle);
        final AppCompatEditText pictureAuthor = (AppCompatEditText) view.findViewById(R.id.textAuthor);
        final String name = pictureTitle.getText().toString();
        final String author = pictureAuthor.getText().toString();

        if (!name.isEmpty() && !author.isEmpty()) {
            databaseHelper.insertData(name, author, null);
            startActivity(DrawCreatingActivity.class, databaseHelper.getLastSavedRow());
        } else {
            Toast.makeText(this, R.string.uncompletedData, LENGTH_SHORT).show();
        }
    }
}