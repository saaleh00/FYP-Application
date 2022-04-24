package com.example.medicalcentreappointmentbooker.User;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.example.medicalcentreappointmentbooker.Model.TimeSlot;
import com.example.medicalcentreappointmentbooker.R;

import java.util.ArrayList;

public class TimeSlotAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater layoutInflater;

    private ArrayList<TimeSlot> timeSlotList;
    private ItemClickListener clickListener;



    public TimeSlotAdapter(Context context, ArrayList<TimeSlot> timeSlotList, ItemClickListener clickListener){
        this.context = context;
        this.timeSlotList = timeSlotList;
        this.clickListener = clickListener;
    }

    @Override
    public int getCount() {
        return timeSlotList.size();
    }

    @Override
    public Object getItem(int position) {
        return timeSlotList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (layoutInflater == null)
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null){

            convertView = layoutInflater.inflate(R.layout.item_time_slot, null);
        }
        TextView timeSlotTime = convertView.findViewById(R.id.timeTextView);

        timeSlotTime.setText(timeSlotList.get(position).timeSlot);
        timeSlotTime.setEnabled(timeSlotList.get(position).isAvailable);

        timeSlotTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onItemClick(timeSlotList.get(position).timeSlot);
                //Date, Time and Doctor
                notifyDataSetChanged();
            }
        });

        return convertView;
    }

    public interface ItemClickListener {

        public void onItemClick(String timeItem);
    }
}
