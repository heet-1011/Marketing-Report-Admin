package com.hp.marketingreport;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignInFragment extends Fragment {
    private final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private TextInputEditText txtInpEditTxtMobNo, txtInpEditTxtPwd;
    private TextInputLayout txtInpLayoutMobNo, txtInpLayoutPwd;
    private TextView txtViewFgtPwd, txtViewSignUp;
    private MaterialButton btnSignIn;
    private String mobNo, pwd;
    private LinearProgressIndicator prgIndicator;
    private View root;

    public SignInFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_sign_in, container, false);
        initViews(root);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnSignIn.setOnClickListener(view1 -> getEnteredData());
        txtViewSignUp.setOnClickListener(view1 -> {
            Navigation.findNavController(view).navigate(R.id.action_signInFragment_to_signUpFragment);
        });
        txtInpEditTxtMobNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                txtInpLayoutMobNo.setErrorEnabled(false);
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
                txtInpLayoutPwd.setErrorEnabled(false);
                txtInpLayoutPwd.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        txtViewFgtPwd.setOnClickListener(view1 -> {
            Navigation.findNavController(view).navigate(R.id.action_signInFragment_to_fgtPwdFragment);
        });
    }

    private void initViews(View root) {
        txtInpLayoutMobNo = root.findViewById(R.id.txtInpLayoutMobNo);
        txtInpLayoutPwd = root.findViewById(R.id.txtInpLayoutPwd);
        txtInpEditTxtMobNo = root.findViewById(R.id.txtInpEditTxtMobNo);
        txtInpEditTxtPwd = root.findViewById(R.id.txtInpEditTxtPwd);
        txtViewFgtPwd = root.findViewById(R.id.txtViewFgtPwd);
        txtViewSignUp = root.findViewById(R.id.txtViewSignUp);
        btnSignIn = root.findViewById(R.id.btnSignIn);
        prgIndicator = root.findViewById(R.id.prgIndicator);
    }

    private void getEnteredData() {
        prgIndicator.setIndeterminate(true);
        if (mobNoValid() && pwdValid()) {
            firebaseFirestore.collection("admin").document("+91" + mobNo).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()) {
                        Toast.makeText(getActivity(), "admin found yourpwd " + pwd + "getpwd " + documentSnapshot.getString("pwd"), Toast.LENGTH_LONG).show();
                        Log.v("hp", "admin found yourpwd " + pwd + "getpwd " + documentSnapshot.getString("name"));
                        if (pwd.equals(documentSnapshot.getString("pwd"))) {
                            Bundle databundle = new Bundle();
                            databundle.putString("source", "SignIn");
                            databundle.putString("mobNo", mobNo);
                            Navigation.findNavController(root).navigate(R.id.action_signInFragment_to_otpVerificationFragment, databundle);
                        } else {
                            prgIndicator.setIndeterminate(false);
                            txtInpLayoutPwd.setError("Wrong Password!");
                        }
                    } else {
                        prgIndicator.setIndeterminate(false);
                        Toast.makeText(getActivity(), "No Data Found", Toast.LENGTH_LONG).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    prgIndicator.setIndeterminate(false);
                }
            });
        }
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

}