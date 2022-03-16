package com.hp.marketingreport;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hp.marketingreport.databinding.ActivityHomeBinding;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {

    private final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private FirebaseUser firebaseUser;
    private AppBarConfiguration mAppBarConfiguration;
    public ActivityHomeBinding binding;
    public String mobNo = "", name = "", verificationDoc, email, dob, employeeType;
    public String date = "", currdate;
    int mYear;
    private TextView txtViewUserName;
    SharedPreferences sharedPreferences,sharedPreferences2;
    NavController navController;
    SearchView searchView;
    public int totalVisits = 0, todayVisits = 0, totalSalesman = 0, totalRoutes = 0;
    public float Jan = 0, Feb = 0, Mar = 0, Apr = 0, May = 0, Jun = 0, Jul = 0, Aug = 0, Sep = 0, Oct = 0, Nov = 0, Dec = 0;
    viewModel viewModel;
    NavHostFragment navHostFragment;
    MenuItem menuItem, menuItem1,menuItem2;
    MaterialButton btnTryAgain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        BottomNavigationView bottomNavigation = binding.appBarHome.bottomNavigation;
        navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_content_home);
        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();
        }
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.HomeFragment, R.id.StoreFragment, R.id.SettingsFragment, R.id.TimelineFragment, R.id.RoutesFragment, R.id.MarketingPersonFragment)
                .setOpenableLayout(drawer)
                .build();
        setSupportActionBar(binding.appBarHome.toolbar);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        NavigationUI.setupWithNavController(bottomNavigation, navController);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mobNo = firebaseUser.getPhoneNumber();
        loadData();
        viewModel = new ViewModelProvider(HomeActivity.this).get(com.hp.marketingreport.viewModel.class);
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        String mMonth = c.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH).substring(0, 3);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        currdate = mDay + " " + mMonth + " " + mYear;
        navController.addOnDestinationChangedListener((navController, navDestination, bundle) -> {

            switch (navDestination.getId()) {
                case R.id.HomeFragment:
                    binding.appBarHome.bottomNavigation.setVisibility(View.VISIBLE);
                    bottomNavigation.getMenu().findItem(R.id.home).setChecked(true);
                    break;
                case R.id.TimelineFragment:
                    chkInternetSpeed();
                    binding.appBarHome.bottomNavigation.setVisibility(View.VISIBLE);
                    bottomNavigation.getMenu().findItem(R.id.timeline).setChecked(true);
                    break;
                case R.id.RoutesFragment:
                    chkInternetSpeed();
                    binding.appBarHome.bottomNavigation.setVisibility(View.VISIBLE);
                    bottomNavigation.getMenu().findItem(R.id.route).setChecked(true);
                    break;
                case R.id.MarketingPersonFragment:
                    chkInternetSpeed();
                    binding.appBarHome.bottomNavigation.setVisibility(View.VISIBLE);
                    bottomNavigation.getMenu().findItem(R.id.salesman).setChecked(true);
                    break;
                case R.id.StoreFragment:
                    binding.appBarHome.bottomNavigation.setVisibility(View.VISIBLE);
                    bottomNavigation.getMenu().findItem(R.id.store).setChecked(true);
                    break;
                default:
                    binding.appBarHome.bottomNavigation.setVisibility(View.GONE);
            }
        });

        bottomNavigation.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    Navigation.findNavController(this, R.id.nav_host_fragment_content_home).navigate(R.id.HomeFragment);
                    break;
                case R.id.timeline:
                    Bundle searchTerms = new Bundle();
                    searchTerms.putString("salesmanName", "");
                    searchTerms.putString("salesmanMobNo", "");
                    searchTerms.putString("storeName", "");
                    searchTerms.putString("date", "");
                    Navigation.findNavController(this, R.id.nav_host_fragment_content_home).navigate(R.id.TimelineFragment, searchTerms);
                    break;
                case R.id.route:
                    Navigation.findNavController(this, R.id.nav_host_fragment_content_home).navigate(R.id.RoutesFragment);
                    break;
                case R.id.salesman:
                    Navigation.findNavController(this, R.id.nav_host_fragment_content_home).navigate(R.id.MarketingPersonFragment);
                    break;
                case R.id.store:
                    Navigation.findNavController(this, R.id.nav_host_fragment_content_home).navigate(R.id.StoreFragment);
                    break;
            }
            return true;
        });
        View headerview = navigationView.getHeaderView(0);
        LinearLayout linLayoutHeader = headerview.findViewById(R.id.linLayoutHeader);
        linLayoutHeader.setOnClickListener(view -> {
            binding.drawerLayout.close();
            Bundle dataBundle = new Bundle();
            dataBundle.putString("mode", "edit");
            Handler handler = new Handler();
            handler.postDelayed(() -> Navigation.findNavController(HomeActivity.this, R.id.nav_host_fragment_content_home).navigate(R.id.MyProfileFragment, dataBundle), 300);
        });
        sharedPreferences = getSharedPreferences("profile", MODE_PRIVATE);
        if (sharedPreferences.contains("name")) {
            name = sharedPreferences.getString("name", "");
            email = sharedPreferences.getString("email", "");
            verificationDoc = sharedPreferences.getString("verificationDoc", "");
            dob = sharedPreferences.getString("dob", "");
            employeeType = sharedPreferences.getString("employeeType", "");
        } else {
            findProfileData();
        }
        View headerView = navigationView.getHeaderView(0);
        txtViewUserName = headerView.findViewById(R.id.txtViewUserName);
        txtViewUserName.setText(name.toUpperCase(Locale.ROOT));
        btnTryAgain = findViewById(R.id.btnTryAgain);
        btnTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chkInternetSpeed();
            }
        });

    }

    private void findProfileData() {
        firebaseFirestore.collection("admin").document(mobNo).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                employeeType = "admin";
                getProfileData(documentSnapshot);
            } else {
                Toast.makeText(HomeActivity.this, "Profile Data Not Found", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(e -> {
        });

    }




    private void getProfileData(DocumentSnapshot documentSnapshot) {
        name = documentSnapshot.getString("name");
        email = documentSnapshot.getString("emailId");
        mobNo = documentSnapshot.getString("mobileNo");
        verificationDoc = documentSnapshot.getString("verificationDoc");
        Timestamp ctimestamp = (Timestamp) documentSnapshot.get("dob");
        String[] dateParams = ctimestamp.toDate().toString().split(" ");
        dob = dateParams[2] + " " + dateParams[1] + " " + dateParams[5];
        if (verificationDoc.equals("")) {
            Bundle databundle = new Bundle();
            databundle.putString("mobNo", mobNo);
            databundle.putString("name", name);
            databundle.putString("employeeType", employeeType);
            Navigation.findNavController(this, R.id.nav_host_fragment_content_home).navigate(R.id.UpdateProfileFragment, databundle);
        }
        SharedPreferences sharedPreferences = getSharedPreferences("profile", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("name", name);
        editor.putString("email", email);
        editor.putString("verificationDoc", verificationDoc);
        editor.putString("dob", dob);
        editor.putString("employeeType", employeeType);
        editor.apply();
        txtViewUserName.setText(name.toUpperCase(Locale.ROOT));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        menuItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Type here to search");
        EditText editText = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        editText.setTextColor(getResources().getColor(R.color.black));
        editText.setHintTextColor(getResources().getColor(R.color.black));
        ImageView imageView1 = searchView.findViewById(androidx.appcompat.R.id.search_close_btn);
        imageView1.setImageDrawable(getDrawable(R.drawable.close));
        imageView1.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.black)));
        menuItem1 = menu.findItem(R.id.action_edit);
        menuItem2 = menu.findItem(R.id.action_notification);
        sharedPreferences2 = getSharedPreferences("msgRecieved", Context.MODE_PRIVATE);
        sharedPreferences2.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
                if(sharedPreferences.getAll().isEmpty()){
                    menuItem2.setIcon(R.drawable.notification_icon);
                }else{
                    menuItem2.setIcon(R.drawable.notification_active_icon);
                }
            }
        });
        Map<String, ?> notifications = new HashMap<>();
        notifications =  sharedPreferences2.getAll();
        if(notifications.isEmpty()){
            menuItem2.setIcon(R.drawable.notification_icon);
        }else{
            menuItem2.setIcon(R.drawable.notification_active_icon);
        }
        menuItem2.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Navigation.findNavController(HomeActivity.this, R.id.nav_host_fragment_content_home).navigate(R.id.NotificationFragment);
                return true;
            }
        });
        navController.addOnDestinationChangedListener((navController, navDestination, bundle) -> {
            switch (navDestination.getId()) {
                case R.id.HomeFragment:
                    menuItem.setVisible(false);
                    menuItem1.setVisible(false);
                    menuItem2.setVisible(true);
                    break;
                case R.id.TimelineFragment:
                case R.id.MarketingPersonFragment:
                case R.id.RoutesFragment:
                case R.id.StoreFragment:
                    menuItem.setVisible(true);
                    menuItem1.setVisible(false);
                    menuItem2.setVisible(false);
                    break;
                case R.id.UserDetailsFragment:
                    menuItem.setVisible(false);
                    menuItem1.setVisible(true);
                    menuItem2.setVisible(false);
                    break;
                default:
                    menuItem.setVisible(false);
                    menuItem1.setVisible(false);
                    menuItem2.setVisible(false);
            }
        });
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void loadData() {
        loadRoutesCollection();
        loadDailyReportCollection(String.valueOf(mYear));
        loadMarketingPersonCollection();
    }

    public void loadRoutesCollection() {
        firebaseFirestore.collection("routes").get().addOnSuccessListener(queryDocumentSnapshots -> {
            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
            totalRoutes = list.size();
            viewModel.setTotalRoutes(totalRoutes);
        });
    }

    public void loadMarketingPersonCollection() {
        firebaseFirestore.collection("marketingPerson").get().addOnSuccessListener(queryDocumentSnapshots -> {
            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
            totalSalesman = list.size();
            viewModel.setTotalSalesman(totalSalesman);
        });
    }

    public void loadDailyReportCollection(String year) {
        totalVisits = 0;
        todayVisits = 0;
        firebaseFirestore.collection("dailyReport").get().addOnSuccessListener(queryDocumentSnapshots -> {
            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
            totalVisits = list.size();
            Jan = 0;
            Feb = 0;
            Mar = 0;
            Apr = 0;
            May = 0;
            Jun = 0;
            Jul = 0;
            Aug = 0;
            Sep = 0;
            Oct = 0;
            Nov = 0;
            Dec = 0;

            for (DocumentSnapshot documentSnapshot : list) {
                Timestamp timestamp = (Timestamp) documentSnapshot.get("date");
                String[] dateParams = timestamp.toDate().toString().split(" ");
                String taskDate = dateParams[2] + " " + dateParams[1] + " " + dateParams[5];
                String chkDate = timestamp.toDate().toString();
                if (chkDate.contains("Jan") && chkDate.contains(year)) {
                    Jan++;
                } else if (chkDate.contains("Feb") && chkDate.contains(year)) {
                    Feb++;
                } else if (chkDate.contains("Mar") && chkDate.contains(year)) {
                    Mar++;
                } else if (chkDate.contains("Apr") && chkDate.contains(year)) {
                    Apr++;
                } else if (chkDate.contains("May") && chkDate.contains(year)) {
                    May++;
                } else if (chkDate.contains("Jun") && chkDate.contains(year)) {
                    Jun++;
                } else if (chkDate.contains("Jul") && chkDate.contains(year)) {
                    Jul++;
                } else if (chkDate.contains("Aug") && chkDate.contains(year)) {
                    Aug++;
                } else if (chkDate.contains("Sep") && chkDate.contains(year)) {
                    Sep++;
                } else if (chkDate.contains("Oct") && chkDate.contains(year)) {
                    Oct++;
                } else if (chkDate.contains("Nov") && chkDate.contains(year)) {
                    Nov++;
                } else if (chkDate.contains("Dec") && chkDate.contains(year)) {
                    Dec++;
                }
                if (taskDate.contains(currdate)) {
                    todayVisits++;
                }
            }
            HashMap<String, Float> monthlyDataCount = new HashMap<String, Float>();
            monthlyDataCount.put("Jan", Jan);
            monthlyDataCount.put("Feb", Feb);
            monthlyDataCount.put("Mar", Mar);
            monthlyDataCount.put("Apr", Apr);
            monthlyDataCount.put("May", May);
            monthlyDataCount.put("Jun", Jun);
            monthlyDataCount.put("Jul", Jul);
            monthlyDataCount.put("Aug", Aug);
            monthlyDataCount.put("Sep", Sep);
            monthlyDataCount.put("Oct", Oct);
            monthlyDataCount.put("Nov", Nov);
            monthlyDataCount.put("Dec", Dec);
            viewModel.setMonthlyDataCount(monthlyDataCount);
            viewModel.setTodayVisits(todayVisits);
            viewModel.setTotalVisits(totalVisits);
        });
    }

    public void chkInternetSpeed() {
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (networkChkClass.chkInternetSpeed(this)) {
            findViewById(R.id.error).setVisibility(View.GONE);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.white));
        } else {
            findViewById(R.id.error).setVisibility(View.VISIBLE);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.secondary));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        chkInternetSpeed();
    }
}