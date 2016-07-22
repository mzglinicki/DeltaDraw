package com.project.mzglinicki96.deltaDraw.adapters;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

/**
 * Created by mzglinicki.96 on 02.07.2016.
 *
 */
public class PictureListTouchHelper extends ItemTouchHelper.Callback {

    private final ItemTouchHelperAdapter mAdapter;

    public PictureListTouchHelper(final ItemTouchHelperAdapter mAdapter) {
        this.mAdapter = mAdapter;
    }

    public interface ItemTouchHelperAdapter {
        void onItemDismiss(final RecyclerView.ViewHolder viewHolder);
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override
    public int getMovementFlags(final RecyclerView recyclerView, final RecyclerView.ViewHolder viewHolder) {
        final int dragFlags = 0;
        final int swipeFlags = ItemTouchHelper.END;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(final RecyclerView recyclerView, final RecyclerView.ViewHolder viewHolder, final RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(final RecyclerView.ViewHolder viewHolder, final int direction) {
        mAdapter.onItemDismiss(viewHolder);
    }
}