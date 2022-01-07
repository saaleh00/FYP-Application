package com.example.medicalcentreappointmentbooker;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.afollestad.sectionedrecyclerview.SectionedRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class TimeSlotAdapter extends SectionedRecyclerViewAdapter<TimeSlotAdapter.ViewHolder> {

    Context context;
    ArrayList<String> timePeriodList;
    HashMap<String, ArrayList<String>> timeSlotList = new HashMap<>();

    int selectedPeriod = -1;
    int selectedTime = -1;

    public TimeSlotAdapter(Context context, ArrayList<String> timePeriodList, HashMap<String, ArrayList<String>> timeSlotList){
        this.context = context;
        this.timePeriodList = timePeriodList;
        this.timeSlotList = timeSlotList;
    }

    @Override
    public int getSectionCount() {
        return timePeriodList.size();
    }

    @Override
    public int getItemCount(int section) {
        return timeSlotList.get(timePeriodList.get(section)).size();
    }

    @Override
    public void onBindHeaderViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.timeHeaderTextView.setText(timePeriodList.get(i));
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i, int i1, int i2) {
        String timeItem = timeSlotList.get(timePeriodList.get(i)).get(i1);
        viewHolder.timeTextView.setText(timeItem);
        
        viewHolder.timeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, timeItem, Toast.LENGTH_SHORT).show();
                selectedPeriod = i;
                selectedTime = i1;
                notifyDataSetChanged();
            }
        });

        if (selectedPeriod == i && selectedTime == i1){
            viewHolder.timeTextView.setBackground(ContextCompat.getDrawable(context, R.drawable.rectangle_fill));
            viewHolder.timeTextView.setTextColor(Color.WHITE);
        }else {
            viewHolder.timeTextView.setBackground(ContextCompat.getDrawable(context, R.drawable.rectangle_outline));
            viewHolder.timeTextView.setTextColor(Color.BLACK);
        }
    }

    @Override
    public int getItemViewType(int section, int relativePosition, int absolutePosition) {
        if (section == 1){
            return 0;
        }
        return super.getItemViewType(section, relativePosition, absolutePosition);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layout;
        if (viewType == VIEW_TYPE_HEADER){
            layout = R.layout.item_time_slot_header;
        }else {
            layout = R.layout.item_time_slot;
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(layout,parent,false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView timeTextView;
        TextView timeHeaderTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
            timeHeaderTextView = itemView.findViewById(R.id.timeHeaderTextView);

        }
    }
}
