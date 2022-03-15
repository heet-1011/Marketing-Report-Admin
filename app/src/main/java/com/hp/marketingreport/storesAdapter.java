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

public class storesAdapter extends RecyclerView.Adapter<storesAdapter.myViewHolder> {

    ArrayList<String> storesModelList;
    Context context;

    public storesAdapter(ArrayList<String> storesModelList, Context context) {
        this.storesModelList = storesModelList;
        this.context = context;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_stores, parent, false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        holder.linLayoutStore.setAnimation(AnimationUtils.loadAnimation(context, R.anim.recycler_view_animation));
        holder.txtViewStore.setText(storesModelList.get(position).toUpperCase(Locale.ROOT));

    }

    @Override
    public int getItemCount() {
        return storesModelList.size();
    }

    class myViewHolder extends RecyclerView.ViewHolder {
        TextView txtViewStore;
        LinearLayout linLayoutStore;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            txtViewStore = itemView.findViewById(R.id.txtViewStores);
            linLayoutStore = itemView.findViewById(R.id.linLayoutStore);
        }
    }
}
