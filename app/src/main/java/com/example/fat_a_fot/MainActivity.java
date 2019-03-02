package com.example.fat_a_fot;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    TextView username;
    TextView mobile;
    private SessionManager session;
    private SQLLiteHandler db;
    @SuppressLint({"RestrictedApi", "ResourceAsColor"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new SQLLiteHandler(getApplicationContext());
        session = new SessionManager(getApplicationContext());
        if (savedInstanceState == null) {
            Fragment newFragment = new HomeFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame,newFragment);
            ft.addToBackStack(null);
            ft.commit();
        }
        if (Detectconnection.checkInternetConnection(this)) {
            if (Common.getSavedUserData(MainActivity.this, "mobile").equalsIgnoreCase("")) {
                Intent intent = new Intent(this, Signup.class);
                startActivity(intent);
            }

        } else {
            Toast.makeText(this, "Check Internet Connection.", Toast.LENGTH_LONG).show();
            Intent noconnection = new Intent(MainActivity.this, NoInternetConnectionActivity.class);
            startActivity(noconnection);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Fat-A-Fot");
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.hide();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        username = (TextView) headerView.findViewById(R.id.name);
        mobile = (TextView) headerView.findViewById(R.id.mobile);
        mobile.setText(Common.getSavedUserData(MainActivity.this, "mobile"));
//        if(! Common.getSavedUserData(MainActivity.this, "name").isEmpty()){
//            username.setText(Common.getSavedUserData(MainActivity.this, "name"));
//        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    /*public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }*/
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_profile) {
            Fragment newFragment = new ProfileFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame,newFragment);
            ft.commit();
        }else if (id == R.id.nav_home) {
            Fragment newFragment = new HomeFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame,newFragment);
            ft.commit();
        } else if (id == R.id.nav_Myorder) {
            Fragment newFragment = new MyOrderFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame,newFragment);
            ft.commit();
        }else if (id == R.id.nav_Mycart) {
            Fragment newFragment = new UserCartFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame,newFragment);
            ft.commit();
        } else if (id == R.id.nav_logout) {
            clearApplicationData();
        } else if (id == R.id.nav_chat)
        {
            try {
                /*String url="=";*/
                String no = "+918374042767";
                String text = "Hello Admin";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://api.whatsapp.com/send?phone=" + no + "&text=" + text));
                startActivity(i);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        } else if (id == R.id.nav_contactus) {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:9182901719"));
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void clearApplicationData() {
        File cacheDirectory = getCacheDir();
        File applicationDirectory = new File(cacheDirectory.getParent());
        Common.saveUserData(MainActivity.this, "email", "");
        Common.saveUserData(MainActivity.this, "userId", "");
        Common.saveUserData(MainActivity.this, "name", "");
        Common.saveUserData(MainActivity.this, "mobile", "");
        Common.saveUserData(MainActivity.this, "image", "");
        Common.saveUserData(MainActivity.this, "temp_mobile","");
        if (applicationDirectory.exists()) {
            String[] fileNames = applicationDirectory.list();
            for (String fileName : fileNames) {
                if (!fileName.equals("lib")) {
                    deleteFile(new File(applicationDirectory, fileName));
                    Intent login = new Intent(MainActivity.this,Signup.class);
                    startActivity(login);
                }
            }
        }
    }

    public static boolean deleteFile(File file) {
        boolean deletedAll = true;
        if (file != null) {
            if (file.isDirectory()) {
                String[] children = file.list();
                for (int i = 0; i < children.length; i++) {
                    deletedAll = deleteFile(new File(file, children[i])) && deletedAll;
                }
            } else {
                deletedAll = file.delete();
            }
        }
        return deletedAll;
    }
}
