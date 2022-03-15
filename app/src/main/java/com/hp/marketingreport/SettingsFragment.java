package com.hp.marketingreport;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;

public class SettingsFragment extends Fragment {

    MaterialButton btnEditProfile, btnChangePwd, btnLogOut;

    public SettingsFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_settings, container, false);
        btnChangePwd = root.findViewById(R.id.btnChangePwd);
        btnEditProfile = root.findViewById(R.id.btnEditProfile);
        btnLogOut = root.findViewById(R.id.btnLogOut);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnChangePwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AuthenticationActivity.class);
                intent.putExtra("way", "settings");
                startActivity(intent);
                getActivity().finish();
            }
        });
        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle databundle = new Bundle();
                databundle.putString("mode", "edit");
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_home).navigate(R.id.MyProfileFragment, databundle);
            }
        });
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("profile", MODE_PRIVATE);
                sharedPreferences.edit().clear().apply();
                Intent intent = new Intent(getActivity(), AuthenticationActivity.class);
                intent.putExtra("way", "signUp");
                startActivity(intent);
                getActivity().finish();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}