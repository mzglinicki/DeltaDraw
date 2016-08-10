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
    TextView pictureTitleField;
    @Bind(R.id.pictureAuthorField)
    TextView pictureAuthorField;
    @Bind(R.id.pictureDateField)
    TextView pictureDateField;
    @Bind(R.id.pictureTitleEditField)
    EditText pictureTitleEditField;
    @Bind(R.id.pictureAuthorEditField)
    EditText pictureAuthorEditField;
    @Bind(R.id.acceptChangesImageBtn)
    ImageButton acceptChangesImageBtn;
    @Bind(R.id.cancelChangesImageBtn)
    ImageButton cancelChangesImageBtn;
    @Bind(R.id.record)
    RelativeLayout record;
    @Bind({R.id.pictureTitleEditField, R.id.pictureAuthorEditField, R.id.acceptChangesImageBtn, R.id.cancelChangesImageBtn, R.id.pictureTitleField, R.id.pictureAuthorField, R.id.pictureDateField})
    List<View> listViewItems;

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