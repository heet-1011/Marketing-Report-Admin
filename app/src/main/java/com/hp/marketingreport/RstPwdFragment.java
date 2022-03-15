package com.hp.marketingreport;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class RstPwdFragment extends Fragment {
    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private DocumentReference documentReference;
    private TextInputLayout txtInpLayoutPwd, txtInpLayoutConfPwd;
    private TextInputEditText txtInpEditTxtPwd, txtInpEditTxtConfPwd;
    private MaterialButton btnResetPwd;
    private String mobNo, pwd, confPwd, verificationID, userOtp;

    public RstPwdFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_rst_pwd, container, false);
        mobNo = getArguments().getString("mobNo");
        verificationID = getArguments().getString("verificationID");
        userOtp = getArguments().getString("userOtp");
        Log.v("heetrst", mobNo);
        initViews(root);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Navigation.findNavController(view).popBackStack(R.id.OtpVerificationFragment, true);
            }
        };
        btnResetPwd.setOnClickListener(view1 -> getEnteredData(view));
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
        txtInpLayoutPwd = root.findViewById(R.id.txtInpLayoutPwd);
        txtInpLayoutConfPwd = root.findViewById(R.id.txtInpLayoutConfPwd);
        txtInpEditTxtPwd = root.findViewById(R.id.txtInpEditTxtPwd);
        txtInpEditTxtConfPwd = root.findViewById(R.id.txtInpEditTxtConfPwd);
        btnResetPwd = root.findViewById(R.id.btnResetPwd);
    }

    private void getEnteredData(View view) {
        if (pwdValid() && confPwdValid()) {
            rstPwd();
        }
    }

    private boolean pwdValid() {
        if (!txtInpEditTxtPwd.getText().toString().isEmpty()) {
            pwd = txtInpEditTxtPwd.getText().toString().trim();
            Log.v("hpname", "'" + pwd + "'");
            /*if (pwd.length() > 6) {
                if(pwdValidationPattern.PASSWORD_PATTERN.matcher(pwd).matches()){
                    return true;
                }else{
                    txtInpLayoutPwd.setError("Password must include LowerCase, UpperCase, Digit & Special Symbol.");
                    return false;
                }
                return true;
            } else {
                txtInpLayoutPwd.setError("Password length must be minimum 6 characted long.");
                return false;
            }*/
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

    private void rstPwd() {
        firebaseFirestore.collection("user").document(mobNo).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    Toast.makeText(getActivity(), "user found your" + documentSnapshot.getString("name"), Toast.LENGTH_LONG).show();
                    Log.v("hp", "user found" + documentSnapshot.getString("name"));
                    signInWithPhoneAuthCredentials("user");
                } else {
                    firebaseFirestore.collection("admin").document(mobNo).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                Toast.makeText(getActivity(), "admin found" + documentSnapshot.getString("name"), Toast.LENGTH_LONG).show();
                                Log.v("hp", "admin found " + documentSnapshot.getString("name"));
                                signInWithPhoneAuthCredentials("admin");
                            } else {
                                Toast.makeText(getActivity(), "No Data Found", Toast.LENGTH_LONG).show();
                                Log.v("hprst", "1");
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.v("hprst", "2");
                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.v("hprst", "3");
            }
        });
    }

    private void signInWithPhoneAuthCredentials(String collection) {
        PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(verificationID, userOtp);
        firebaseAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getContext(), "User Account Created", Toast.LENGTH_LONG).show();
                    documentReference = firebaseFirestore.collection(collection).document(mobNo);
                    documentReference.update("pwd", pwd);
                    Intent intent = new Intent(getActivity(), HomeActivity.class);
                    getActivity().startActivity(intent);
                    getActivity().finish();
                } else {
                    Toast.makeText(getContext(), "Invalid Otp, retry after sometime", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}