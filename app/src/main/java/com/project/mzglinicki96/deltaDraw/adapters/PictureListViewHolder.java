package com.project.mzglinicki96.deltaDraw.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.project.mzglinicki96.deltaDraw.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by mzglinicki.96 on 01.07.2016.
 */
public class PictureListViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.pictureTitleField)
    protected TextView pictureTitleField;
    @Bind(R.id.pictureAuthorField)
    protected TextView pictureAuthorField;
    @Bind(R.id.pictureDateField)
    protected TextView pictureDateField;
    @Bind(R.id.pictureTitleEditField)
    protected EditText pictureTitleEditField;
    @Bind(R.id.pictureAuthorEditField)
    protected EditText pictureAuthorEditField;
    @Bind(R.id.acceptChangesImageBtn)
    protected ImageButton acceptChangesImageBtn;
    @Bind(R.id.cancelChangesImageBtn)
    protected ImageButton cancelChangesImageBtn;
    @Bind(R.id.record)
    protected RelativeLayout record;
    @Bind({R.id.pictureTitleEditField, R.id.pictureAuthorEditField, R.id.acceptChangesImageBtn, R.id.cancelChangesImageBtn, R.id.pictureTitleField, R.id.pictureAuthorField, R.id.pictureDateField})
    protected List<View> listViewItems;

    public PictureListViewHolder(final View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public TextView getDateField() {
        return pictureDateField;
    }

    public TextView getPictureTitleField() {
        return pictureTitleField;
    }

    public TextView getAuthorField() {
        return pictureAuthorField;
    }

    public RelativeLayout getRecord() {
        return record;
    }

    public EditText getPictureTitleEditField() {
        return pictureTitleEditField;
    }

    public EditText getPictureAuthorEditField() {
        return pictureAuthorEditField;
    }

    public ImageButton getAcceptChangesImageBtn() {
        return acceptChangesImageBtn;
    }

    public ImageButton getCancelChangesImageBtn() {
        return cancelChangesImageBtn;
    }

    public List<View> getListViewItems() {
        return listViewItems;
    }
}