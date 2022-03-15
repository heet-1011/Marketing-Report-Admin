package com.hp.marketingreport;

import android.os.Bundle;
import android.view.View;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.button.MaterialButton;

public class AuthenticationActivity extends AppCompatActivity {

    Window window;
    MaterialButton btnTryAgain;
    String way = "";
    NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        try {
            way = getIntent().getStringExtra("way");
            if (way.equals("settings")) {
                Navigation.findNavController(this, R.id.nav_host_fragment_content_authentication).navigate(R.id.FgtPwdFragment);
            } else if (way.equals("signUp")) {
                Navigation.findNavController(this, R.id.nav_host_fragment_content_authentication).navigate(R.id.SignUpFragment);
            }
        } catch (Exception e) {
            e.printStackTrace();
            final NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_content_authentication);
            navController = navHostFragment.getNavController();
        }
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController navController, @NonNull NavDestination navDestination, @Nullable Bundle bundle) {
                switch (navDestination.getId()) {
                    default:
                        chkInternetSpeed();
                }
            }
        });
        btnTryAgain = findViewById(R.id.btnTryAgain);
        btnTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chkInternetSpeed();
            }
        });
    }

    public boolean chkInternetSpeed() {
        if (networkChkClass.chkInternetSpeed(this)) {
            findViewById(R.id.error).setVisibility(View.GONE);
            return true;
        } else {
            findViewById(R.id.error).setVisibility(View.VISIBLE);
            return false;
        }
    }
}