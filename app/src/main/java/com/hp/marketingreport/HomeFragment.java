package com.hp.marketingreport;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment {
    LineChart lineChart;
    TextView txtViewTodaysVisits, txtViewTotalVisits, txtViewSalesmen, txtViewRoutes;
    TextInputLayout txtInpLayoutYear;
    TextInputEditText txtInpEditTxtYear;
    private final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    float Jan = 0, Feb = 0, Mar = 0, Apr = 0, May = 0, Jun = 0, Jul = 0, Aug = 0, Sep = 0, Oct = 0, Nov = 0, Dec = 0;
    int totalVisits = 0, todayVisits = 0, totalSalesman = 0, totalRoutes = 0;
    Timestamp timestamp;
    String date;
    SwipeRefreshLayout swipeRefreshLayout;
    int i = 0;

    public HomeFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Calendar c = Calendar.getInstance();
        Date dateRepresentation = c.getTime();
        int mYear = c.get(Calendar.YEAR);
        String mMonth = c.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH).substring(0, 3);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        timestamp = new Timestamp(dateRepresentation);
        date = mDay + " " + mMonth + " " + mYear;
        //Log.v("date",date);
        Log.v("heeeeeeeet", date);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ((HomeActivity) getActivity()).loadData();
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        lineChart = root.findViewById(R.id.lineChart);
        txtViewTotalVisits = root.findViewById(R.id.txtViewTotalVisits);
        txtViewTodaysVisits = root.findViewById(R.id.txtViewTodaysVisits);
        txtViewSalesmen = root.findViewById(R.id.txtViewSalesmen);
        txtViewRoutes = root.findViewById(R.id.txtViewRoutes);
        swipeRefreshLayout = root.findViewById(R.id.SwipeRefreshLayout);
        txtInpLayoutYear = root.findViewById(R.id.txtInpLayoutYear);
        txtInpEditTxtYear = root.findViewById(R.id.txtInpEditTxtYear);
        txtInpEditTxtYear.setText(String.valueOf(Calendar.getInstance().getWeekYear()));
        txtInpEditTxtYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                yearPicker();
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ((HomeActivity) getActivity()).loadData();
            }
        });
        /*firebaseFirestore.collection("marketingPerson").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                totalSalesman = list.size();
                txtViewSalesmen.setText(String.valueOf(totalSalesman));
            }
        });
        firebaseFirestore.collection("routes").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                totalRoutes = list.size();
                txtViewRoutes.setText(String.valueOf(totalRoutes));
            }
        });
        firebaseFirestore.collection("dailyReport").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                totalVisits = list.size();
                for(DocumentSnapshot documentSnapshot:list){
                    Timestamp timestamp = (Timestamp) documentSnapshot.get("date");
                    String[] dateParams = timestamp.toDate().toString().split(" ");
                    String taskDate = dateParams[2]+" "+dateParams[1]+" "+dateParams[5];
                    Log.v("taskdate",taskDate);
                    Log.v("taskdate",date);
                    if(timestamp.toDate().toString().contains("Jan")){
                        Jan++;
                    }else if(timestamp.toDate().toString().contains("Feb")){
                        Feb++;
                    }else if(timestamp.toDate().toString().contains("Mar")){
                        Mar++;
                    }else if(timestamp.toDate().toString().contains("Apr")){
                        Apr++;
                    }else if(timestamp.toDate().toString().contains("May")){
                        May++;
                    }else if(timestamp.toDate().toString().contains("Jun")){
                        Jun++;
                    }else if(timestamp.toDate().toString().contains("Jul")){
                        Jul++;
                    }else if(timestamp.toDate().toString().contains("Aug")){
                        Aug++;
                    }else if(timestamp.toDate().toString().contains("Sep")){
                        Sep++;
                    }else if(timestamp.toDate().toString().contains("Oct")){
                        Oct++;
                    }else if(timestamp.toDate().toString().contains("Nov")){
                        Nov++;
                    }else if(timestamp.toDate().toString().contains("Dec")){
                        Dec++;
                    }
                    if(taskDate.contains(date)){
                        todayVisits++;
                    }
                }
                Log.v("feb", String.valueOf(Feb));
                Log.v("feb", String.valueOf(Jan));

                txtViewTodaysVisits.setText(String.valueOf(todayVisits));
                txtViewTotalVisits.setText(String.valueOf(totalVisits));
                loadGraphDate();
            }
        });*/
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel viewModel = new ViewModelProvider(getActivity()).get(viewModel.class);
        viewModel.getTotalRoutes().observe(getActivity(), item -> {
            txtViewRoutes.setText(item.toString());
        });
        viewModel.getTodayVisits().observe(getActivity(), item -> {
            txtViewTodaysVisits.setText(item.toString());
        });
        viewModel.getTotalVisits().observe(getActivity(), item -> {
            txtViewTotalVisits.setText(item.toString());
        });
        viewModel.getTotalSalesman().observe(getActivity(), item -> {
            txtViewSalesmen.setText(item.toString());
        });
        viewModel.getMonthlyDataCount().observe(getActivity(), item -> {
            Jan = item.get("Jan");
            Feb = item.get("Feb");
            Mar = item.get("Mar");
            Apr = item.get("Apr");
            May = item.get("May");
            Jun = item.get("Jun");
            Jul = item.get("Jul");
            Aug = item.get("Aug");
            Sep = item.get("Sep");
            Oct = item.get("Oct");
            Nov = item.get("Nov");
            Dec = item.get("Dec");
            loadGraphData();
        });
    }

    private void loadGraphData() {
        LineDataSet lineDataSet = new LineDataSet(dataValue(), "Visits Per Month");
        lineDataSet.setLineWidth(3);
        lineDataSet.setColor(getResources().getColor(R.color.primary));
        lineDataSet.setDrawCircles(true);
        lineDataSet.setDrawCircleHole(false);
        lineDataSet.setCircleRadius(5);
        lineDataSet.setCircleColors(getResources().getColor(R.color.primary));
        lineDataSet.setCircleHoleColor(getResources().getColor(R.color.primary));
        lineDataSet.setValueTextSize(12);
        lineDataSet.setDrawFilled(true);
        lineDataSet.setFillDrawable(ContextCompat.getDrawable(getContext(), R.drawable.graph_gradient));
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(lineDataSet);
        Description description = new Description();
        description.setText("");
        Legend legend = lineChart.getLegend();
        legend.setEnabled(true);
        legend.setTextColor(getResources().getColor(R.color.black));
        legend.setTextSize(16);
        LineData data = new LineData(dataSets);
        lineChart.setData(data);
        lineChart.invalidate();
        lineChart.setBackground(getResources().getDrawable(R.drawable.round_bg));
        lineChart.setPadding(10, 10, 10, 10);
        lineChart.setDrawGridBackground(false);
        lineChart.setDescription(description);
        lineChart.getXAxis().setEnabled(true);
        lineChart.getXAxis().setLabelCount(13);
        lineChart.getXAxis().setGranularityEnabled(true);
        lineChart.getXAxis().setGranularity(1.0f);
        lineChart.getAxisLeft().setEnabled(true);
        lineChart.getAxisLeft().setGranularity(1.0f);
        lineChart.getAxisRight().setEnabled(false);
        lineChart.getXAxis().setDrawGridLines(false);
        lineChart.getAxisLeft().setDrawGridLines(false);
        lineChart.getAxisLeft().setStartAtZero(true);
        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        lineChart.setVisibleYRangeMinimum(0, YAxis.AxisDependency.LEFT);
        lineChart.setNoDataTextColor(R.color.black);
        swipeRefreshLayout.setRefreshing(false);
    }

    private List<Entry> dataValue() {
        ArrayList<Entry> dataValues = new ArrayList<Entry>();
        dataValues.add(new Entry(1, Jan));
        dataValues.add(new Entry(2, Feb));
        dataValues.add(new Entry(3, Mar));
        dataValues.add(new Entry(4, Apr));
        dataValues.add(new Entry(5, May));
        dataValues.add(new Entry(6, Jun));
        dataValues.add(new Entry(7, Jul));
        dataValues.add(new Entry(8, Aug));
        dataValues.add(new Entry(9, Sep));
        dataValues.add(new Entry(10, Oct));
        dataValues.add(new Entry(11, Nov));
        dataValues.add(new Entry(12, Dec));
        return dataValues;
    }

    public void yearPicker() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        final View customLayout = getLayoutInflater().inflate(R.layout.layout_year_picker, null);
        NumberPicker numberPicker = customLayout.findViewById(R.id.yearPicker);
        numberPicker.setMinValue(2000);
        numberPicker.setMaxValue(2100);
        numberPicker.setValue(Integer.parseInt(txtInpEditTxtYear.getText().toString().trim()));
        MaterialButton btnSubmit = customLayout.findViewById(R.id.btnSubmit);
        alertDialog.setView(customLayout);
        AlertDialog alert = alertDialog.create();
        alert.setCanceledOnTouchOutside(true);
        alert.show();
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert.dismiss();
                txtInpEditTxtYear.setText(String.valueOf(numberPicker.getValue()));
                ((HomeActivity) getActivity()).loadDailyReportCollection(String.valueOf(numberPicker.getValue()));
            }
        });
    }
}
