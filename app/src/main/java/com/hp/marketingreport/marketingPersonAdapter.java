package com.hp.marketingreport;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.Locale;

public class marketingPersonAdapter extends RecyclerView.Adapter<marketingPersonAdapter.myViewHolder> {

    ArrayList<marketingPersonModel> marketingPersonModelList;
    Context context;

    public marketingPersonAdapter(ArrayList<marketingPersonModel> marketingPersonModelList, Context context) {
        this.marketingPersonModelList = marketingPersonModelList;
        this.context = context;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_user, parent, false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        holder.linLayoutmarketingPerson.setAnimation(AnimationUtils.loadAnimation(context, R.anim.recycler_view_animation));
        holder.txtViewUserName.setText(marketingPersonModelList.get(position).getName().toUpperCase(Locale.ROOT));
        holder.txtViewUserMobNo.setText(marketingPersonModelList.get(position).getMobileNo());
        Timestamp timestamp = marketingPersonModelList.get(position).getDob();
        String[] dateParams = timestamp.toDate().toString().split(" ");
        String date = dateParams[2] + " " + dateParams[1] + " " + dateParams[5];
        holder.itemView.setOnClickListener(view -> {
            Bundle dataBundle = new Bundle();
            dataBundle.putString("name", marketingPersonModelList.get(position).getName());
            dataBundle.putString("email", marketingPersonModelList.get(position).getEmailId());
            dataBundle.putString("mobNo", marketingPersonModelList.get(position).getMobileNo());
            dataBundle.putString("dob", date);
            dataBundle.putString("routeAssign", marketingPersonModelList.get(position).getRouteAssign());
            dataBundle.putString("verificationDoc", marketingPersonModelList.get(position).getVerificationDoc());
            dataBundle.putString("pwd",marketingPersonModelList.get(position).getPwd());
            Navigation.findNavController(view).navigate(R.id.action_MarketingPersonFragment_to_userDetailsFragment, dataBundle);
        });
    }

    @Override
    public int getItemCount() {
        return marketingPersonModelList.size();
    }

    class myViewHolder extends RecyclerView.ViewHolder {
        TextView txtViewUserName, txtViewUserMobNo;
        LinearLayout linLayoutmarketingPerson;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            txtViewUserName = itemView.findViewById(R.id.txtViewUserName);
            txtViewUserMobNo = itemView.findViewById(R.id.txtViewUserMobNo);
            linLayoutmarketingPerson = itemView.findViewById(R.id.linLayoutmarketingPerson);
        }
    }
}
