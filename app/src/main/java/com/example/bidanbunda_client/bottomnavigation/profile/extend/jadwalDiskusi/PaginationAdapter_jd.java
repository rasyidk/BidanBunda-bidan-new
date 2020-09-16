package com.example.bidanbunda_client.bottomnavigation.profile.extend.jadwalDiskusi;

import android.app.ProgressDialog;
import android.content.Context;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.example.bidanbunda_client.api.retrofit.retrofitapi;
import com.example.bidanbunda_client.bottomnavigation.profile.extend.jadwalDiskusi.api.jdService;
import com.example.bidanbunda_client.bottomnavigation.profile.extend.jadwalDiskusi.extendJadwalDiskusi.addJadwalDiskusi;
import com.example.bidanbunda_client.bottomnavigation.profile.extend.jadwalDiskusi.extendJadwalDiskusi.api.addjdService;
import com.example.bidanbunda_client.bottomnavigation.profile.extend.jadwalDiskusi.models.data_jd;
import com.example.bidanbunda_client.bottomnavigation.profile.extend.jadwalDiskusi.utils.PaginationAdapterCallback_jd;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PaginationAdapter_jd extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // View Types
    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private static final int HERO = 2;

    private static final String BASE_URL_IMG = "https://image.tmdb.org/t/p/w200";

    private List<data_jd> movieResults;
    private Context context;

    private boolean isLoadingAdded = false;
    private boolean retryPageLoad = false;

    private PaginationAdapterCallback_jd mCallback;

    private String errorMsg;
    String cookie;

    PaginationAdapter_jd(Context context, String cookie) {
        this.cookie = cookie;
        this.context = context;
        this.mCallback = (PaginationAdapterCallback_jd) context;
        movieResults = new ArrayList<>();
    }

    public List<data_jd> getMovies() {
        return movieResults;
    }

    public void setMovies(List<data_jd> movieResults) {
        this.movieResults = movieResults;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ITEM:
                View viewItem = inflater.inflate(R.layout.row_jadwaldiskusi, parent, false);
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
        data_jd result = movieResults.get(position); // Movie

        switch (getItemViewType(position)) {

            case HERO:
                final HeroVH heroVh = (HeroVH) holder;


                break;

            case ITEM:
                final MovieVH movieVH = (MovieVH) holder;
                movieVH.tv_title.setText(result.getTitle());


                movieVH.bt_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        movieResults.remove(position);
//                        notifyDataSetChanged();
                        mCallback.showProgressBar();

                        jdService api = retrofitapi.getClient(v.getContext()).create(jdService.class);
                        Call<ResponseBody> call = api.deleteJadwal("/api/v1/jadwaldiskusi/"+ String.valueOf(position)+"", cookie);
                        call.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                                if (response.code() == 200){
                                    movieResults.remove(position);
                                    notifyDataSetChanged();

                                    mCallback.dismissProgressBar();
                                    Toast.makeText(v.getContext(),"berhasil Menghapus", Toast.LENGTH_SHORT).show();
                                }else {
                                    mCallback.dismissProgressBar();
                                    Toast.makeText(v.getContext(),"Gagal", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                mCallback.dismissProgressBar();
                                Toast.makeText(v.getContext(),"Jaringan error", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });


                long ts_st = result.getTimestamp_st();
                long ts_en = result.getTimestamp_en();

                String datetime_start;
                String datetime_end;

                if (ts_st == 0){
                    datetime_start = "selesai";
                }else {
                    Calendar cal = Calendar.getInstance(Locale.getDefault());
                    cal.setTimeInMillis(result.getTimestamp_st());
                    datetime_start = DateFormat.format("dd-MM-yyyy HH:mm", cal).toString();
                }
                if (ts_en == 0){
                    datetime_end = "selesai";
                }else {
                    Calendar cal = Calendar.getInstance(Locale.getDefault());
                    cal.setTimeInMillis(result.getTimestamp_st());
                    datetime_end = DateFormat.format("dd-MM-yyyy HH:mm", cal).toString();
                }

                String datetime = datetime_start + " - "+datetime_end;
                movieVH.tv_ts.setText(datetime);
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

    /*
        Helpers - bind Views
   _________________________________________________________________________________________________
    */




    public void add2(data_jd r) {
        movieResults.add(0, r);
        notifyItemInserted(0);
    }



    public void addAll2(List<data_jd> moveResults) {
        for (data_jd result : moveResults) {
            add2(result);
        }
    }


    public void add(data_jd r) {
        movieResults.add(r);
        notifyItemInserted(movieResults.size() - 1);
    }



    public void addAll(List<data_jd> moveResults) {
        for (data_jd result : moveResults) {
            add(result);
        }
    }

    public void remove(data_jd r) {
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
//        add(new data_jd());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = movieResults.size() - 1;
        data_jd result = getItem(position);

        if (result != null) {
            movieResults.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void removeLoadingFooterNEW() {
        isLoadingAdded = false;

        int position = movieResults.size() - 1;
        data_jd result = getItem(position);

        if (result != null) {
            movieResults.remove(position);
            notifyItemRemoved(position);
        }
    }


    public data_jd getItem(int position) {
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
        private TextView tv_title,tv_ts;
        private Button bt_delete;


        public MovieVH(View itemView) {
            super(itemView);

            tv_title = itemView.findViewById(R.id.jdrow_tv_title);
            tv_ts = itemView.findViewById(R.id.jdrow_tv_ts);
            bt_delete = itemView.findViewById(R.id.jdrow_bt_delete);

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
