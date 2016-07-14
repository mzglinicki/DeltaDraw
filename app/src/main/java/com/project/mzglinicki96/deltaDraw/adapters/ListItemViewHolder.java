package com.project.mzglinicki96.deltaDraw.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.project.mzglinicki96.deltaDraw.R;

/**
 * Created by mzglinicki.96 on 01.07.2016.
 */
public class ListItemViewHolder extends RecyclerView.ViewHolder {

    private final TextView pictureNameField;
    private final TextView authorField;
    private final TextView dateField;
    private final RelativeLayout record;

    public ListItemViewHolder(final View itemView) {
        super(itemView);
        this.pictureNameField = (TextView) itemView.findViewById(R.id.pictureTitleField);
        this.authorField = (TextView) itemView.findViewById(R.id.pictureAuthorField);
        this.dateField = (TextView) itemView.findViewById(R.id.pictureDateField);
        this.record = (RelativeLayout) itemView.findViewById(R.id.record);
    }

    public TextView getPictureTitleField() {
        return pictureNameField;
    }

    public TextView getAuthorField() {
        return authorField;
    }

    public TextView getDateField() {
        return dateField;
    }

    public RelativeLayout getRecord() {
        return record;
    }
}