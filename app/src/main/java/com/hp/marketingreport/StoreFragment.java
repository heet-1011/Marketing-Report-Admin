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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class StoreFragment extends Fragment {
    ArrayList<String> storeList;
    storesAdapter storesAdapter;
    RecyclerView recViewStores;
    private final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private final CollectionReference firestoreRoutesCollectionRef = firebaseFirestore.collection("dailyReport");
    String searchQuery = "";
    SwipeRefreshLayout swipeRefreshLayout;
    TextView txtViewNoData;

    public StoreFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_store, container, false);
        swipeRefreshLayout = root.findViewById(R.id.SwipeRefreshLayout);
        recViewStores = root.findViewById(R.id.recViewStores);
        txtViewNoData = root.findViewById(R.id.txtViewNoData);
        recViewStores.setLayoutManager(new LinearLayoutManager(getContext()));
        loadData();
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
    }

    private void loadData() {
        storeList = new ArrayList<String>();
        storesAdapter = new storesAdapter(storeList, getContext());
        recViewStores.setAdapter(storesAdapter);
        firestoreRoutesCollectionRef.orderBy("storeName").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                for (DocumentSnapshot documentSnapshot : list) {
                    if (documentSnapshot.getString("storeName").contains(searchQuery)) {
                        if (!storeList.contains(documentSnapshot.getString("storeName"))) {
                            storeList.add(documentSnapshot.getString("storeName"));
                        }
                    }
                }
                if (storeList.size() == 0) {
                    Log.v("heetnotify", "none");
                    txtViewNoData.setVisibility(View.VISIBLE);
                    recViewStores.setVisibility(View.GONE);
                } else {
                    Log.v("heetnotify", "data");
                    txtViewNoData.setVisibility(View.GONE);
                    recViewStores.setVisibility(View.VISIBLE);
                }
                storesAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }
}