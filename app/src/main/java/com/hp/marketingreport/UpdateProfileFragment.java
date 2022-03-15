package com.hp.marketingreport;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;
import java.util.Date;

public class UpdateProfileFragment extends Fragment {

    private final StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private TextInputEditText txtInpEditTxtDob;
    private TextInputLayout txtInpLayoutDob;
    private LinearLayout linLayoutVerificationDoc;
    private MaterialButton btnUpdateProfile;
    private TextView txtViewFileName;
    private String dob, employeeType, name, mobNo;
    Timestamp timestamp;
    View root;

    public UpdateProfileFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_update_profile, container, false);
        employeeType = getArguments().getString("employeeType");
        name = getArguments().getString("name");
        mobNo = getArguments().getString("mobNo");

        txtInpEditTxtDob = root.findViewById(R.id.txtInpEditTxtDob);
        txtInpLayoutDob = root.findViewById(R.id.txtInpLayoutDob);
        btnUpdateProfile = root.findViewById(R.id.btnUpdateProfile);
        txtViewFileName = root.findViewById(R.id.txtViewFileName);
        linLayoutVerificationDoc = root.findViewById(R.id.linLayoutVerificationDoc);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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
        linLayoutVerificationDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                browseDoc();
            }
        });
    }

    private void browseDoc() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        activityResultLauncher.launch(Intent.createChooser(intent, "Select Document Image File"));

    }

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        Uri uri = result.getData().getData();
                        btnUpdateProfile.setEnabled(true);
                        txtViewFileName.setText(uri.toString().substring(uri.toString().lastIndexOf("/") + 1));
                        btnUpdateProfile.setOnClickListener(view -> updateToDatabase(uri));
                    }
                }
            }
    );

    private void updateToDatabase(Uri uri) {
        if (dateValid()) {
            final ProgressDialog progressDialog = new ProgressDialog(getActivity(), R.style.ProgressDialog);
            progressDialog.setTitle("Loading");
            progressDialog.setMessage("File is uploading...");
            progressDialog.setIndeterminate(true);
            progressDialog.setProgressStyle(com.google.android.material.R.style.Widget_Material3_CircularProgressIndicator_Medium);
            progressDialog.setProgressDrawable(getResources().getDrawable(R.color.primary));
            progressDialog.setCancelable(false);
            progressDialog.show();
            StorageReference storageReference = this.storageReference.child("verificationDoc/" + name + mobNo + ".png");
            storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                    while ((!uriTask.isComplete())) ;
                    Uri uri = uriTask.getResult();
                    firebaseFirestore.collection(employeeType).document(mobNo).update("verificationDoc", uri.toString());
                    firebaseFirestore.collection(employeeType).document(mobNo).update("dob", timestamp);
                    if (employeeType.equals("admin")) {
                        ((HomeActivity) getActivity()).dob = dob;
                        ((HomeActivity) getActivity()).verificationDoc = uri.toString();
                        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("profile", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("verificationDoc", uri.toString());
                        editor.putString("dob", dob);
                        editor.apply();
                    }
                    progressDialog.dismiss();
                    Navigation.findNavController(root).navigate(R.id.action_UpdateProfileFragment_to_HomeFragment);
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    double progress = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                    progressDialog.setMessage(((int) progress) + "%");
                }
            });
        }

    }

    private boolean dateValid() {
        if (!txtInpEditTxtDob.getText().toString().isEmpty()) {
            dob = txtInpEditTxtDob.getText().toString().trim();
            return true;
        } else {
            txtInpLayoutDob.setError("D.O.B. field can't be empty.");
            return false;
        }
    }
}