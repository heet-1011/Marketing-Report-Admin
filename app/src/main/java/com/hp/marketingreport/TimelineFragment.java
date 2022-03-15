package com.hp.marketingreport;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TimelineFragment extends Fragment {

    RecyclerView recViewTimeline;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    ArrayList<timelineModel> timelineList;
    timelineAdapter timelineAdapter;
    public String salesmanName = "", salesmanMobNo = "", storeName = "", date = "", searchQuery = "", sort = "date";
    ChipGroup chipGrpFilter;
    Chip salesmanNameChip, salesmanMobNoChip, storeNameChip, dateChip;
    BottomSheetDialog bottomSheetDialogfilter, bottomSheetDialogSort;
    MaterialButton btnFilterTimeline, btnAddFilter, btnSortTimeline, btnAddSort;
    AutoCompleteTextView autoComTxtViewSalesmanName, autoComTxtViewSalesmanMobNo, autoComTxtViewStoreName, autoComTxtViewSortBy, autoComTxtViewOrder;
    List<String> salesmanNames;
    List<String> salesmanMobNos;
    ArrayList<String> storeNames, storeLatitude, storeLongitude, storeEmployee, storeMobNo, salesman, visitDate;
    ArrayAdapter<String> salesmanNameAdapter, salesmanMobNoAdapter, storeNameAdapter;
    TextInputEditText txtInpEditTxtDate;
    TextInputLayout txtInpLayoutDate;
    Timestamp timestamp;
    TextView txtViewReset, txtviewResetSort, txtViewNoData;
    public Query.Direction order = Query.Direction.DESCENDING;
    SwipeRefreshLayout swipeRefreshLayout;
    ExtendedFloatingActionButton efabLocation;
    viewModel viewModel;

    public TimelineFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_timeline, container, false);
        bottomSheetDialogfilter = new BottomSheetDialog(getContext(), R.style.BottomSheetDialog);
        bottomSheetDialogfilter.setContentView(R.layout.layout_timeline_filter);
        bottomSheetDialogSort = new BottomSheetDialog(getContext(), R.style.BottomSheetDialog);
        bottomSheetDialogSort.setContentView(R.layout.layout_sort);
        initViews(root);
        salesmanName = getArguments().getString("salesmanName");
        salesmanMobNo = getArguments().getString("salesmanMobNo");
        storeName = getArguments().getString("storeName");
        date = getArguments().getString("date");
        chipGrpFilter = root.findViewById(R.id.chipGrpFilter);
        chkFilterChip();
        recViewTimeline = root.findViewById(R.id.recViewTimeline);
        recViewTimeline.setLayoutManager(new LinearLayoutManager(getContext()));
        loadData();
        return root;
    }

    private void initViews(View root) {
        btnFilterTimeline = root.findViewById(R.id.btnFilterTimeline);
        salesmanNameChip = new Chip(getContext());
        salesmanMobNoChip = new Chip(getContext());
        storeNameChip = new Chip(getContext());
        dateChip = new Chip(getContext());
        autoComTxtViewSalesmanName = (AutoCompleteTextView) bottomSheetDialogfilter.findViewById(R.id.autoComTxtViewSalesmanName);
        autoComTxtViewSalesmanMobNo = (AutoCompleteTextView) bottomSheetDialogfilter.findViewById(R.id.autoComTxtViewSalesmanMobNo);
        autoComTxtViewStoreName = (AutoCompleteTextView) bottomSheetDialogfilter.findViewById(R.id.autoComTxtViewStoreName);
        btnAddFilter = bottomSheetDialogfilter.findViewById(R.id.btnAddFilter);
        txtInpEditTxtDate = bottomSheetDialogfilter.findViewById(R.id.txtInpEditTxtDate);
        txtInpLayoutDate = bottomSheetDialogfilter.findViewById(R.id.txtInpLayoutDate);
        txtViewReset = bottomSheetDialogfilter.findViewById(R.id.txtViewReset);
        autoComTxtViewOrder = bottomSheetDialogSort.findViewById(R.id.autoComTxtViewOrder);
        autoComTxtViewSortBy = bottomSheetDialogSort.findViewById(R.id.autoComTxtViewSortBy);
        txtviewResetSort = bottomSheetDialogSort.findViewById(R.id.txtViewResetSort);
        btnSortTimeline = root.findViewById(R.id.btnSortTimeline);
        btnAddSort = bottomSheetDialogSort.findViewById(R.id.btnAddSort);
        swipeRefreshLayout = root.findViewById(R.id.SwipeRefreshLayout);
        efabLocation = root.findViewById(R.id.efabLocation);
        txtViewNoData = root.findViewById(R.id.txtViewNoData);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(getActivity()).get(com.hp.marketingreport.viewModel.class);
        ArrayAdapter<CharSequence> order = ArrayAdapter.createFromResource(getContext(), R.array.order, android.R.layout.simple_spinner_dropdown_item);
        autoComTxtViewOrder.setAdapter(order);
        ArrayAdapter<CharSequence> sort = ArrayAdapter.createFromResource(getContext(), R.array.timelineSort, android.R.layout.simple_spinner_dropdown_item);
        autoComTxtViewSortBy.setAdapter(sort);
        autoComTxtViewOrder.setDropDownBackgroundDrawable(getResources().getDrawable(R.color.black));
        autoComTxtViewSortBy.setDropDownBackgroundDrawable(getResources().getDrawable(R.color.black));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });
        btnFilterTimeline.setOnClickListener(view1 -> {
            bottomSheetDialogfilter.show();
        });
        btnSortTimeline.setOnClickListener(view1 -> {
            bottomSheetDialogSort.show();
        });
        salesmanNameChip.setOnCloseIconClickListener(view1 -> {
            salesmanName = "";
            loadData();
            chipGrpFilter.removeView(salesmanNameChip);
        });
        salesmanMobNoChip.setOnCloseIconClickListener(view1 -> {
            salesmanMobNo = "";
            loadData();
            chipGrpFilter.removeView(salesmanMobNoChip);
        });
        storeNameChip.setOnCloseIconClickListener(view1 -> {
            storeName = "";
            loadData();
            chipGrpFilter.removeView(storeNameChip);
        });
        dateChip.setOnCloseIconClickListener(view1 -> {
            date = "";
            loadData();
            chipGrpFilter.removeView(dateChip);
        });
        btnAddSort.setOnClickListener(view1 -> {
            switch (autoComTxtViewSortBy.getText().toString()) {
                case "Date":
                    this.sort = "date";
                    break;
                case "Employee Name":
                    this.sort = "empName";
                    break;
                case "Salesman Name":
                    this.sort = "salesmanName";
                    break;
                case "Store Name":
                    this.sort = "storeName";
                    break;
            }
            if (autoComTxtViewOrder.getText().toString().equals("Ascending")) {
                this.order = Query.Direction.ASCENDING;
            } else {
                this.order = Query.Direction.DESCENDING;
            }
            loadData();
        });
        txtviewResetSort.setOnClickListener(view1 -> {
            autoComTxtViewOrder.setText("Descending");
            autoComTxtViewSortBy.setText("Date");
            this.sort = "date";
            this.order = Query.Direction.DESCENDING;
            loadData();
        });
        btnAddFilter.setOnClickListener(view1 -> {
            salesmanName = autoComTxtViewSalesmanName.getText().toString().toUpperCase(Locale.ROOT);
            salesmanMobNo = autoComTxtViewSalesmanMobNo.getText().toString();
            storeName = autoComTxtViewStoreName.getText().toString().toUpperCase(Locale.ROOT);
            date = txtInpEditTxtDate.getText().toString();
            chkFilterChip();
            loadData();
            bottomSheetDialogfilter.dismiss();
        });
        txtViewReset.setOnClickListener(view1 -> {
            txtInpEditTxtDate.setText(null);
            autoComTxtViewSalesmanName.setText(null);
            autoComTxtViewStoreName.setText(null);
            autoComTxtViewSalesmanMobNo.setText(null);
            loadData();
        });
        /*MaterialDatePicker.Builder materialDateBuilder = MaterialDatePicker.Builder.datePicker();
        materialDateBuilder.setTitleText("SELECT VISIT DATE");
        final MaterialDatePicker materialDatePicker = materialDateBuilder.build();
        txtInpEditTxtDate.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            materialDatePicker.show(getFragmentManager(), "MATERIAL_DATE_PICKER");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
        txtInpLayoutDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    materialDatePicker.show(getFragmentManager(), "MATERIAL_DATE_PICKER");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
       materialDatePicker.addOnPositiveButtonClickListener(
                new MaterialPickerOnPositiveButtonClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onPositiveButtonClick(Object selection) {
                        String date;
                        switch (materialDatePicker.getHeaderText().split(" ")[1]){
                            case "Jan":
                                date = materialDatePicker.getHeaderText().replaceAll("Jan","01");
                                break;
                            case "Feb":
                                date = materialDatePicker.getHeaderText().replaceAll("Feb","02");
                                break;
                            case "Mar":
                                date = materialDatePicker.getHeaderText().replaceAll("Mar","03");
                                break;
                            case "Apr":
                                date = materialDatePicker.getHeaderText().replaceAll("Apr","04");
                                break;
                            case "May":
                                date = materialDatePicker.getHeaderText().replaceAll("May","05");
                                break;
                            case "Jun":
                                date = materialDatePicker.getHeaderText().replaceAll("Jun","06");
                                break;
                            case "Jul":
                                date = materialDatePicker.getHeaderText().replaceAll("Jul","07");
                                break;
                            case "Aug":
                                date = materialDatePicker.getHeaderText().replaceAll("Aug","08");
                                break;
                            case "Sep":
                                date = materialDatePicker.getHeaderText().replaceAll("Sep","09");
                                break;
                            case "Oct":
                                date = materialDatePicker.getHeaderText().replaceAll("Oct","10");
                                break;
                            case "Nov":
                                date = materialDatePicker.getHeaderText().replaceAll("Nov","11");
                                break;
                            case "Dec":
                                 date = materialDatePicker.getHeaderText().replaceAll("Dec","12");
                                break;
                            default:
                                throw new IllegalStateException("Unexpected value: " + materialDatePicker.getHeaderText().split(" ")[1]);
                        }
                        txtInpEditTxtDate.setText(date);
                }
                });*/
        txtInpEditTxtDate.setOnClickListener(view1 -> {
            final Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR);
            int mMonth = c.get(Calendar.MONTH);
            int mDay = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                    new DatePickerDialog.OnDateSetListener() {

                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            c.set(Calendar.YEAR, year);
                            c.set(Calendar.MONTH, monthOfYear);
                            c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                            Date dateRepresentations = c.getTime();
                            timestamp = new Timestamp(dateRepresentations);
                            String[] dateParams = timestamp.toDate().toString().split(" ");
                            txtInpEditTxtDate.setText(dateParams[2] + " " + dateParams[1] + " " + dateParams[5]);
                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();

        });
        ((HomeActivity) requireActivity()).searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchQuery = ((HomeActivity) requireActivity()).searchView.getQuery().toString().trim().toUpperCase(Locale.ROOT);
                Toast.makeText(getContext(), searchQuery, Toast.LENGTH_SHORT).show();
                loadData();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchQuery = ((HomeActivity) requireActivity()).searchView.getQuery().toString().trim().toUpperCase(Locale.ROOT);
                if (searchQuery.equals("")) {
                    searchQuery = "";
                    loadData();
                }
                return true;
            }
        });
        efabLocation.setOnClickListener(view1 -> {
            Bundle dataBundle = new Bundle();
            dataBundle.putStringArrayList("storeNames", storeNames);
            dataBundle.putStringArrayList("storeLatitude", storeLatitude);
            dataBundle.putStringArrayList("storeLongitude", storeLongitude);
            dataBundle.putStringArrayList("salesman", salesman);
            dataBundle.putStringArrayList("visitDate", visitDate);
            dataBundle.putStringArrayList("storeMobNo", storeMobNo);
            dataBundle.putStringArrayList("storeEmployee", storeEmployee);
            Navigation.findNavController(view).navigate(R.id.MapsFragment, dataBundle);
        });
    }

    private void chkFilterChip() {
        chipGrpFilter.removeView(salesmanNameChip);
        chipGrpFilter.removeView(salesmanMobNoChip);
        chipGrpFilter.removeView(storeNameChip);
        chipGrpFilter.removeView(dateChip);
        if (!salesmanName.isEmpty()) {
            addFilterChip(salesmanNameChip, salesmanName);
        }
        if (!salesmanMobNo.isEmpty()) {
            addFilterChip(salesmanMobNoChip, salesmanMobNo);
        }
        if (!storeName.isEmpty()) {
            addFilterChip(storeNameChip, storeName);
        }
        if (!date.isEmpty()) {
            addFilterChip(dateChip, date);
        }
    }

    private void addFilterChip(Chip chip, String chipText) {
        chip.setText(chipText);
        chip.setChipBackgroundColorResource(R.color.white);
        chip.setCloseIconVisible(true);
        chip.setTextColor(getResources().getColor(R.color.black));
        chip.setCloseIconTint(ColorStateList.valueOf(getResources().getColor(R.color.black)));
        chipGrpFilter.addView(chip);
    }

    private void loadData() {
        timelineList = new ArrayList<>();
        timelineAdapter = new timelineAdapter(timelineList, getContext());
        recViewTimeline.setAdapter(timelineAdapter);
        firebaseFirestore.collection("dailyReport").orderBy(sort, order).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                salesmanNames = new ArrayList<>();
                salesmanMobNos = new ArrayList<>();
                storeNames = new ArrayList<>();
                storeLatitude = new ArrayList<>();
                storeLongitude = new ArrayList<>();
                storeEmployee = new ArrayList<>();
                storeMobNo = new ArrayList<>();
                salesman = new ArrayList<>();
                visitDate = new ArrayList<>();
                for (DocumentSnapshot documentSnapshot : list) {
                    timelineModel obj = documentSnapshot.toObject(timelineModel.class);
                    Timestamp ctimestamp = (Timestamp) documentSnapshot.get("date");
                    String[] dateParams = ctimestamp.toDate().toString().split(" ");
                    String taskDate = dateParams[2] + " " + dateParams[1] + " " + dateParams[5];
                    if (documentSnapshot.getString("salesmanName").contains(salesmanName) &&
                            documentSnapshot.getString("salesmanMobNo").contains(salesmanMobNo) &&
                            documentSnapshot.getString("storeName").contains(storeName) &&
                            taskDate.contains(date) &&
                            (documentSnapshot.getString("salesmanName").contains(searchQuery) ||
                                    documentSnapshot.getString("salesmanMobNo").contains(searchQuery) ||
                                    documentSnapshot.getString("storeName").contains(searchQuery))) {
                        if (!salesmanNames.contains(documentSnapshot.getString("salesmanName"))) {
                            salesmanNames.add(documentSnapshot.getString("salesmanName"));
                        }
                        if (!salesmanMobNos.contains(documentSnapshot.getString("salesmanMobNo"))) {
                            salesmanMobNos.add(documentSnapshot.getString("salesmanMobNo"));
                        }
                        if (!storeNames.contains(documentSnapshot.getString("storeName"))) {
                            Log.v("store", documentSnapshot.getString("storeName") + " " + String.valueOf(documentSnapshot.getGeoPoint("location").getLatitude())
                                    + " " + String.valueOf(documentSnapshot.getGeoPoint("location").getLongitude()));
                            storeNames.add(documentSnapshot.getString("storeName"));
                            storeLatitude.add(String.valueOf(documentSnapshot.getGeoPoint("location").getLatitude()));
                            storeLongitude.add(String.valueOf(documentSnapshot.getGeoPoint("location").getLongitude()));
                            storeEmployee.add(documentSnapshot.getString("empName"));
                            storeMobNo.add(documentSnapshot.getString("storeMobNo"));
                            salesman.add(documentSnapshot.getString("salesmanName"));
                            visitDate.add(taskDate);
                        }
                        timelineList.add(obj);
                    }
                }
                if (timelineList.size() == 0) {
                    Log.v("heetnotify", "none");
                    txtViewNoData.setVisibility(View.VISIBLE);
                    recViewTimeline.setVisibility(View.GONE);
                } else {
                    Log.v("heetnotify", "data");
                    txtViewNoData.setVisibility(View.GONE);
                    recViewTimeline.setVisibility(View.VISIBLE);
                }
                setDropDownAdapter();
                timelineAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void setDropDownAdapter() {
        salesmanNameAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, salesmanNames);
        autoComTxtViewSalesmanName.setAdapter(salesmanNameAdapter);
        autoComTxtViewSalesmanName.setThreshold(2);
        autoComTxtViewSalesmanName.setDropDownBackgroundDrawable(getResources().getDrawable(R.color.black));
        salesmanMobNoAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, salesmanMobNos);
        autoComTxtViewSalesmanMobNo.setAdapter(salesmanMobNoAdapter);
        autoComTxtViewSalesmanMobNo.setThreshold(2);
        autoComTxtViewSalesmanMobNo.setDropDownBackgroundDrawable(getResources().getDrawable(R.color.black));
        storeNameAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, storeNames);
        autoComTxtViewStoreName.setAdapter(storeNameAdapter);
        autoComTxtViewStoreName.setThreshold(2);
        autoComTxtViewStoreName.setDropDownBackgroundDrawable(getResources().getDrawable(R.color.black));
    }

}