package com.example.bidanbunda_client.bottomnavigation.videomateri;

import android.content.Context;
import android.content.Intent;
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
import com.example.bidanbunda_client.api.utils.PaginationAdapterCallback;

import java.util.ArrayList;
import java.util.List;

import com.example.bidanbunda_client.bottomnavigation.videomateri.models.data_vm;
import com.example.bidanbunda_client.bottomnavigation.videomateri.video.video;

public class PaginationAdapter_vm extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // View Types
    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private static final int HERO = 2;

    private static final String BASE_URL_IMG = "https://image.tmdb.org/t/p/w200";

    private List<data_vm> movieResults;
    private Context context;

    private boolean isLoadingAdded = false;
    private boolean retryPageLoad = false;

    private PaginationAdapterCallback mCallback;

    private String errorMsg;

    PaginationAdapter_vm(Context context, PaginationAdapterCallback callback) {
        super();
        this.context = context;
        this.mCallback = callback;
        movieResults = new ArrayList<>();
    }



    public List<data_vm> getMovies() {
        return movieResults;
    }

    public void setMovies(List<data_vm> movieResults) {
        this.movieResults = movieResults;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ITEM:
                View viewItem = inflater.inflate(R.layout.row_videomateri, parent, false);
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
        data_vm result = movieResults.get(position); // Movie

        switch (getItemViewType(position)) {

            case HERO:
                final HeroVH heroVh = (HeroVH) holder;

            case ITEM:
                final MovieVH movieVH = (MovieVH) holder;

                movieVH.tvWeek.setText("Minggu Ke- " + result.getWeek() +"");
                movieVH.tvContent.setText(  "Oleh : "+ result.getAuthorname() +"");
                movieVH.tvDurationTotal.setText("Total :" +  result.getVideostotal()  +" Duration :"+ result.getVideosduration()+"");

                Glide.with(movieVH.mPosterImg.getContext()).load(result.getThumbnail_url()).into(movieVH.mPosterImg);
                // load movie thumbnail

                movieVH.relativeLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(v.getContext(), video.class);
                        intent.putExtra("week", movieResults.get(position).getWeek());
                        intent.putExtra("content", movieResults.get(position).getContent());
                        v.getContext().startActivity(intent);
  //                      Toast.makeText(v.getContext(), movieResults.get(position).getWeek(), Toast.LENGTH_SHORT).show();
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





    public void add(data_vm r) {
        movieResults.add(r);
        notifyItemInserted(movieResults.size() - 1);
    }

    public void addAll(List<data_vm> moveResults) {
        for (data_vm result : moveResults) {
            add(result);
        }
    }

    public void remove(data_vm r) {
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
        add(new data_vm());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = movieResults.size() - 1;
        data_vm result = getItem(position);

        if (result != null) {
            movieResults.remove(position);
            notifyItemRemoved(position);
        }
    }

    public data_vm getItem(int position) {
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
        private TextView tvWeek;
        private TextView tvContent;
        private  TextView tvDurationTotal;
        private TextView mYear; // displays "year | language"
        private ImageView mPosterImg;
        private ProgressBar mProgress;
        RelativeLayout relativeLayout;

        public MovieVH(View itemView) {
            super(itemView);

            tvWeek = itemView.findViewById(R.id.vm_tv_week);
            tvContent = itemView.findViewById(R.id.vm_tv_content);
            relativeLayout = itemView.findViewById(R.id.vm_relative);
            mPosterImg = itemView.findViewById(R.id.vm_img_main);
            tvDurationTotal = itemView.findViewById(R.id.vm_tv_durationtotal);
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
