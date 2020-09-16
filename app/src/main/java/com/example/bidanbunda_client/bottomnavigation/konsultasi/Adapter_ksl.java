package com.example.bidanbunda_client.bottomnavigation.konsultasi;

import android.content.Intent;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.bidanbunda_client.R;

import com.example.bidanbunda_client.bottomnavigation.konsultasi.extend.chatkonsultasi.chatkonsultasi;
import com.example.bidanbunda_client.bottomnavigation.konsultasi.extend.chatkonsultasi.models.data_ck;
import com.example.bidanbunda_client.bottomnavigation.konsultasi.models.data_ksl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.zip.CheckedOutputStream;

public class Adapter_ksl extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<data_ksl> dataKslList;

    Adapter_ksl(List<data_ksl> dataKslList) {
        this.dataKslList = dataKslList;

    }



    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view;
        view = layoutInflater.inflate(R.layout.row_ksl, parent,false);
        return new ViewHolderOne(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolderOne viewHolderOne = (ViewHolderOne) holder;
        viewHolderOne.tv_name.setText(dataKslList.get(position).getChatroom_name());
        viewHolderOne.tv_message.setText(dataKslList.get(position).getMessage());


        Calendar cal = Calendar.getInstance(Locale.JAPAN);
        cal.setTimeInMillis(dataKslList.get(position).getTimestamp() * 1000L);
        String date = DateFormat.format("dd-MM-yyyy", cal).toString();
        String time = DateFormat.format("HH:mm", cal).toString();
        viewHolderOne.tv_date.setText(date);
        viewHolderOne.tv_time.setText(time);

        Log.d("TS IN adapter", String.valueOf(dataKslList.get(position).getTimestamp()));



        viewHolderOne.ln_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), chatkonsultasi.class);
                i.putExtra("name", dataKslList.get(position).getChatroom_name());
                i.putExtra("targetid", dataKslList.get(position).getChatroom_user_id());
                v.getContext().startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataKslList.size();
    }



    class ViewHolderOne extends RecyclerView.ViewHolder {

        private TextView tv_name,tv_time,tv_date;
        private TextView tv_message;
        private LinearLayout ln_main;

        public ViewHolderOne(@NonNull View itemView) {
            super(itemView);
            ln_main = (LinearLayout) itemView.findViewById(R.id.ksl_ln_main);
            tv_name = (TextView)itemView.findViewById(R.id.ksl_tv_name);
            tv_message = (TextView) itemView.findViewById(R.id.ksl_tv_msg);

            tv_date = (TextView) itemView.findViewById(R.id.ksl_tv_date);
            tv_time = (TextView) itemView.findViewById(R.id.ksl_tv_time);

        }
    }

    public void add2(data_ksl r) {
        dataKslList.add(0, r);
        notifyItemInserted(0);
    }



    public void addAll2(List<data_ksl> moveResults) {
        for (data_ksl result : moveResults) {
            add2(result);
        }
    }

    public void addNewData(String senderId, String senderName, String message, long longtimestamp) {


            for (int i = 0; i < dataKslList.size(); i++) {
                if (dataKslList.get(i).getChatroom_user_id().equals(senderId)) {
                    Log.d("POSITION", String.valueOf(i));
                    dataKslList.remove(i);
                }
            }
            addtodata(senderId,senderName,message,longtimestamp);


    }

    private void addtodata(String senderId, String senderName, String message, long longtimestamp) {
        List<data_ksl> list = new ArrayList<data_ksl>();
        list.add(0, new data_ksl(senderName, message, senderId, longtimestamp));
        addAll2(list);

    }
}
