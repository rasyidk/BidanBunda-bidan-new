package com.example.bidanbunda_client.bottomnavigation.profile.extend.catatanKhusus.extendAddDataPatient.showPatient;

import android.content.Context;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bidanbunda_client.R;
import com.example.bidanbunda_client.bottomnavigation.profile.extend.catatanKhusus.extendAddDataPatient.addNotePatient.addNotePatient;
import com.example.bidanbunda_client.bottomnavigation.profile.extend.catatanKhusus.extendAddDataPatient.showPatient.models.data_sp;
import com.example.bidanbunda_client.bottomnavigation.profile.extend.catatanKhusus.extendAddDataPatient.showPatient.utils.PaginationAdapterCallback_sp;

import java.util.ArrayList;
import java.util.List;


public class PaginationAdapter_sp extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // View Types
    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private static final int HERO = 2;

    private static final String BASE_URL_IMG = "https://image.tmdb.org/t/p/w200";

    private List<data_sp> movieResults;
    private Context context;

    private boolean isLoadingAdded = false;
    private boolean retryPageLoad = false;

    private PaginationAdapterCallback_sp mCallback;

    private String errorMsg;

    PaginationAdapter_sp(Context context) {
        this.context = context;
        this.mCallback = (PaginationAdapterCallback_sp) context;
        movieResults = new ArrayList<>();
    }

    public List<data_sp> getMovies() {
        return movieResults;
    }

    public void setMovies(List<data_sp> movieResults) {
        this.movieResults = movieResults;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ITEM:
                View viewItem = inflater.inflate(R.layout.row_showpatient, parent, false);
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
        data_sp result = movieResults.get(position); // Movie

        switch (getItemViewType(position)) {

            case HERO:
                final HeroVH heroVh = (HeroVH) holder;


                break;

            case ITEM:
                final MovieVH movieVH = (MovieVH) holder;

                movieVH.tv_name.setText(result.getName());

                movieVH.linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i =  new Intent(v.getContext(), addNotePatient.class);

                        String targetid = result.getId();
                        String name = result.getName();

                        i.putExtra("type","add");
                        i.putExtra("targetid",targetid);
                        i.putExtra("name",name);
                        v.getContext().startActivity(i);
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






    public void add2(data_sp r) {
        movieResults.add(0, r);
        notifyItemInserted(0);
    }



    public void addAll2(List<data_sp> moveResults) {
        for (data_sp result : moveResults) {
            add2(result);
        }
    }


    public void add(data_sp r) {
        movieResults.add(r);
        notifyItemInserted(movieResults.size() - 1);
    }



    public void addAll(List<data_sp> moveResults) {
        for (data_sp result : moveResults) {
            add(result);
        }
    }

    public void remove(data_sp r) {
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
//        add(new data_sp());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = movieResults.size() - 1;
        data_sp result = getItem(position);

        if (result != null) {
            movieResults.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void removeLoadingFooterNEW() {
        isLoadingAdded = false;

        int position = movieResults.size() - 1;
        data_sp result = getItem(position);

        if (result != null) {
            movieResults.remove(position);
            notifyItemRemoved(position);
        }
    }


    public data_sp getItem(int position) {
        return movieResults.get(position);
    }


    public void showRetry(boolean show, @Nullable String errorMsg) {
        retryPageLoad = show;
        notifyItemChanged(movieResults.size() - 1);

        if (errorMsg != null) this.errorMsg = errorMsg;
    }



    protected class HeroVH extends RecyclerView.ViewHolder {
        private TextView mMovieTitle;

        public HeroVH(View itemView) {
            super(itemView);

        }
    }


    protected class MovieVH extends RecyclerView.ViewHolder {
        private TextView tv_name;
        private LinearLayout linearLayout;

        public MovieVH(View itemView) {
            super(itemView);

            linearLayout = itemView.findViewById(R.id.sp_linear_main);
            tv_name = itemView.findViewById(R.id.sp_tv_nameXXX);

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
