package com.example.bidanbunda_client.bottomnavigation.grupchat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bidanbunda_client.R;
import com.example.bidanbunda_client.bottomnavigation.grupchat.models.data_gc;
import com.example.bidanbunda_client.bottomnavigation.grupchat.utils.PaginationAdapterCallback_gc;
import com.example.bidanbunda_client.bottomnavigation.videomateri.video.video;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class PaginationAdapter_gc extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // View Types
    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private static final int HERO = 2;

    private static final String BASE_URL_IMG = "https://image.tmdb.org/t/p/w200";

    private List<data_gc> movieResults;
    private Context context;

    private boolean isLoadingAdded = false;
    private boolean retryPageLoad = false;

    private PaginationAdapterCallback_gc mCallback;

    private String errorMsg;
    String user_id;

    PaginationAdapter_gc(Context context, PaginationAdapterCallback_gc callback, String user_id) {
        super();
        this.context = context;
        this.mCallback = callback;
        this.user_id = user_id;
        movieResults = new ArrayList<>();
    }



    public List<data_gc> getMovies() {
        return movieResults;
    }

    public void setMovies(List<data_gc> movieResults) {
        this.movieResults = movieResults;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ITEM:
                View viewItem = inflater.inflate(R.layout.row_gc, parent, false);
                viewHolder = new MovieVH(viewItem);
                break;
            case LOADING:
                View viewLoading = inflater.inflate(R.layout.item_progress, parent, false);
                viewHolder = new LoadingVH(viewLoading);
                break;
            case HERO:
                View viewHero = inflater.inflate(R.layout.row_gc_me, parent, false);
                viewHolder = new HeroVH(viewHero);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        data_gc result = movieResults.get(position); // Movie

        switch (getItemViewType(position)) {

            case HERO:
                final HeroVH heroVh = (HeroVH) holder;
                heroVh.tv_msghero.setText(result.getMessage());

                Calendar cal = Calendar.getInstance(Locale.JAPAN);
                cal.setTimeInMillis(movieResults.get(position).getTimestamp() * 1000L);
                String date = DateFormat.format("dd-MM-yyyy HH:mm", cal).toString();
                heroVh.tv_timestamphero.setText(date);




                break;

            case ITEM:
                final MovieVH movieVH = (MovieVH) holder;

                movieVH.tv_name.setText(result.getSender_name());
                movieVH.tv_message.setText(result.getMessage());


                Calendar calx = Calendar.getInstance(Locale.JAPAN);
                calx.setTimeInMillis(movieResults.get(position).getTimestamp() * 1000L);
                String datex = DateFormat.format("dd-MM-yyyy HH:mm", calx).toString();
                movieVH.tv_timestamp.setText(datex);

                if (position%4 == 0){
                    movieVH.tv_name.setTextColor(Color.parseColor("#4398B7"));
                }
                if (position%4 == 1){
                    movieVH.tv_name.setTextColor(Color.parseColor("#725948"));
                }
                if (position%4 == 2){
                    movieVH.tv_name.setTextColor(Color.parseColor("#F68C4A"));
                }
                if (position%4 == 3){
                    movieVH.tv_name.setTextColor(Color.parseColor("#EF4D4D"));
                }

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





    public void add(data_gc r) {
        movieResults.add(r);
        notifyItemInserted(movieResults.size() - 1);
    }

    public void addAll(List<data_gc> moveResults) {
        for (data_gc result : moveResults) {
            add(result);
        }
    }

    public void add2(data_gc r) {
        movieResults.add(0, r);
        notifyItemInserted(0);
    }



    public void addAll2(List<data_gc> moveResults) {
        for (data_gc result : moveResults) {
            add2(result);
        }
    }

    public void remove(data_gc r) {
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
        //add(new data_gc());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = movieResults.size() - 1;
        data_gc result = getItem(position);

        if (result != null) {
            movieResults.remove(position);
            notifyItemRemoved(position);
        }
    }

    public data_gc getItem(int position) {
        return movieResults.get(position);
    }


    public void showRetry(boolean show, @Nullable String errorMsg) {
        retryPageLoad = show;
        notifyItemChanged(movieResults.size() - 1);

        if (errorMsg != null) this.errorMsg = errorMsg;
    }



    protected class HeroVH extends RecyclerView.ViewHolder {
        private TextView tv_msghero,tv_timestamphero;

        public HeroVH(View itemView) {
            super(itemView);

            tv_msghero = itemView.findViewById(R.id.row_me_msg);
            tv_timestamphero = itemView.findViewById(R.id.row_me_timestamp);
        }
    }


    protected class MovieVH extends RecyclerView.ViewHolder {
        private TextView tv_name;
        private TextView tv_message,tv_timestamp;

        public MovieVH(View itemView) {
            super(itemView);

            tv_name = itemView.findViewById(R.id.rowgc_tv_nama);
            tv_message = itemView.findViewById(R.id.rowgc_tv_msg);
            tv_timestamp = itemView.findViewById(R.id.rowgc_tv_timestamp);

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
