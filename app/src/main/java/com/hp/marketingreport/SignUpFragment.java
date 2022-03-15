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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class SignUpFragment extends Fragment {

    private TextInputLayout txtInpLayoutName, txtInpLayoutEmail, txtInpLayoutMobNo, txtInpLayoutPwd, txtInpLayoutConfPwd;
    private TextInputEditText txtInpEditTxtName, txtInpEditTxtEmail, txtInpEditTxtMobNo, txtInpEditTxtPwd, txtInpEditTxtConfPwd;
    private TextView txtViewSignIn;
    private MaterialButton btnSignUp;
    private String name, email, mobNo, pwd, confPwd;

    public SignUpFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_sign_up, container, false);
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
        txtInpEditTxtConfPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                txtInpLayoutConfPwd.setErrorEnabled(false);
                txtInpLayoutConfPwd.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        txtViewSignIn.setOnClickListener(view1 -> {
            Navigation.findNavController(view).navigate(R.id.action_signUpFragment_to_signInFragment);

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
        txtViewSignIn = root.findViewById(R.id.txtViewSignIn);
    }

    private void getEnteredData(View view) {
        if (nameValid() && emailValid() && mobNoValid() && pwdValid() && confPwdValid()) {
            Bundle databundle = new Bundle();
            databundle.putString("source", "SignUp");
            databundle.putString("name", name);
            databundle.putString("mobNo", mobNo);
            databundle.putString("email", email);
            databundle.putString("pwd", pwd);
            Navigation.findNavController(view).navigate(R.id.action_signUpFragment_to_otpVerificationFragment, databundle);
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
            //txtInpLayoutEmail.setError("Email field can't be empty");
            //return false;
            return true;
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
}