package com.writm.writm;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.writm.R;
import com.writm.service.PostService;

import Utils.Preference;
import Utils.TypefaceSpan;
import Utils.TypefaceUtil;
import fragment.AboutFragment;
import fragment.ComposeFragment;
import fragment.ExploreFragment;
import fragment.HomeFragment;
import fragment.PrivacyFragment;
import fragment.ProfileFragment;
import fragment.SearchFragment;


public class NavigationDrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    NavigationView navigationView = null;
    Toolbar toolbar = null;
    ProgressBar progressBar;
    View outer;
    private int navigationPosition= R.id.navigation_home;
    private boolean doubleBackToExitPressedOnce = false;
    private int STORAGE_PERMISSION_CODE=10;

    @Override
    protected void onResume() {
        super.onResume();

        Log.v("NAVIGATION","ONRESUMECALLED");
//        displaySelectedscreen(navigationPosition);
        Log.v("AUTHDETAILS",new Preference(this).getUserid());
        registerReceiver(receiver, new IntentFilter(
                PostService.NOTIFICATION));

   //     displaySelectedscreen(navigationPosition);

    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.v("NAVIGATION","ONSTARTCALLED");
        displaySelectedscreen(navigationPosition);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.v("NAVIGATION","ONCREATECALLED");

        setContentView(R.layout.activity_navigation_drawer);
        TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "josephregular.ttf");
        outer=  findViewById(R.id.outer);
        progressBar= (ProgressBar) outer.findViewById(R.id.progressBarCircularIndeterminate);

        if(getIntent().getExtras()!=null)
            navigationPosition=getIntent().getExtras().getInt("id");
        else
            navigationPosition=R.id.navigation_home;

        if(getIntent().getExtras().getString("progress")!=null)
        {
            progressBar.setVisibility(View.VISIBLE);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    progressBar.setVisibility(View.GONE);
                    displaySelectedscreen(navigationPosition);
                    if(!isWriteStorageAllowed())
                        requestStoragePermission();
                }
            }, 3000);
            //
        }
        else if(getIntent().getExtras().getString("post_id")!=null || getIntent().getExtras().getString("auth_id")!=null)
        {
            if(getIntent().getExtras().getString("post_id")!=null)
            {
                Intent intent  = new Intent(NavigationDrawerActivity.this,NotificationsTales.class);
                intent.putExtra("post_id",getIntent().getExtras().getString("post_id"));
                startActivity(intent);
            }
            else
            {
                Intent intent  = new Intent(NavigationDrawerActivity.this,ProfileActivity.class);
                intent.putExtra("auth_id",getIntent().getExtras().getString("auth_id"));
                startActivity(intent);
            }

        }
        else
        {
           // displaySelectedscreen(navigationPosition);
            if(!isWriteStorageAllowed())
                requestStoragePermission();
        }
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View hView =  navigationView.getHeaderView(0);
        TextView name = (TextView)hView.findViewById(R.id.user_name_navigation);
        String temp1;
        Logger.addLogAdapter(new AndroidLogAdapter());
      temp1=FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
      Logger.i("Name->" + temp1);
        temp1=temp1.substring(0,1).toUpperCase()+temp1.substring(1);
        SpannableString s = new SpannableString(temp1);
        s.setSpan(new TypefaceSpan( this, "josephregular.ttf"), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        name.setText(s);
        temp1="" + FirebaseAuth.getInstance().getCurrentUser().getEmail();
        TextView email = (TextView)hView.findViewById(R.id.user_email_navigation);
        Logger.i("Name->" + FirebaseAuth.getInstance().getCurrentUser().getEmail());
        SpannableString s1 = new SpannableString(temp1);
        Logger.i("SpannableString->" + s1);
        s1.setSpan(new TypefaceSpan( this, "josephregular.ttf"), 0, s1.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        email.setText(s1);
    }

    @Override
    public void onBackPressed() {
        Log.v("NAVIGATIONID", String.valueOf(navigationPosition));
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        if (navigationPosition == R.id.navigation_home || navigationPosition==R.id.navigation_explore)
        {
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce=false;
                }
            }, 2000);
        }



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }

        else if(getSupportFragmentManager().getBackStackEntryCount() > 0)
        {
            getSupportFragmentManager().popBackStack();
            Log.v("BACKPRESS","ONBACKPRESS CALLED");
        }
        else if(navigationPosition == R.id.navigation_explore)
        {
            displaySelectedscreen(R.id.navigation_explore);
            getSupportFragmentManager().popBackStack();
        }
        else
        {
            navigationPosition=R.id.navigation_home;
                displaySelectedscreen(R.id.navigation_home);
               getSupportFragmentManager().popBackStack();
        }

        //moveTaskToBack(true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_drawer, menu);
        return true;
    }
     public  void displaySelectedscreen(int id)


    {
        if(getSupportFragmentManager().getBackStackEntryCount() >0)
        {
            Log.v("RECREATE","RECREATECALLEDPOPBACKSTACK");
            getSupportFragmentManager().popBackStack();
        }

        navigationPosition=id;
        Fragment selectedFragment = null;
        switch (id) {
            case R.id.navigation_home:
                selectedFragment = HomeFragment.newInstance();
                break;
            case R.id.navigation_write:
                selectedFragment= ComposeFragment.newInstance();
                break;
            case R.id.navigation_explore:
                selectedFragment = ExploreFragment.newInstance();
                break;
            case R.id.navigation_search:
               selectedFragment= SearchFragment.newInstance();
                break;
            case R.id.navigation_profile:
               selectedFragment= ProfileFragment.newInstance();
                break;
            case R.id.navigation_about:
                selectedFragment = AboutFragment.newInstance();
                break;
            case R.id.contact_us:
                selectedFragment= PrivacyFragment.newInstance();
                break;
            case R.id.log_out:

                FirebaseAuth.getInstance().signOut();
                LoginManager.getInstance().logOut();
                Intent intent1 = new Intent(getApplicationContext(),PreviewActivity.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent1);
                finish();


                break;
            default:
                selectedFragment = HomeFragment.newInstance();
                break;
        }
        if(selectedFragment != null)
        {

            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container,selectedFragment);
           // fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Log.v("ITEMID", String.valueOf(id));
        navigationPosition=id;
        displaySelectedscreen(id);
        return true;
    }


    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {

                int resultCode = bundle.getInt(PostService.RESULT);
                if (resultCode == RESULT_OK) {
                    AlertDialog.Builder builder;

                        builder = new AlertDialog.Builder(context);
                    builder.setTitle("Tale Posted")
                            .setPositiveButton("View now", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                    displaySelectedscreen(R.id.navigation_explore);
                                    dialog.dismiss();
                                }
                            })
                            .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                    dialog.dismiss();
                                }
                            }
                            )
                            .show();
                } else {
                    Toast.makeText(NavigationDrawerActivity.this, "Tale Posting failed",
                            Toast.LENGTH_LONG).show();
                }
            }
        }
    };

    private boolean isWriteStorageAllowed() {

        int result = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (result == PackageManager.PERMISSION_GRANTED)
            return true;

        return false;
    }

    private void requestStoragePermission(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            Toast.makeText(this,"Write Permission is required for sharing and downloading", Toast.LENGTH_SHORT).show();
        }

        ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},STORAGE_PERMISSION_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final Fragment permissionFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        permissionFragment.onActivityResult(requestCode,resultCode,data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==10)
        {
            if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED)
            {

            }

            else
            {
                Toast.makeText(this,"Oops! you just denied the permission sharing and downloading will not work", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            final Fragment permissionFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            permissionFragment.onRequestPermissionsResult(requestCode,permissions,grantResults);
        }

    }
}
