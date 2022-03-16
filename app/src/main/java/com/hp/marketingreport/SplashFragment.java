package com.hp.marketingreport;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Timer;
import java.util.TimerTask;

public class SplashFragment extends Fragment {
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    public SplashFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_splash, container, false);
        final NavHostFragment navHostFragment = (NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_content_authentication);
        NavController navController = navHostFragment.getNavController();
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (getActivity() != null) {
                    ((AuthenticationActivity) requireActivity()).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (user != null) {
                                Intent intent = new Intent(requireActivity(), HomeActivity.class);
                                requireActivity().startActivity(intent);
                                requireActivity().finish();
                            } else {
                                navController.navigate(R.id.SignUpFragment);
                            }
                        }
                    });
                }
            }
        }, 3000);
        return root;
    }
}