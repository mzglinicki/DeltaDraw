package com.project.mzglinicki96.deltaDraw.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.project.mzglinicki96.deltaDraw.R;
import com.project.mzglinicki96.deltaDraw.database.PictureModel;

import java.util.List;

/**
 * Created by mzglinicki.96 on 01.07.2016.
 */
public class PictureListRecycleAdapter extends RecyclerView.Adapter<PictureListViewHolder> implements PictureListTouchHelper.ItemTouchHelperAdapter {

    private final LayoutInflater inflater;
    private List<PictureModel> pictureModels;
    private ClickListener clickListener;

    public PictureListRecycleAdapter(final Context context, final List<PictureModel> pictureModels) {
        inflater = LayoutInflater.from(context);
        this.pictureModels = pictureModels;
    }

    @Override
    public PictureListViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        final View view = inflater.inflate(R.layout.data_picture_model, parent, false);
        return new PictureListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PictureListViewHolder holder, final int position) {

        final PictureModel model = pictureModels.get(position);
        holder.getPictureTitleField().setText(model.getName());
        holder.getAuthorField().setText(model.getAuthor());
        holder.getDateField().setText(model.getDate());
        setOnClickListener(holder, model);
    }

    private void setOnClickListener(final PictureListViewHolder holder, final PictureModel model) {


        holder.getRecord().setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View v) {
                if (clickListener != null) {
                    clickListener.onClick(holder.getAdapterPosition());
                }
            }
        });
        holder.getRecord().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View v) {
                if (clickListener != null) {
                    clickListener.onLongClick(v, model, holder);
                }
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return pictureModels.size();
    }

    @Override
    public void onItemDismiss(final RecyclerView.ViewHolder viewHolder) {
        final int position = viewHolder.getAdapterPosition();

        clickListener.onDeleteItem(pictureModels.get(position), position);
    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void animateDrawList(final List<PictureModel> models) {
        pictureModels = models;
        notifyDataSetChanged();
    }

    public interface ClickListener {
        void onClick(final int position);

        void onLongClick(final View view, final PictureModel model, final PictureListViewHolder holder);

        void onDeleteItem(final PictureModel model, final int position);
    }
}