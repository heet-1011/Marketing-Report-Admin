package com.hp.marketingreport;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;


public class MapsFragment extends Fragment {

    List<String> storeNames, storeLatitude, storeLongitude, storeEmployee, storeMobNo, salesman, visitDate;
    TextView txtViewDate, txtViewSalesmanName, txtViewStoreMobNo, txtViewEmployeeName, txtViewStoreName;
    MotionLayout motionLayoutMap;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            googleMap.clear();
            UiSettings mapUiSettings = googleMap.getUiSettings();
            mapUiSettings.setRotateGesturesEnabled(true);
            mapUiSettings.setCompassEnabled(true);
            mapUiSettings.setScrollGesturesEnabled(true);
            mapUiSettings.setTiltGesturesEnabled(true);
            mapUiSettings.setZoomGesturesEnabled(true);
            mapUiSettings.setZoomControlsEnabled(true);
            for (int i = 0; i < storeNames.size(); i++) {
                LatLng latLng = new LatLng(Double.parseDouble(storeLatitude.get(i)), Double.parseDouble(storeLongitude.get(i)));
                googleMap.addMarker(new MarkerOptions().position(latLng).title(storeNames.get(i)));
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            }
            googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(@NonNull Marker marker) {
                    int markerIndex = storeNames.indexOf(marker.getTitle());
                    txtViewStoreName.setText(storeNames.get(markerIndex));
                    txtViewSalesmanName.setText(salesman.get(markerIndex));
                    txtViewDate.setText(visitDate.get(markerIndex));
                    txtViewEmployeeName.setText(storeEmployee.get(markerIndex));
                    txtViewStoreMobNo.setText(storeMobNo.get(markerIndex));
                    motionLayoutMap.transitionToEnd();
                    return false;
                }
            });
            googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(@NonNull LatLng latLng) {
                    motionLayoutMap.transitionToStart();
                }
            });
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(8.0f), 1000, null);
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_maps, container, false);
        storeNames = getArguments().getStringArrayList("storeNames");
        storeLatitude = getArguments().getStringArrayList("storeLatitude");
        storeLongitude = getArguments().getStringArrayList("storeLongitude");
        storeEmployee = getArguments().getStringArrayList("storeEmployee");
        storeMobNo = getArguments().getStringArrayList("storeMobNo");
        salesman = getArguments().getStringArrayList("salesman");
        visitDate = getArguments().getStringArrayList("visitDate");
        txtViewDate = root.findViewById(R.id.txtViewDate);
        txtViewSalesmanName = root.findViewById(R.id.txtViewSalesmanName);
        txtViewStoreMobNo = root.findViewById(R.id.txtViewStoreMobNo);
        txtViewStoreName = root.findViewById(R.id.txtViewStoreName);
        txtViewEmployeeName = root.findViewById(R.id.txtViewEmployeeName);
        motionLayoutMap = root.findViewById(R.id.motionLayoutMap);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }
}