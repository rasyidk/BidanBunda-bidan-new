package com.example.bidanbunda_client.bottomnavigation.profile.extend.jadwalDiskusi.extendJadwalDiskusi;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.bidanbunda_client.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class timeBottomSheetDialog extends BottomSheetDialogFragment {


    Button btn_batal,btn_ok;
    TimePicker timePicker_start, timePicker_end;
    TextView tv_timestart, tv_timeend;


    private BottomSheetListener mListener;
    int hour_start;
    int minute_start;

    int hour_end;
    int minute_end;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.time_bottom_sheet_layout, container, false);

        btn_batal =  v.findViewById(R.id.time_bts_btn_batal);
        btn_ok = v.findViewById(R.id.time_bts_btn_ok);

        timePicker_start = v.findViewById(R.id.timePicker_start);
        timePicker_end = v.findViewById(R.id.timePicker_end);

        tv_timestart = v.findViewById(R.id.time_bts_tv_time_start);
        tv_timeend = v.findViewById(R.id.time_bts_tv_time_end);

        //24time
        timePicker_start.setIs24HourView(true);
        timePicker_end.setIs24HourView(true);

        //setvalue
        hour_start = timePicker_start.getCurrentHour();
        minute_start = timePicker_end.getCurrentMinute();
        tv_timestart.setText(""+ String.valueOf(hour_start) +"."+ String.valueOf(minute_start) +"");

        hour_end = timePicker_start.getCurrentHour();
        minute_end = timePicker_end.getCurrentMinute();
        tv_timeend.setText(""+ String.valueOf(hour_end) +"."+ String.valueOf(minute_end) +"");


        timePicker_start.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {

                hour_start = hourOfDay;
                minute_end = minute;
//                Toast.makeText(getContext(),""+ String.valueOf(hourOfDay) +": "+ String.valueOf(minute) +"",Toast.LENGTH_SHORT).show();
                tv_timestart.setText(""+ String.valueOf(hourOfDay) +"."+ String.valueOf(minute) +"");
            }
        });

        timePicker_end.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                hour_end = hourOfDay;
                minute_end = minute;
                tv_timeend.setText(""+ String.valueOf(hourOfDay) +"."+ String.valueOf(minute) +"");
            }
        });


        btn_batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();

                Calendar calendar_start = Calendar.getInstance(TimeZone.getTimeZone("GMT"),
                        Locale.getDefault());
                Calendar calendar_end = Calendar.getInstance(TimeZone.getTimeZone("GMT"),
                        Locale.getDefault());

                calendar_start.set(Calendar.HOUR_OF_DAY, hour_start);
                calendar_start.set(Calendar.MINUTE, minute_start);
                calendar_start.set(Calendar.SECOND, 0);



                calendar_end.set(Calendar.HOUR_OF_DAY, hour_end);
                calendar_end.set(Calendar.MINUTE, minute_end);
                calendar_end.set(Calendar.SECOND, 0);

                Long timestamp_start = calendar_start.getTimeInMillis();
                Long timestamp_end = calendar_end.getTimeInMillis();

                Log.d("TS S", String.valueOf(hour_start));
                Log.d("TS E",String.valueOf(hour_end));

                Log.d("TS S", String.valueOf(timestamp_start));
                Log.d("TS E",String.valueOf(timestamp_end));

                String tm_st = tv_timestart.getText().toString();
                String tm_en = tv_timeend.getText().toString() ;
                String time = ""+ tm_st +" - "+ tm_en +"" ;


                mListener.onTimePick(time, tm_st + ".00",tm_en + ".00");
            }
        });
        return v;
    }

    public interface BottomSheetListener{
        void  onTimePick(String time, String tm_st, String tm_en);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try{
            mListener= (BottomSheetListener) context;
        }catch (ClassCastException e){
            throw  new ClassCastException(context.toString() + "Must impelemet");
        }
    }
}
