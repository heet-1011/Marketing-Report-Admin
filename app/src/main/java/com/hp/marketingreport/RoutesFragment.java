package com.hp.marketingreport;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class RoutesFragment extends Fragment {

    ImageView imgViewClose;
    TextInputLayout txtInpLayoutRoute;
    TextInputEditText txtInpEditTxtRoute;
    FloatingActionButton fabAdd;
    RecyclerView recViewRoutes;
    ArrayList<routesModel> routesList;
    routesAdapter routesAdapter;
    MaterialButton btnAddRoute;
    private final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private final CollectionReference firestoreRoutesCollectionRef = firebaseFirestore.collection("routes");
    BottomSheetDialog bottomSheetDialog;
    SwipeRefreshLayout swipeRefreshLayout;
    TextView txtViewNoData;
    String searchQuery = "";

    public RoutesFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_routes, container, false);
        bottomSheetDialog = new BottomSheetDialog(getContext(), R.style.BottomSheetDialog);
        swipeRefreshLayout = root.findViewById(R.id.SwipeRefreshLayout);
        bottomSheetDialog.setContentView(R.layout.layout_add_routes);
        txtViewNoData = root.findViewById(R.id.txtViewNoData);
        initViews(root);
        recViewRoutes.setLayoutManager(new LinearLayoutManager(getContext()));
        loadData();
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.show();
            }
        });
        txtInpEditTxtRoute.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                txtInpLayoutRoute.setErrorEnabled(false);
                txtInpLayoutRoute.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        btnAddRoute.setOnClickListener(view1 -> getEnteredData(view));
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
        routesList = new ArrayList<>();
        routesAdapter = new routesAdapter(routesList, getContext());
        recViewRoutes.setAdapter(routesAdapter);
        firestoreRoutesCollectionRef.orderBy("routeName").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                for (DocumentSnapshot documentSnapshot : list) {
                    routesModel obj = new routesModel(documentSnapshot.getString("routeName"));
                    Log.v("routes", documentSnapshot.getString("routeName"));
                    Log.v("routes", searchQuery);
                    if (documentSnapshot.getString("routeName").contains(searchQuery)) {
                        routesList.add(obj);
                    }
                }
                if (routesList.size() == 0) {
                    Log.v("heetnotify", "none");
                    txtViewNoData.setVisibility(View.VISIBLE);
                    recViewRoutes.setVisibility(View.GONE);
                } else {
                    Log.v("heetnotify", "data");
                    txtViewNoData.setVisibility(View.GONE);
                    recViewRoutes.setVisibility(View.VISIBLE);
                }
                routesAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void initViews(View root) {
        txtInpLayoutRoute = bottomSheetDialog.findViewById(R.id.txtInpLayoutRoute);
        txtInpEditTxtRoute = bottomSheetDialog.findViewById(R.id.txtInpEditTxtRoute);
        fabAdd = root.findViewById(R.id.fabAdd);
        recViewRoutes = root.findViewById(R.id.recViewRoutes);
        btnAddRoute = bottomSheetDialog.findViewById(R.id.btnAddRoute);
    }

    private void getEnteredData(View view) {
        if (!txtInpEditTxtRoute.getText().toString().isEmpty()) {
            final ProgressDialog progressDialog = new ProgressDialog(getActivity(), R.style.ProgressDialog);
            progressDialog.setTitle("Loading");
            progressDialog.setMessage("Route Creating...");
            progressDialog.setIndeterminate(true);
            progressDialog.setProgressStyle(com.google.android.material.R.style.Widget_Material3_CircularProgressIndicator_Medium);
            progressDialog.setProgressDrawable(getResources().getDrawable(R.color.primary));
            progressDialog.setCancelable(false);
            progressDialog.show();
            DocumentReference documentReference = firestoreRoutesCollectionRef.document();
            Map<String, Object> userDataMap = new HashMap<>();
            userDataMap.put("routeName", txtInpEditTxtRoute.getText().toString().trim().toUpperCase(Locale.ROOT));
            documentReference.set(userDataMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(getContext(), "Route Created Successfully", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                    loadData();
                    txtInpEditTxtRoute.setText(null);
                    bottomSheetDialog.dismiss();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), "Route Creation Failed", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }
            });
        } else {
            txtInpLayoutRoute.setError("Route Field can't be empty.");
        }
    }

}