package com.hp.marketingreport;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class FgtPwdFragment extends Fragment {

    private TextInputLayout txtInpLayoutMobNo;
    private TextInputEditText txtInpEditTxtMobNo;
    private MaterialButton btnVerifyOtp;
    private String mobNo;

    public FgtPwdFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_fgt_pwd, container, false);
        initViews(root);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Navigation.findNavController(view).popBackStack(R.id.SignInFragment, true);
            }
        };
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
        btnVerifyOtp.setOnClickListener(view1 -> getEnteredData(view));
    }

    private void initViews(View root) {
        txtInpLayoutMobNo = root.findViewById(R.id.txtInpLayoutMobNo);
        txtInpEditTxtMobNo = root.findViewById(R.id.txtInpEditTxtMobNo);
        btnVerifyOtp = root.findViewById(R.id.btnVerifyOtp);
    }

    private void getEnteredData(View view) {
        if (mobNoValid()) {
            Bundle databundle = new Bundle();
            databundle.putString("source", "fgtPwd");
            databundle.putString("mobNo", mobNo);
            Navigation.findNavController(view).navigate(R.id.action_fgtPwdFragment_to_otpVerificationFragment, databundle);
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
}