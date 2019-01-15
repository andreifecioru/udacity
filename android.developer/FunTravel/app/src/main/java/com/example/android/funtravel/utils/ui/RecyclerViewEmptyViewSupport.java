package com.example.android.funtravel.utils.ui;

import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;


/**
 * RecyclerView does not provide the "empty-view" functionality out-of-the-box
 * the way ListView does. This implementation tries to fill that gap.
 *
 * Original source: https://stackoverflow.com/a/30415582/387692
 */
public class RecyclerViewEmptyViewSupport extends RecyclerView {
    private View mEmptyView;

    /* Make the empty view visible whenever we detected that the adapter is empty. */
    private AdapterDataObserver emptyObserver = new AdapterDataObserver() {

        @Override
        public void onChanged() {
            Adapter<?> adapter =  getAdapter();
            if(adapter != null && mEmptyView != null) {
                if(adapter.getItemCount() == 0) {
                    mEmptyView.setVisibility(View.VISIBLE);
                    RecyclerViewEmptyViewSupport.this.setVisibility(View.GONE);
                } else {
                    mEmptyView.setVisibility(View.GONE);
                    RecyclerViewEmptyViewSupport.this.setVisibility(View.VISIBLE);
                }
            }
        }
    };

    /* Constructors */
    public RecyclerViewEmptyViewSupport(Context context) {
        super(context);
    }

    public RecyclerViewEmptyViewSupport(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RecyclerViewEmptyViewSupport(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);

        if(adapter != null) {
            adapter.registerAdapterDataObserver(emptyObserver);
        }

        emptyObserver.onChanged();
    }

    public void setEmptyView(View emptyView) {
        mEmptyView = emptyView;
    }
}
