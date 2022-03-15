package com.hp.marketingreport;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.chaos.view.PinView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class OtpVerificationFragment extends Fragment {
    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private final CollectionReference firestoreAdminCollectionRef = firebaseFirestore.collection("admin");
    private PhoneAuthProvider.ForceResendingToken forceResendToken;
    private TextView txtViewResendOtp, txtViewMobNo;
    private String verificationID, name, email = "", mobNo, pwd, source;
    private MaterialButton btnVerifyOtp;
    private View root;
    private LinearProgressIndicator prgIndicator;
    private PinView otpView;
    ProgressDialog progressDialog;

    public OtpVerificationFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_otp_verification, container, false);
        initViews(root);
        source = getArguments().getString("source");
        mobNo = "+91" + getArguments().getString("mobNo");
        if (source.equals("SignUp")) {
            name = getArguments().getString("name");
            email = getArguments().getString("email");
            pwd = getArguments().getString("pwd");
        }
        Log.v("hpargs", mobNo);
        return root;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (source.equals("SignUp")) {
                    Navigation.findNavController(view).popBackStack(R.id.SignUpFragment, true);
                } else if (source.equals("fgtPwd")) {
                    Navigation.findNavController(view).popBackStack(R.id.FgtPwdFragment, true);
                } else if (source.equals("SignIn")) {
                    Navigation.findNavController(view).popBackStack(R.id.SignInFragment, true);
                }
            }
        };
        txtViewResendOtp.setOnClickListener(view1 -> resendOTP());
        txtViewMobNo.setText("+91 " + mobNo.charAt(3) + "xxxx xx" + mobNo.charAt(10) + mobNo.charAt(11) + mobNo.charAt(12));
        btnVerifyOtp.setOnClickListener(view1 -> getUserOtp());
        createUserWithPhone();
    }

    private void initViews(View root) {
        txtViewResendOtp = root.findViewById(R.id.txtViewresendotp);
        txtViewMobNo = root.findViewById(R.id.txtViewMobNo);
        otpView = root.findViewById(R.id.otpView);
        btnVerifyOtp = root.findViewById(R.id.btnVerifyOtp);
        prgIndicator = root.findViewById(R.id.prgIndicator);
    }

    private void createUserWithPhone() {
        PhoneAuthProvider.verifyPhoneNumber(PhoneAuthOptions.newBuilder(firebaseAuth).
                setPhoneNumber(mobNo)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setCallbacks(callbacks)
                .setActivity(getActivity())
                .build());
        prgIndicator.setIndeterminate(true);
    }

    private void resendOTP() {
        PhoneAuthProvider.verifyPhoneNumber(PhoneAuthOptions.newBuilder(firebaseAuth).
                setPhoneNumber(mobNo)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setCallbacks(callbacks)
                .setActivity(getActivity()).setForceResendingToken(forceResendToken)
                .build());
    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String deviceOtp = phoneAuthCredential.getSmsCode();
            if (deviceOtp != null) {
                verifyUserOtp(deviceOtp);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(getContext(), "OTP Verification Failed", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            forceResendToken = forceResendingToken;
            verificationID = s;
        }
    };

    private void getUserOtp() {
        String userOtp = otpView.getText().toString();
        verifyUserOtp(userOtp);
    }

    private void verifyUserOtp(String userOtp) {
        if (source.equals("SignUp") || source.equals("SignIn")) {
            PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(verificationID, userOtp);
            signInWithPhoneAuthCredentials(phoneAuthCredential);
        } else if (source.equals("fgtPwd")) {
            Bundle dataBundle = new Bundle();
            dataBundle.putString("mobNo", mobNo);
            dataBundle.putString("verificationID", verificationID);
            dataBundle.putString("userOtp", userOtp);
            Navigation.findNavController(root).navigate(R.id.action_otpVerificationFragment_to_rstPwdFragment, dataBundle);
        }
    }

    private void signInWithPhoneAuthCredentials(PhoneAuthCredential phoneAuthCredential) {
        firebaseAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    if (source.equals("SignUp")) {
                        progressDialog = new ProgressDialog(getActivity(), R.style.ProgressDialog);
                        progressDialog.setTitle("Loading");
                        progressDialog.setMessage("Creating Account...");
                        progressDialog.setIndeterminate(true);
                        progressDialog.setProgressStyle(com.google.android.material.R.style.Widget_Material3_CircularProgressIndicator_Medium);
                        progressDialog.setProgressDrawable(getResources().getDrawable(R.color.primary));
                        progressDialog.setCancelable(false);
                        progressDialog.show();
                        uploadUserData();
                    } else if (source.equals("SignIn")) {
                        Intent intent = new Intent(getActivity(), HomeActivity.class);
                        getActivity().startActivity(intent);
                        getActivity().finish();
                    }
                    prgIndicator.setIndeterminate(false);
                } else {
                    Toast.makeText(getContext(), "User Account Creation Failed", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void uploadUserData() {
        DocumentReference documentReference = firestoreAdminCollectionRef.document(mobNo);
        Map<String, Object> userDataMap = new HashMap<>();
        userDataMap.put("name", name.toUpperCase(Locale.ROOT));
        userDataMap.put("mobileNo", mobNo);
        userDataMap.put("emailId", email);
        userDataMap.put("pwd", pwd);
        userDataMap.put("status", 0);
        userDataMap.put("verificationDoc", "");
        userDataMap.put("dob", null);
        documentReference.set(userDataMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getContext(), "Success", Toast.LENGTH_LONG).show();
                FirebaseMessaging.getInstance().getToken().addOnSuccessListener(new OnSuccessListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        documentReference.update("fcmToken", s);
                    }
                });
                progressDialog.dismiss();
                Intent intent = new Intent(getActivity(), HomeActivity.class);
                getActivity().startActivity(intent);
                getActivity().finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Failed", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                Intent intent = new Intent(getActivity(), HomeActivity.class);
                getActivity().startActivity(intent);
                getActivity().finish();
            }
        });

    }


}
