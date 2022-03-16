package com.hp.marketingreport;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class notificationAdapter extends RecyclerView.Adapter<notificationAdapter.myViewHolder> {

    ArrayList<String[]> notificationModelList;
    Context context;

    public notificationAdapter(ArrayList<String[]> notificationModelList, Context context) {
        this.notificationModelList = notificationModelList;
        this.context = context;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_notification_item, parent, false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        if (notificationModelList.size() != 0) {
            holder.linLayoutNotification.setAnimation(AnimationUtils.loadAnimation(context, R.anim.recycler_view_animation));
            String[] notificationData = notificationModelList.get(position);
            String[] date = notificationData[0].split(" ");
            Log.v("heetnotifiy",notificationData[1]+notificationData[2]);
            holder.txtViewTitle.setText(notificationData[1]);
            holder.txtViewText.setText(notificationData[2]);
            holder.txtViewDate.setText(date[2] + " " + date[1] + " " + date[5]);
        }
    }

    @Override
    public int getItemCount() {
        return notificationModelList.size();
    }

    class myViewHolder extends RecyclerView.ViewHolder {
        TextView txtViewTitle, txtViewText, txtViewDate;
        LinearLayout linLayoutNotification;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            linLayoutNotification = itemView.findViewById(R.id.linLayoutNotification);
            txtViewTitle = itemView.findViewById(R.id.txtViewTitle);
            txtViewText = itemView.findViewById(R.id.txtViewText);
            txtViewDate = itemView.findViewById(R.id.txtViewDate);
        }
    }
}
