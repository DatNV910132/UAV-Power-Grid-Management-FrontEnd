package com.dji.sdk.sample.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.dji.sdk.sample.JsonPlaceHolderApi;
import com.dji.sdk.sample.R;
import com.dji.sdk.sample.ui.activity.login.LoginSystemActivity;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * The type Pilot start activity.
 */
public class PilotStartActivity extends AppCompatActivity {
    private final String SHARED_PREFERENCES_NAME = "login";
    private final String ID_USER = "id_user";
    private final String NAME_USER = "name_user";
    private final String MAIL_USER = "mail_user";
    private final String ROLE_USER = "role_user";
    private JsonPlaceHolderApi jsonPlaceHolderApi;
    private AppBarConfiguration mAppBarConfiguration;

    private TextView mtv_name_user;
    private TextView mtv_role_user;
    private TextView mtv_email_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pilot_start);
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, getApplicationContext().MODE_PRIVATE);
        String id_user = sharedPreferences.getString(ID_USER, "");
        String name_user = sharedPreferences.getString(NAME_USER, "");
        String mail_user = sharedPreferences.getString(MAIL_USER, "");
        String role_user = sharedPreferences.getString(ROLE_USER, "");
        Log.d("ID_USER", id_user);

        //Khai báo các biến đầu vào để Call API từ server tương ứng tại baseURL
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.URLDataService))
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        NavigationView navView = findViewById(R.id.nav_view_pilot);
        View headerview = navView.getHeaderView(0);

        mtv_name_user = headerview.findViewById(R.id.tv_nav_name_user);
        mtv_role_user = headerview.findViewById(R.id.tv_nav_role_user);
        mtv_email_user = headerview.findViewById(R.id.tv_nav_email_user);
        mtv_name_user.setText(name_user);
        mtv_email_user.setText(mail_user);
        mtv_role_user.setText(role_user);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout_pilot);
        NavigationView navigationView = findViewById(R.id.nav_view_pilot);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.connectDroneActivity, R.id.nav_user,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send, R.id.nav_cotdien, R.id.nav_report,
                R.id.nav_auto_check_inci, R.id.nav_data_flight_mana)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.start_app, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, getApplicationContext().MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        switch (item.getItemId()) {
            case R.id.action_logout:
                editor.clear();
                editor.commit();
                Intent i = new Intent(getApplicationContext(), LoginSystemActivity.class);
                startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
