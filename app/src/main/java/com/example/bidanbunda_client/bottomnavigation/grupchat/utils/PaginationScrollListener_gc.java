package com.example.bidanbunda_client.bottomnavigation.grupchat.utils;



import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Pagination
 * Created by Suleiman19 on 10/15/16.
 * Copyright (c) 2016. Suleiman Ali Shakir. All rights reserved.
 */
public abstract class PaginationScrollListener_gc extends RecyclerView.OnScrollListener {

    private LinearLayoutManager layoutManager;

    /**
     * Supporting only LinearLayoutManager for now.
     *
     * @param layoutManager
     */
    protected PaginationScrollListener_gc(LinearLayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        int visibleItemCount = layoutManager.getChildCount();
        int totalItemCount = layoutManager.getItemCount();
        int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

        if (!isLoading() && !isLastPage()) {
            if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                    && firstVisibleItemPosition >= 0) {
                loadMoreItems();
            }
        }

    }

    protected abstract void loadMoreItems();

    public abstract int getTotalPageCount();

    public abstract boolean isLastPage();

    public abstract boolean isLoading();

}
