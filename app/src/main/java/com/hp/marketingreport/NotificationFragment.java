package com.hp.marketingreport;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class NotificationFragment extends Fragment {

    RecyclerView recViewNotifications;
    TextView txtViewNoData;
    MaterialButton btnClear;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public NotificationFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_notification, container, false);
        recViewNotifications = root.findViewById(R.id.recViewNotifications);
        recViewNotifications.setLayoutManager(new LinearLayoutManager(getContext()));
        txtViewNoData = root.findViewById(R.id.txtViewNoData);
        btnClear = root.findViewById(R.id.btnClear);
        sharedPreferences = getContext().getSharedPreferences("msgRecieved", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        notifications();
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.clear();
                editor.apply();
                notifications();
                //((HomeActivity) requireActivity()).menuItem2.setIcon(R.drawable.notification_icon);
            }
        });
    }

    public void notifications() {
        ArrayList<String[]> notificationList = new ArrayList<>();
        notificationAdapter notificationAdapter = new notificationAdapter(notificationList,getContext());
        recViewNotifications.setAdapter(notificationAdapter);
        Map<String, ?> notifications = new HashMap<>();
        notifications =  sharedPreferences.getAll();
        for (Map.Entry<String, ?> entry : notifications.entrySet()) {
            String value = (String) entry.getValue();
            String[] notificationData = value.split("\\$");
            notificationList.add(notificationData);
        }
        if(notificationList.size()==0){
            Log.v("heetnotify","none");
            txtViewNoData.setVisibility(View.VISIBLE);
            recViewNotifications.setVisibility(View.GONE);
        }else{
            Log.v("heetnotify","data");
            txtViewNoData.setVisibility(View.GONE);
            recViewNotifications.setVisibility(View.VISIBLE);
        }
        notificationAdapter.notifyDataSetChanged();
    }
}