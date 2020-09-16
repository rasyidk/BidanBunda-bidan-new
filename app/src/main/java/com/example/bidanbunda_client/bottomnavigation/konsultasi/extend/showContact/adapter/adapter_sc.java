package com.example.bidanbunda_client.bottomnavigation.konsultasi.extend.showContact.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bidanbunda_client.R;
import com.example.bidanbunda_client.bottomnavigation.konsultasi.Adapter_ksl;

//import com.example.bidanbunda_client.bottomnavigation.konsultasi.extend.chatkonsultasi.chatkonsultasi;
import com.example.bidanbunda_client.bottomnavigation.konsultasi.extend.chatkonsultasi.chatkonsultasi;
import com.example.bidanbunda_client.bottomnavigation.konsultasi.extend.showContact.models.data_sc;

import java.util.List;

public class adapter_sc  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<data_sc> scList;

    public adapter_sc(List<data_sc> scList){
        this.scList = scList;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view;
        view = layoutInflater.inflate(R.layout.row_sc, parent,false);
        return new adapter_sc.ViewHolderOne(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        adapter_sc.ViewHolderOne viewHolderOne = (adapter_sc.ViewHolderOne) holder;
        viewHolderOne.tv_name.setText(scList.get(position).getName());
        viewHolderOne.ln_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), chatkonsultasi.class);
                i.putExtra("name", scList.get(position).getName());
                i.putExtra("targetid", scList.get(position).getId());
                v.getContext().startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return scList.size();
    }

    class ViewHolderOne extends RecyclerView.ViewHolder {

        private TextView tv_name;
        private LinearLayout ln_main;

        public ViewHolderOne(@NonNull View itemView) {
            super(itemView);
            ln_main = (LinearLayout) itemView.findViewById(R.id.sc_ln_main);
            tv_name = (TextView)itemView.findViewById(R.id.sc_tv_name);
        }
    }
}
