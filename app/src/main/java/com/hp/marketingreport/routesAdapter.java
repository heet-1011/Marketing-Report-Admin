package com.hp.marketingreport;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Locale;

public class routesAdapter extends RecyclerView.Adapter<routesAdapter.myViewHolder> {

    ArrayList<routesModel> routesModelList;
    Context context;

    public routesAdapter(ArrayList<routesModel> routesModelList, Context context) {
        this.routesModelList = routesModelList;
        this.context = context;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_routes, parent, false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        holder.linLayoutRoute.setAnimation(AnimationUtils.loadAnimation(context, R.anim.recycler_view_animation));
        holder.txtViewRoute.setText(routesModelList.get(position).getRouteName().toUpperCase(Locale.ROOT));
    }

    @Override
    public int getItemCount() {
        return routesModelList.size();
    }

    class myViewHolder extends RecyclerView.ViewHolder {
        TextView txtViewRoute;
        LinearLayout linLayoutRoute;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            txtViewRoute = itemView.findViewById(R.id.txtViewRoute);
            linLayoutRoute = itemView.findViewById(R.id.linLayoutRoute);
        }
    }
}