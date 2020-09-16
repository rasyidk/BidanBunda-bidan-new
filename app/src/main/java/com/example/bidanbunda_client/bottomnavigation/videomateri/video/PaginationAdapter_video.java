package com.example.bidanbunda_client.bottomnavigation.videomateri.video;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bidanbunda_client.R;
import com.example.bidanbunda_client.bottomnavigation.videomateri.video.models.data_video;
import com.example.bidanbunda_client.bottomnavigation.videomateri.video.playvideo.playvideo;
import com.example.bidanbunda_client.bottomnavigation.videomateri.video.utils.PaginationAdapterCallback_video;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Suleiman on 19/10/16.
 */

public class PaginationAdapter_video extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // View Types
    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private static final int HERO = 2;

    private static final String BASE_URL_IMG = "https://image.tmdb.org/t/p/w200";

    private List<data_video> movieResults;
    private Context context;

    private boolean isLoadingAdded = false;
    private boolean retryPageLoad = false;

    private PaginationAdapterCallback_video mCallback;

    private String errorMsg;

    PaginationAdapter_video(Context context) {
        this.context = context;
        this.mCallback = (PaginationAdapterCallback_video) context;
        movieResults = new ArrayList<>();
    }

    public List<data_video> getMovies() {
        return movieResults;
    }

    public void setMovies(List<data_video> movieResults) {
        this.movieResults = movieResults;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ITEM:
                View viewItem = inflater.inflate(R.layout.row_video, parent, false);
                viewHolder = new MovieVH(viewItem);
                break;
            case LOADING:
                View viewLoading = inflater.inflate(R.layout.item_progress, parent, false);
                viewHolder = new LoadingVH(viewLoading);
                break;
            case HERO:
                View viewHero = inflater.inflate(R.layout.item_hero, parent, false);
                viewHolder = new HeroVH(viewHero);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        data_video result = movieResults.get(position); // Movie

        switch (getItemViewType(position)) {

            case HERO:
                final HeroVH heroVh = (HeroVH) holder;




            case ITEM:
                final MovieVH movieVH = (MovieVH) holder;

                  movieVH.tvTitle.setText(result.getTitle());
                  movieVH.tvDuration.setText("durasi :" + result.getVideo_duration_str() +"");

//                if (position%4 == 0){
//                    movieVH.relativeLayout.setBackgroundColor(Color.parseColor("#A4BF57"));
//                }
//                if (position%4 == 1){
//                    movieVH.relativeLayout.setBackgroundColor(Color.parseColor("#725948"));
//                }
//                if (position%4 == 2){
//                    movieVH.relativeLayout.setBackgroundColor(Color.parseColor("#F68C4A"));
//                }
//                if (position%4 == 3){
//                    movieVH.relativeLayout.setBackgroundColor(Color.parseColor("#EF4D4D"));
//                }

                Glide.with(movieVH.mPosterImg.getContext()).load(result.getThumbnail_url()).into(movieVH.mPosterImg);

               movieVH.relativeLayout.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       Intent intent = new Intent(v.getContext(), playvideo.class);
                       intent.putExtra("url", movieResults.get(position).getYt_video_id());
                       v.getContext().startActivity(intent);
                   }
               });

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

        return (position == movieResults.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }





    public void add(data_video r) {
        movieResults.add(r);
        notifyItemInserted(movieResults.size() - 1);
    }

    public void addAll(List<data_video> moveResults) {
        for (data_video result : moveResults) {
            add(result);
        }
    }

    public void remove(data_video r) {
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
        add(new data_video());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = movieResults.size() - 1;
        data_video result = getItem(position);

        if (result != null) {
            movieResults.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void removeLoadingFooterNEW() {
        isLoadingAdded = false;

        int position = movieResults.size() - 1;
        data_video result = getItem(position);

        if (result != null) {
            movieResults.remove(position);
            notifyItemRemoved(position);
        }
    }


    public data_video getItem(int position) {
        return movieResults.get(position);
    }


    public void showRetry(boolean show, @Nullable String errorMsg) {
        retryPageLoad = show;
        notifyItemChanged(movieResults.size() - 1);

        if (errorMsg != null) this.errorMsg = errorMsg;
    }



    protected class HeroVH extends RecyclerView.ViewHolder {
        private TextView mMovieTitle;
        private TextView mMovieDesc;
        private TextView mYear; // displays "year | language"
        private ImageView mPosterImg;

        public HeroVH(View itemView) {
            super(itemView);

            mMovieTitle = itemView.findViewById(R.id.movie_title);
            mMovieDesc = itemView.findViewById(R.id.movie_desc);
            mYear = itemView.findViewById(R.id.movie_year);
            mPosterImg = itemView.findViewById(R.id.movie_poster);
        }
    }


    protected class MovieVH extends RecyclerView.ViewHolder {
        private TextView tvTitle;
        private TextView tvDuration;
        private ImageView mPosterImg;
        private ProgressBar mProgress;
        RelativeLayout relativeLayout;

        public MovieVH(View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.vdo_tv_title);
            tvDuration = itemView.findViewById(R.id.vdo_tv_duration);
            relativeLayout = itemView.findViewById(R.id.vdo_relative);
            mPosterImg = itemView.findViewById(R.id.vdo_img_main);
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
