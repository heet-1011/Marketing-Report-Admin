package com.hp.marketingreport;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MyProfileFragment extends Fragment {

    private final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private final CollectionReference firestoreAdminCollectionRef = firebaseFirestore.collection("admin");
    TextInputEditText txtInpEditTxtName, txtInpEditTxtMobNo, txtInpEditTxtEmail, txtInpEditTxtDob;
    TextInputLayout txtInpLayoutName, txtInpLayoutEmail, txtInpLayoutDob;
    String mode = "", name, email, dob, mobNo;
    MaterialButton btnSubmit;
    Timestamp timestamp;

    public MyProfileFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_my_profile, container, false);
        initViews(root);
        mobNo = ((HomeActivity) getActivity()).mobNo;
        try {
            mode = getArguments().getString("mode");
        } catch (Exception e) {
            mode = "";
        }
        if (mode.equals("edit")) {
            btnSubmit.setVisibility(View.VISIBLE);
            btnSubmit.setEnabled(true);
            componentAccessChange(txtInpEditTxtName, true);
            componentAccessChange(txtInpEditTxtEmail, true);
            componentAccessChange(txtInpEditTxtDob, true);
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
        }
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnSubmit.setOnClickListener(view1 -> {
            getEnteredData(view);
        });
        loadData();
        txtInpEditTxtName.setText(((HomeActivity) getActivity()).name.toUpperCase(Locale.ROOT));
        txtInpEditTxtMobNo.setText(((HomeActivity) getActivity()).mobNo);
        txtInpEditTxtEmail.setText(((HomeActivity) getActivity()).email);
        txtInpEditTxtDob.setText(((HomeActivity) getActivity()).dob);
    }

    private void loadData() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("profile", MODE_PRIVATE);
        txtInpEditTxtName.setText(sharedPreferences.getString("name",""));
        mobNo = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
        txtInpEditTxtMobNo.setText(mobNo);
        txtInpEditTxtEmail.setText(sharedPreferences.getString("email",""));
        txtInpEditTxtDob.setText(sharedPreferences.getString("dob",""));
    }

    private void initViews(View root) {
        txtInpEditTxtName = root.findViewById(R.id.txtInpEditTxtName);
        txtInpEditTxtMobNo = root.findViewById(R.id.txtInpEditTxtMobNo);
        txtInpEditTxtEmail = root.findViewById(R.id.txtInpEditTxtEmail);
        txtInpEditTxtDob = root.findViewById(R.id.txtInpEditTxtDob);
        btnSubmit = root.findViewById(R.id.btnSubmit);
    }

    public void componentAccessChange(TextInputEditText componentName, boolean b) {
        componentName.setClickable(b);
        componentName.setCursorVisible(b);
        componentName.setFocusable(b);
        componentName.setFocusableInTouchMode(b);
    }

    private void getEnteredData(View view) {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity(), R.style.ProgressDialog);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Profile updating...");
        progressDialog.setIndeterminate(true);
        progressDialog.setProgressStyle(com.google.android.material.R.style.Widget_Material3_CircularProgressIndicator_Medium);
        progressDialog.setProgressDrawable(getResources().getDrawable(R.color.primary));
        progressDialog.setCancelable(false);
        progressDialog.show();
        if (nameValid() && emailValid() && dobValid()) {
            DocumentReference documentReference = firestoreAdminCollectionRef.document(mobNo);
            Map<String, Object> userDataMap = new HashMap<>();
            userDataMap.put("name", name.toUpperCase(Locale.ROOT));
            userDataMap.put("emailId", email);
            userDataMap.put("dob", timestamp);
            documentReference.update(userDataMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(getContext(), "Success", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                    componentAccessChange(txtInpEditTxtName, false);
                    componentAccessChange(txtInpEditTxtEmail, false);
                    componentAccessChange(txtInpEditTxtDob, false);
                    txtInpEditTxtDob.setOnClickListener(null);
                    btnSubmit.setVisibility(View.GONE);
                    SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("profile", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("name", name);
                    editor.putString("email", email);
                    editor.putString("dob", dob);
                    editor.apply();
                    loadData();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    loadData();
                }
            });
        }
    }

    private boolean dobValid() {
        if (!txtInpEditTxtDob.getText().toString().isEmpty()) {
            dob = txtInpEditTxtDob.getText().toString().trim();
            return true;
        } else {
            txtInpLayoutDob.setError("Dob field can't be empty.");
            return false;
        }
    }

    private boolean nameValid() {
        if (!txtInpEditTxtName.getText().toString().isEmpty()) {
            name = txtInpEditTxtName.getText().toString().trim();
            Log.v("hpname", "'" + name + "'");
            return true;
        } else {
            txtInpLayoutName.setError("Name field can't be empty.");
            return false;
        }
    }

    private boolean emailValid() {
        if (!txtInpEditTxtEmail.getText().toString().isEmpty()) {
            email = txtInpEditTxtEmail.getText().toString().trim();
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