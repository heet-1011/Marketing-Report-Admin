package com.hp.marketingreport;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MarketingPersonFragment extends Fragment {

    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    RecyclerView recViewMarketingPerson;
    ArrayList<marketingPersonModel> marketingPersonList;
    marketingPersonAdapter marketingPersonAdapter;
    FloatingActionButton fabAddMarketingPerson;
    SwipeRefreshLayout swipeRefreshLayout;
    String searchQuery = "";
    TextView txtViewNoData;

    public MarketingPersonFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_marketing_person, container, false);
        recViewMarketingPerson = root.findViewById(R.id.recViewMarketingPerson);
        fabAddMarketingPerson = root.findViewById(R.id.fabAddMarketingPerson);
        swipeRefreshLayout = root.findViewById(R.id.SwipeRefreshLayout);
        txtViewNoData = root.findViewById(R.id.txtViewNoData);
        recViewMarketingPerson.setLayoutManager(new LinearLayoutManager(getContext()));
        loadData();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });
        fabAddMarketingPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_MarketingPersonFragment_to_MarketingPersonAccountCreate);
            }
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
        return root;
    }

    public void loadData() {
        marketingPersonList = new ArrayList<>();
        marketingPersonAdapter = new marketingPersonAdapter(marketingPersonList, getContext());
        recViewMarketingPerson.setAdapter(marketingPersonAdapter);
        firebaseFirestore.collection("marketingPerson").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                for (DocumentSnapshot documentSnapshot : list) {
                    marketingPersonModel obj = documentSnapshot.toObject(marketingPersonModel.class);
                    if (documentSnapshot.getString("name").contains(searchQuery)) {
                        marketingPersonList.add(obj);
                    }
                }
                if (marketingPersonList.size() == 0) {
                    Log.v("heetnotify", "none");
                    txtViewNoData.setVisibility(View.VISIBLE);
                    recViewMarketingPerson.setVisibility(View.GONE);
                } else {
                    Log.v("heetnotify", "data");
                    txtViewNoData.setVisibility(View.GONE);
                    recViewMarketingPerson.setVisibility(View.VISIBLE);
                }
                marketingPersonAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

}