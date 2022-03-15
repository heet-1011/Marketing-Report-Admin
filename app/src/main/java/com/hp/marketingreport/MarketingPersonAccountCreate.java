package com.hp.marketingreport;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MarketingPersonAccountCreate extends Fragment {
    private TextInputLayout txtInpLayoutName, txtInpLayoutEmail, txtInpLayoutMobNo, txtInpLayoutPwd, txtInpLayoutConfPwd;
    private TextInputEditText txtInpEditTxtName, txtInpEditTxtEmail, txtInpEditTxtMobNo, txtInpEditTxtPwd, txtInpEditTxtConfPwd;
    private MaterialButton btnSignUp;
    private String name, email, mobNo, pwd, confPwd;
    private final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private final CollectionReference firestoreAdminCollectionRef = firebaseFirestore.collection("marketingPerson");


    public MarketingPersonAccountCreate() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_marketing_person_account_create, container, false);
        ((HomeActivity) getActivity()).binding.appBarHome.bottomNavigation.setVisibility(View.GONE);
        initViews(root);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnSignUp.setOnClickListener(view1 -> getEnteredData(view));
        txtInpEditTxtName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                txtInpLayoutName.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        txtInpEditTxtMobNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                txtInpLayoutMobNo.setError(null);
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
                txtInpLayoutPwd.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        txtInpEditTxtConfPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                txtInpLayoutConfPwd.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void initViews(View root) {
        txtInpLayoutName = root.findViewById(R.id.txtInpLayoutName);
        txtInpLayoutEmail = root.findViewById(R.id.txtInpLayoutEmail);
        txtInpLayoutMobNo = root.findViewById(R.id.txtInpLayoutMobNo);
        txtInpLayoutPwd = root.findViewById(R.id.txtInpLayoutPwd);
        txtInpLayoutConfPwd = root.findViewById(R.id.txtInpLayoutConfPwd);
        txtInpEditTxtName = root.findViewById(R.id.txtInpEditTxtName);
        txtInpEditTxtEmail = root.findViewById(R.id.txtInpEditTxtEmail);
        txtInpEditTxtMobNo = root.findViewById(R.id.txtInpEditTxtMobNo);
        txtInpEditTxtPwd = root.findViewById(R.id.txtInpEditTxtPwd);
        txtInpEditTxtConfPwd = root.findViewById(R.id.txtInpEditTxtConfPwd);
        btnSignUp = root.findViewById(R.id.btnSignUp);
    }

    private void getEnteredData(View view) {
        if (nameValid() && emailValid() && mobNoValid() && pwdValid() && confPwdValid()) {
            uploadUserData();
        }
    }

    private void uploadUserData() {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity(), R.style.ProgressDialog);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Salesman account creating...");
        progressDialog.setIndeterminate(true);
        progressDialog.setProgressStyle(com.google.android.material.R.style.Widget_Material3_CircularProgressIndicator_Medium);
        progressDialog.setProgressDrawable(getResources().getDrawable(R.color.primary));
        progressDialog.setCancelable(false);
        progressDialog.show();
        DocumentReference documentReference = firestoreAdminCollectionRef.document("+91" + mobNo);
        Map<String, Object> userDataMap = new HashMap<>();
        userDataMap.put("name", name.toUpperCase(Locale.ROOT));
        userDataMap.put("mobileNo", "+91" + mobNo);
        userDataMap.put("emailId", email);
        userDataMap.put("pwd", pwd);
        userDataMap.put("status", 0);
        userDataMap.put("verificationDoc", "");
        userDataMap.put("dob", "");
        userDataMap.put("routeAssign", "");
        documentReference.set(userDataMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getContext(), "Marketing Person Account Created Successfully", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                resetEntries();
                Bundle databundle = new Bundle();
                databundle.putString("mobNo", "+91" + mobNo);
                databundle.putString("name", name);
                databundle.putString("employeeType", "marketingPerson");
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_home).navigate(R.id.UpdateProfileFragment, databundle);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Marketing Person Account Creation Failed", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });
    }

    private void resetEntries() {
        txtInpEditTxtName.setText(null);
        txtInpEditTxtMobNo.setText(null);
        txtInpEditTxtPwd.setText(null);
        txtInpEditTxtConfPwd.setText(null);
        txtInpEditTxtEmail.setText(null);
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
        email = txtInpEditTxtEmail.getText().toString().trim();
        return true;
    }

    private boolean mobNoValid() {
        if (!txtInpEditTxtMobNo.getText().toString().isEmpty()) {
            mobNo = txtInpEditTxtMobNo.getText().toString().trim();
            Log.v("hpname", "'" + mobNo + "'");
            if (mobNo.length() == 10) {
                if (Patterns.PHONE.matcher(mobNo).matches()) {
                    return true;
                } else {
                    txtInpLayoutMobNo.setError("Invalid Mobile No!");
                    return false;
                }
            } else {
                txtInpLayoutMobNo.setError("Mobile No. must be of 10 digit");
                return false;
            }

        } else {
            txtInpLayoutMobNo.setError("Mobile No field can't be empty.");
            return false;
        }
    }

    private boolean pwdValid() {
        if (!txtInpEditTxtPwd.getText().toString().isEmpty()) {
            pwd = txtInpEditTxtPwd.getText().toString().trim();
            Log.v("hpname", "'" + pwd + "'");
            return true;
        } else {
            txtInpLayoutPwd.setError("Password field can't be empty.");
            return false;
        }
    }

    private boolean confPwdValid() {
        if (!txtInpEditTxtConfPwd.getText().toString().isEmpty()) {
            confPwd = txtInpEditTxtConfPwd.getText().toString().trim();
            Log.v("hpname", "'" + confPwd + "'");
            if (confPwd.equals(pwd)) {
                return true;
            } else {
                txtInpLayoutConfPwd.setError("Confirm Password must be same as Password.");
                return false;
            }
        } else {
            txtInpLayoutConfPwd.setError("Confirm Password field can't be empty.");
            return false;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((HomeActivity) getActivity()).binding.appBarHome.bottomNavigation.setVisibility(View.VISIBLE);
    }
}