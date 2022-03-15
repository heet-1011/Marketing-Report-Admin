package com.hp.marketingreport;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class TaskDetailsFragment extends Fragment {

    TextInputEditText txtInpEditTxtStore, txtInpEditTxtContact, txtInpEditTxtEmpName, txtInpEditTxtSalesman, txtInpEditTxtSalesmanContact, txtInpEditTxtDate;
    String store, contact, empName, salesman, salesmanContact, date;
    Double latitude, longitude;
    MaterialButton btnViewLocation;

    public TaskDetailsFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_task_details, container, false);
        initViews(root);
        store = getArguments().getString("storeName");
        contact = getArguments().getString("storeMobNo");
        empName = getArguments().getString("empName");
        salesman = getArguments().getString("salesmanName");
        salesmanContact = getArguments().getString("salesmanMobNo");
        date = getArguments().getString("date");
        latitude = getArguments().getDouble("latitude");
        longitude = getArguments().getDouble("longitude");
        btnViewLocation.setOnClickListener(view -> {
            ArrayList<String> storeNames = new ArrayList<>();
            storeNames.add(store);
            ArrayList<String> storeLatitude = new ArrayList<>();
            storeLatitude.add(String.valueOf(latitude));
            ArrayList<String> storeLongitude = new ArrayList<>();
            storeLongitude.add(String.valueOf(longitude));
            ArrayList<String> salesmans = new ArrayList<>();
            salesmans.add(salesman);
            ArrayList<String> visitDate = new ArrayList<>();
            visitDate.add(date);
            ArrayList<String> storeMobNo = new ArrayList<>();
            storeMobNo.add(contact);
            ArrayList<String> storeEmployee = new ArrayList<>();
            storeEmployee.add(empName);
            Bundle dataBundle = new Bundle();
            dataBundle.putStringArrayList("storeNames", storeNames);
            dataBundle.putStringArrayList("storeLatitude", storeLatitude);
            dataBundle.putStringArrayList("storeLongitude", storeLongitude);
            dataBundle.putStringArrayList("salesman", salesmans);
            dataBundle.putStringArrayList("visitDate", visitDate);
            dataBundle.putStringArrayList("storeMobNo", storeMobNo);
            dataBundle.putStringArrayList("storeEmployee", storeEmployee);
            Navigation.findNavController(view).navigate(R.id.MapsFragment, dataBundle);
        });
        txtInpEditTxtStore.setText(store);
        txtInpEditTxtContact.setText(contact);
        txtInpEditTxtEmpName.setText(empName);
        txtInpEditTxtSalesman.setText(salesman);
        txtInpEditTxtSalesmanContact.setText(salesmanContact);
        txtInpEditTxtDate.setText(date);
        return root;
    }

    private void initViews(View root) {
        txtInpEditTxtStore = root.findViewById(R.id.txtInpEditTxtStore);
        txtInpEditTxtContact = root.findViewById(R.id.txtInpEditTxtContact);
        txtInpEditTxtEmpName = root.findViewById(R.id.txtInpEditTxtEmpName);
        txtInpEditTxtSalesman = root.findViewById(R.id.txtInpEditTxtSalesman);
        txtInpEditTxtSalesmanContact = root.findViewById(R.id.txtInpEditTxtSalesmanContact);
        txtInpEditTxtDate = root.findViewById(R.id.txtInpEditTxtDate);
        btnViewLocation = root.findViewById(R.id.btnViewLocation);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}