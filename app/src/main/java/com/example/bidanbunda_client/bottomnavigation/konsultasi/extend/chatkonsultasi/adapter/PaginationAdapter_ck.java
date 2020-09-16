package com.example.bidanbunda_client.bottomnavigation.konsultasi.extend.chatkonsultasi.adapter;

import android.content.Context;

import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bidanbunda_client.R;
import com.example.bidanbunda_client.bottomnavigation.konsultasi.extend.chatkonsultasi.models.data_ck;
import com.example.bidanbunda_client.bottomnavigation.konsultasi.extend.chatkonsultasi.utils.PaginationAdapterCallback_ck;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class PaginationAdapter_ck extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // View Types
    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private static final int HERO = 2;

    private static final String BASE_URL_IMG = "https://image.tmdb.org/t/p/w200";

    private List<data_ck> movieResults;
    private Context context;

    private boolean isLoadingAdded = false;
    private boolean retryPageLoad = false;

    private PaginationAdapterCallback_ck mCallback;

    private String errorMsg;
    String user_id;

    public PaginationAdapter_ck(Context context, String user_id) {
        this.context = context;
        this.user_id= user_id;
        this.mCallback = (PaginationAdapterCallback_ck) context;
        movieResults = new ArrayList<>();
    }

    public List<data_ck> getMovies() {
        return movieResults;
    }

    public void setMovies(List<data_ck> movieResults) {
        this.movieResults = movieResults;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ITEM:
                View viewItem = inflater.inflate(R.layout.row_ck, parent, false);
                viewHolder = new MovieVH(viewItem);
                break;
            case LOADING:
                View viewLoading = inflater.inflate(R.layout.item_progress, parent, false);
                viewHolder = new LoadingVH(viewLoading);
                break;
            case HERO:
                View viewHero = inflater.inflate(R.layout.row_ck_me, parent, false);
                viewHolder = new HeroVH(viewHero);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        data_ck result = movieResults.get(position); // Movie

        switch (getItemViewType(position)) {

            //ME
            case HERO:
                final HeroVH heroVh = (HeroVH) holder;


                Calendar cal = Calendar.getInstance(Locale.JAPAN);
                cal.setTimeInMillis(movieResults.get(position).getTimestamp() * 1000L);
                String date = DateFormat.format("dd-MM-yyyy HH:mm", cal).toString();

                heroVh.tv_msghero.setText(result.getMessage());
                heroVh.tv_timestamphero.setText(date);
                break;

                //U
            case ITEM:
                final MovieVH movieVH = (MovieVH) holder;

                Calendar calx = Calendar.getInstance(Locale.JAPAN);
                calx.setTimeInMillis(movieResults.get(position).getTimestamp() * 1000L);
                String datex = DateFormat.format("dd-MM-yyyy HH:mm", calx).toString();

                movieVH.tv_message.setText(result.getMessage());
                movieVH.tv_timestamp.setText(datex);

                break;

            case LOADING:
                LoadingVH loadingVH = (LoadingVH) holder;

                if (retryPageLoad) {
                    loadingVH.mErrorLayout.setVisibility(View.VISIBLE);
                    loadingVH.mProgressBar.setVisibility(View.GONE);

                    loadingVH.mErrorTxt.setText(
                            errorMsg != null ?
                                    errorMsg :
                                    context.getString(R.string.error_msg_unknown));

                } else {
                    loadingVH.mErrorLayout.setVisibility(View.GONE);
                    loadingVH.mProgressBar.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    @Override
    public int getItemCount() {
        return movieResults == null ? 0 : movieResults.size();
    }

    @Override
    public int getItemViewType(int position) {
//        if (position == 0) {
//            return HERO;
//        } else {
//            return (position == movieResults.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
//        }

        if (movieResults.get(position).getSender_id().equals(user_id)){
            return (position == movieResults.size() - 1 && isLoadingAdded) ? LOADING : HERO;

        }else{
            return (position == movieResults.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
        }


    }

    /*
        Helpers - bind Views
   _________________________________________________________________________________________________
    */





    public void add2(data_ck r) {
        movieResults.add(0, r);
        notifyItemInserted(0);
    }



    public void addAll2(List<data_ck> moveResults) {
        for (data_ck result : moveResults) {
            add2(result);
        }
    }


    public void add(data_ck r) {
        movieResults.add(r);
        notifyItemInserted(movieResults.size() - 1);
    }



    public void addAll(List<data_ck> moveResults) {
        for (data_ck result : moveResults) {
            add(result);
        }
    }

    public void remove(data_ck r) {
        int position = movieResults.indexOf(r);
        if (position > -1) {
            movieResults.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        isLoadingAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }


    public void addLoadingFooter() {
        isLoadingAdded = true;
//        add(new data_ck());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = movieResults.size() - 1;
        data_ck result = getItem(position);

        if (result != null) {
            movieResults.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void removeLoadingFooterNEW() {
        isLoadingAdded = false;

        int position = movieResults.size() - 1;
        data_ck result = getItem(position);

        if (result != null) {
            movieResults.remove(position);
            notifyItemRemoved(position);
        }
    }


    public data_ck getItem(int position) {
        return movieResults.get(position);
    }


    public void showRetry(boolean show, @Nullable String errorMsg) {
        retryPageLoad = show;
        notifyItemChanged(movieResults.size() - 1);

        if (errorMsg != null) this.errorMsg = errorMsg;
    }



    protected class HeroVH extends RecyclerView.ViewHolder {
        private TextView tv_msghero, tv_timestamphero;

        public HeroVH(View itemView) {
            super(itemView);

            tv_msghero = itemView.findViewById(R.id.rck_me_tvmsg);
            tv_timestamphero = itemView.findViewById(R.id.rck_me_timestamp);
        }
    }


    protected class MovieVH extends RecyclerView.ViewHolder {
        private TextView tv_message,tv_timestamp;


        public MovieVH(View itemView) {
            super(itemView);

            tv_message = itemView.findViewById(R.id.rck_tvmsg);
            tv_timestamp =itemView.findViewById(R.id.rck_tv_timestamp);

        }
    }


    protected class LoadingVH extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ProgressBar mProgressBar;
        private ImageButton mRetryBtn;
        private TextView mErrorTxt;
        private LinearLayout mErrorLayout;

        public LoadingVH(View itemView) {
            super(itemView);

            mProgressBar = itemView.findViewById(R.id.loadmore_progress);
            mRetryBtn = itemView.findViewById(R.id.loadmore_retry);
            mErrorTxt = itemView.findViewById(R.id.loadmore_errortxt);
            mErrorLayout = itemView.findViewById(R.id.loadmore_errorlayout);

            mRetryBtn.setOnClickListener(this);
            mErrorLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.loadmore_retry:
                case R.id.loadmore_errorlayout:

                    showRetry(false, null);
                    mCallback.retryPageLoad();

                    break;
            }
        }
    }

}
