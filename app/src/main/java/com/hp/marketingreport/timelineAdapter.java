package com.hp.marketingreport;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.Locale;

public class timelineAdapter extends RecyclerView.Adapter<timelineAdapter.myViewHolder> {

    ArrayList<timelineModel> timelineModelList;
    Context context;

    public timelineAdapter(ArrayList<timelineModel> timelineModelList, Context context) {
        this.timelineModelList = timelineModelList;
        this.context = context;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_timeline, parent, false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        holder.constraintLayTimeline.setAnimation(AnimationUtils.loadAnimation(context, R.anim.recycler_view_animation));
        holder.txtViewStoreName.setText(timelineModelList.get(position).getStoreName().toUpperCase(Locale.ROOT));
        holder.txtViewSalesman.setText(timelineModelList.get(position).getSalesmanName().toUpperCase(Locale.ROOT));
        Timestamp timestamp = timelineModelList.get(position).getDate();
        String[] dateParams = timestamp.toDate().toString().split(" ");
        String date = dateParams[2] + " " + dateParams[1] + " " + dateParams[5];
        holder.txtViewDate.setText(date);
        holder.constraintLayTimeline.setOnClickListener(view -> {
            Bundle dataBundle = new Bundle();
            dataBundle.putString("storeName", timelineModelList.get(position).getStoreName());
            dataBundle.putString("storeMobNo", timelineModelList.get(position).getStoreMobNo());
            dataBundle.putString("empName", timelineModelList.get(position).getEmpName());
            /*dataBundle.putString("location",timelineModelList.get(position).getLocation());*/
            dataBundle.putDouble("latitude", timelineModelList.get(position).getLocation().getLatitude());
            dataBundle.putDouble("longitude", timelineModelList.get(position).getLocation().getLongitude());
            dataBundle.putString("date", date);
            dataBundle.putString("salesmanMobNo", timelineModelList.get(position).getSalesmanMobNo());
            dataBundle.putString("salesmanName", timelineModelList.get(position).getSalesmanName());
            Navigation.findNavController(view).navigate(R.id.action_TimelineFragment_to_TaskDetailsFragment, dataBundle);
        });
    }

    @Override
    public int getItemCount() {
        return timelineModelList.size();
    }

    class myViewHolder extends RecyclerView.ViewHolder {
        TextView txtViewStoreName, txtViewSalesman, txtViewDate;
        ConstraintLayout constraintLayTimeline;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            txtViewStoreName = itemView.findViewById(R.id.txtViewStoreName);
            txtViewSalesman = itemView.findViewById(R.id.txtViewSalesman);
            txtViewDate = itemView.findViewById(R.id.txtViewDate);
            constraintLayTimeline = itemView.findViewById(R.id.constraintLayTimeline);
        }
    }
}
