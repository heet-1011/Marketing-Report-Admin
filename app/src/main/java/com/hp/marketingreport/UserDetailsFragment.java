package com.hp.marketingreport;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class UserDetailsFragment extends Fragment {

    TextInputEditText txtInpEditTxtName, txtInpEditTxtEmail, txtInpEditTxtMobNo, txtInpEditTxtDob,txtInpEditTxtPwd;
    AutoCompleteTextView autoComTxtViewRoutes;
    MaterialButton btnVerificationDoc, btnViewTimeline, btnUpdate;
    String name, email, mobNo, dob, routeAssign, verificationDoc,pwd, mode = "see", newName, newEmail, newMobNo, fcmToken,newPwd;
    LinearLayout linLayoutButton;
    MenuItem menuItemMain;
    TextInputLayout txtInpLayoutName, txtInpLayoutEmail, txtInpLayoutMobNo, txtInpLayoutDob, txtInpLayoutRouteAssign,txtInpLayoutPwd;
    Timestamp timestamp;
    ArrayList<String> routesList;


    public UserDetailsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_user_details, container, false);
        ((HomeActivity) getActivity()).binding.appBarHome.bottomNavigation.setVisibility(View.GONE);
        initViews(root);
        ((HomeActivity) getActivity()).menuItem1.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                menuItemMain = menuItem;
                if (mode.equals("see")) {
                    Log.v("see", "see");
                    mode = "edit";
                    menuItem.setIcon(R.drawable.close);
                    linLayoutButton.setVisibility(View.GONE);
                    btnUpdate.setVisibility(View.VISIBLE);
                    componentAccessChange(txtInpEditTxtName, true);
                    componentAccessChange(txtInpEditTxtEmail, true);
                    componentAccessChange(txtInpEditTxtPwd,true);
                    autoComTxtViewRoutes.setClickable(true);
                    autoComTxtViewRoutes.setCursorVisible(true);
                    autoComTxtViewRoutes.setFocusable(true);
                    autoComTxtViewRoutes.setFocusableInTouchMode(true);
                    txtInpLayoutRouteAssign.setEndIconMode(TextInputLayout.END_ICON_DROPDOWN_MENU);
                    txtInpEditTxtDob.setOnClickListener(view1 -> {
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
                                        txtInpEditTxtDob.setText(dateParams[2] + " " + dateParams[1] + " " + dateParams[5]);
                                    }
                                }, mYear, mMonth, mDay);
                        datePickerDialog.show();
                    });
                } else {
                    Log.v("see", "edit");
                    mode = "see";
                    menuItem.setIcon(R.drawable.edit_icon);
                    linLayoutButton.setVisibility(View.VISIBLE);
                    btnUpdate.setVisibility(View.GONE);
                    componentAccessChange(txtInpEditTxtName, false);
                    componentAccessChange(txtInpEditTxtEmail, false);
                    componentAccessChange(txtInpEditTxtPwd,false);
                    txtInpEditTxtDob.setOnClickListener(null);
                    autoComTxtViewRoutes.setClickable(false);
                    autoComTxtViewRoutes.setCursorVisible(false);
                    autoComTxtViewRoutes.setFocusable(false);
                    autoComTxtViewRoutes.setFocusableInTouchMode(false);
                    txtInpLayoutRouteAssign.setEndIconMode(TextInputLayout.END_ICON_NONE);
                }

                return true;
            }
        });
        return root;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        txtInpEditTxtName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                txtInpLayoutName.setErrorEnabled(false);
                txtInpLayoutName.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        txtInpEditTxtEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                txtInpLayoutEmail.setErrorEnabled(false);
                txtInpLayoutEmail.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        txtInpEditTxtPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                txtInpLayoutPwd.setErrorEnabled(false);
                txtInpLayoutPwd.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        txtInpEditTxtDob.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                txtInpLayoutDob.setErrorEnabled(false);
                txtInpLayoutDob.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        autoComTxtViewRoutes.setDropDownBackgroundDrawable(getResources().getDrawable(R.color.black));
        name = getArguments().getString("name");
        email = getArguments().getString("email");
        mobNo = getArguments().getString("mobNo");
        dob = getArguments().getString("dob");
        pwd = getArguments().getString("pwd");
        routeAssign = getArguments().getString("routeAssign");
        verificationDoc = getArguments().getString("verificationDoc");
        txtInpEditTxtName.setText(name);
        txtInpEditTxtMobNo.setText(mobNo);
        txtInpEditTxtEmail.setText(email);
        txtInpEditTxtDob.setText(dob);
        autoComTxtViewRoutes.setText(routeAssign);
        txtInpEditTxtPwd.setText(pwd);
        btnVerificationDoc.setOnClickListener(view1 -> {
            if (verificationDoc.isEmpty()) {
                Toast.makeText(getContext(), "Verification ID not available", Toast.LENGTH_LONG).show();
            } else {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(verificationDoc));
                getActivity().startActivity(i);
            }
        });
        btnViewTimeline.setOnClickListener(view1 -> {
            Bundle searchTerms = new Bundle();
            searchTerms.putString("salesmanName", name);
            searchTerms.putString("salesmanMobNo", "");
            searchTerms.putString("storeName", "");
            searchTerms.putString("date", "");
            Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_home).navigate(R.id.TimelineFragment, searchTerms);
        });
        routesList = new ArrayList<String>();
        FirebaseFirestore.getInstance().collection("routes").orderBy("routeName").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                for (DocumentSnapshot documentSnapshot : list) {
                    routesList.add(documentSnapshot.getString("routeName"));
                }
                ArrayAdapter<String> routesAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, routesList);
                autoComTxtViewRoutes.setAdapter(routesAdapter);
                autoComTxtViewRoutes.setThreshold(2);
                autoComTxtViewRoutes.setDropDownBackgroundDrawable(getResources().getDrawable(R.color.black));
            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mode = "see";
                String adminName = ((HomeActivity) getActivity()).name;
                String message = "Your Profile has been updated by Admin '" + adminName + "'";
                menuItemMain.setIcon(R.drawable.edit_icon);
                linLayoutButton.setVisibility(View.VISIBLE);
                btnUpdate.setVisibility(View.GONE);
                componentAccessChange(txtInpEditTxtName, false);
                componentAccessChange(txtInpEditTxtEmail, false);
                componentAccessChange(txtInpEditTxtMobNo, false);
                componentAccessChange(txtInpEditTxtPwd,false);
                txtInpEditTxtDob.setOnClickListener(null);
                autoComTxtViewRoutes.setClickable(false);
                autoComTxtViewRoutes.setCursorVisible(false);
                autoComTxtViewRoutes.setFocusable(false);
                autoComTxtViewRoutes.setFocusableInTouchMode(false);
                txtInpLayoutRouteAssign.setEndIconMode(TextInputLayout.END_ICON_NONE);
                if (nameValid() && emailValid() && pwdValid()) {
                    DocumentReference documentReference = FirebaseFirestore.getInstance().collection("marketingPerson").document(mobNo);
                    documentReference.update("name", newName);
                    documentReference.update("emailId", newEmail);
                    if(!newPwd.equals(pwd)){
                        documentReference.update("pwd",newPwd);
                    }
                    if (timestamp != null) {
                        documentReference.update("dob", timestamp);
                    }
                    if (!routeAssign.equals(autoComTxtViewRoutes.getText().toString().trim().toUpperCase(Locale.ROOT))) {
                        documentReference.update("routeAssign", autoComTxtViewRoutes.getText().toString().trim().toUpperCase(Locale.ROOT));
                        message = "Admin '" + adminName + "' assign you with new Route '" + autoComTxtViewRoutes.getText().toString().trim().toUpperCase(Locale.ROOT) + "'";
                    }
                }
                DocumentReference documentReference = FirebaseFirestore.getInstance().collection("marketingPerson").document(mobNo);
                String finalMessage = message;
                documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        fcmToken = documentSnapshot.getString("fcmToken");
                        FCMSend.pushNotification(getContext(), fcmToken, "Profile Update", finalMessage);
                    }
                });

            }
        });
    }

    private void initViews(View root) {
        txtInpEditTxtName = root.findViewById(R.id.txtInpEditTxtName);
        txtInpEditTxtMobNo = root.findViewById(R.id.txtInpEditTxtMobNo);
        txtInpEditTxtEmail = root.findViewById(R.id.txtInpEditTxtEmail);
        txtInpEditTxtDob = root.findViewById(R.id.txtInpEditTxtDob);
        autoComTxtViewRoutes = root.findViewById(R.id.autoComTxtViewRoutes);
        btnViewTimeline = root.findViewById(R.id.btnViewTimeline);
        btnVerificationDoc = root.findViewById(R.id.btnVerificationDoc);
        linLayoutButton = root.findViewById(R.id.linLayoutButton);
        btnUpdate = root.findViewById(R.id.btnUpdate);
        txtInpLayoutName = root.findViewById(R.id.txtInpLayoutName);
        txtInpLayoutEmail = root.findViewById(R.id.txtInpLayoutEmail);
        txtInpLayoutMobNo = root.findViewById(R.id.txtInpLayoutMobNo);
        txtInpLayoutDob = root.findViewById(R.id.txtInpLayoutDob);
        txtInpLayoutRouteAssign = root.findViewById(R.id.txtInpLayoutRouteAssign);
        txtInpEditTxtPwd = root.findViewById(R.id.txtInpEditTxtPwd);
        txtInpLayoutPwd = root.findViewById(R.id.txtInpLayoutPwd);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (menuItemMain != null) {
            menuItemMain.setIcon(R.drawable.edit_icon);
        }
        linLayoutButton.setVisibility(View.VISIBLE);
        btnUpdate.setVisibility(View.GONE);
        componentAccessChange(txtInpEditTxtName, false);
        componentAccessChange(txtInpEditTxtEmail, false);
        componentAccessChange(txtInpEditTxtPwd,false);
        autoComTxtViewRoutes.setClickable(false);
        autoComTxtViewRoutes.setCursorVisible(false);
        autoComTxtViewRoutes.setFocusable(false);
        autoComTxtViewRoutes.setFocusableInTouchMode(false);
        txtInpEditTxtDob.setOnClickListener(null);
        txtInpLayoutRouteAssign.setEndIconMode(TextInputLayout.END_ICON_NONE);
    }

    public void componentAccessChange(TextInputEditText componentName, boolean b) {
        componentName.setClickable(b);
        componentName.setCursorVisible(b);
        componentName.setFocusable(b);
        componentName.setFocusableInTouchMode(b);
    }

    private boolean nameValid() {
        if (!txtInpEditTxtName.getText().toString().isEmpty()) {
            newName = txtInpEditTxtName.getText().toString().trim();
            Log.v("hpname", "'" + name + "'");
            return true;
        } else {
            txtInpLayoutName.setError("Name field can't be empty.");
            return false;
        }
    }

    private boolean pwdValid() {
        if (!txtInpEditTxtPwd.getText().toString().isEmpty()) {
            newPwd = txtInpEditTxtPwd.getText().toString().trim();
            return true;
        } else {
            txtInpLayoutPwd.setError("Pwd field can't be empty.");
            return false;
        }
    }

    private boolean emailValid() {
        if (!txtInpEditTxtEmail.getText().toString().isEmpty()) {
            newEmail = txtInpEditTxtEmail.getText().toString().trim();
            if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Log.v("hpname", "'" + email + "'");
                return true;
            } else {
                txtInpLayoutEmail.setError("Invalid Email!");
                return false;
            }
        } else {
            return true;
        }
    }
}