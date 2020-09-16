package com.example.bidanbunda_client.bottomnavigation.profile.extend.jadwalDiskusi.extendJadwalDiskusi;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.bidanbunda_client.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class dateBottomSheetDialog extends BottomSheetDialogFragment {

    private dateBottomSheetListener mListener;
    DatePicker datePicker;
    TextView tv_date;
    List<String> list;
    Button btn_ok,btn_batal;

    int yearx,day,monthx;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.date_bottom_sheet_layout, container, false);

        datePicker  = v.findViewById(R.id.date_datePicker_start);
        tv_date = v.findViewById(R.id.date_tv_date);
        btn_batal = v.findViewById(R.id.date_bts_btn_batal);
        btn_ok = v.findViewById(R.id.date_bts_btn_ok);


        list = new ArrayList<String>();
        list.add("Jan");
        list.add("Feb");
        list.add("Mar");
        list.add("Apr");
        list.add("May");
        list.add("Jun");
        list.add("Jul");
        list.add("Aug");
        list.add("Sep");
        list.add("Oct");
        list.add("Nov");
        list.add("Des");


        day = datePicker.getDayOfMonth();
        yearx =datePicker.getYear();
        monthx = datePicker.getMonth();

        Log.d("MONTT", String.valueOf(datePicker.getMonth()));
        tv_date.setText(""+ list.get(datePicker.getMonth()) +"-" + day +"-"+ yearx +"");


        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {

            @Override
            public void onDateChanged(DatePicker datePicker, int year, int month, int dayOfMonth) {
                Log.d("Date", "Year=" + year + " Month=" + (month + 1) + " day=" + dayOfMonth);

                monthx = month;
                day = dayOfMonth;
                yearx = year;

               // String[] at = new String['jan','feb','mar','apr','may','jun','jul','aug','sep','okt','nov','des'];
                Toast.makeText(getContext(),list.get(month),Toast.LENGTH_SHORT).show();
            }
        });

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                getDate();
            }
        });

        btn_batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return v;
    }

    private void getDate() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"),
                Locale.getDefault());

        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.MONTH, monthx);
        calendar.set(Calendar.YEAR, yearx);

        Long timestamp = calendar.getTimeInMillis();


        String date = ""+ list.get(monthx) +" - "+ day +" - "+ yearx +"";
        String dateforprocess = ""+ String.valueOf(monthx + 1) +"/"+ day +"/"+ yearx +"";
        mListener.onDatePick(date,dateforprocess);

        Log.d("TSXX", String.valueOf(date));
        Log.d("TSXX", String.valueOf(dateforprocess));
    }

    public interface dateBottomSheetListener{
        void  onDatePick(String date, String dateforprocess);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try{
            mListener= (dateBottomSheetListener) context;
        }catch (ClassCastException e){
            throw  new ClassCastException(context.toString() + "Must impelemet");
        }
    }
}
