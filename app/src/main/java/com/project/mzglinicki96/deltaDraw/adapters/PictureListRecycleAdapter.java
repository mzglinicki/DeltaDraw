package com.project.mzglinicki96.deltaDraw.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.mzglinicki96.deltaDraw.R;
import com.project.mzglinicki96.deltaDraw.database.PictureModel;

import java.util.List;

/**
 * Created by mzglinicki.96 on 01.07.2016.
 */
public class PictureListRecycleAdapter extends RecyclerView.Adapter<ListItemViewHolder> implements ListItemTouchHelper.ItemTouchHelperAdapter {

    private final LayoutInflater inflater;
    private List<PictureModel> pictureModels;
    private ClickListener clickListener;

    public PictureListRecycleAdapter(final Context context, final List<PictureModel> pictureModels) {
        inflater = LayoutInflater.from(context);
        this.pictureModels = pictureModels;
    }

    @Override
    public ListItemViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        final View view = inflater.inflate(R.layout.data_picture_model, parent, false);
        return new ListItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ListItemViewHolder holder, final int position) {

        final PictureModel model = pictureModels.get(position);
        holder.getPictureTitleField().setText(model.getName());
        holder.getAuthorField().setText(model.getAuthor());
        holder.getDateField().setText(model.getDate());
        holder.getRecord().setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (clickListener != null) {
                    clickListener.onClick(holder.getAdapterPosition());
                }
            }
        });
        holder.getRecord().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (clickListener != null) {
                    clickListener.onLongClick(v, model);
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

        void onLongClick(final View view, final PictureModel model);

        void onDeleteItem(final PictureModel model, final int position);
    }
}