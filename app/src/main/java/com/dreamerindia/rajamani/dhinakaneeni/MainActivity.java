package com.dreamerindia.rajamani.dhinakaneeni;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.OnNavigationListener;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dreamerindia.rajamani.dhinakaneeni.adapters.NewsDataAdapter;
import com.dreamerindia.rajamani.dhinakaneeni.fragment.NewsFragment;
import com.dreamerindia.rajamani.dhinakaneeni.fragment.SlidingFragment;
import com.dreamerindia.rajamani.dhinakaneeni.models.RSSFeed;

import java.util.ArrayList;
import java.util.List;

//courtesy NDTV sample projects for RSS

public class MainActivity extends AppCompatActivity {

    String TITLES[] = {"அமைப்புகள்", "பதிவிறக்கம்", "எங்களை பற்றி"};
    int ICONS[] = {R.drawable.ic_action_setting, R.drawable.ic_action_offline, R.drawable.ic_action_info};

    String NAME = "தின கணினி";
    private Toolbar toolbar;                              // Declaring the Toolbar Object
    Activity activity;
    private List<RSSFeed> mRssFeedList = new ArrayList<RSSFeed>();
    RecyclerView mRecyclerView,tRecyclerView;                           // Declaring RecyclerView
    RecyclerView.Adapter mAdapter;                        // Declaring Adapter For Recycler View
    RecyclerView.LayoutManager mLayoutManager;            // Declaring Layout Manager as a linear layout manager
    DrawerLayout Drawer;                                  // Declaring DrawerLayout
    private static final int SETTINGS_RESULT = 1;
    ActionBarDrawerToggle mDrawerToggle; // Declaring Action Bar Drawer Toggle
    FragmentTransaction transaction;
    SlidingFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);


        mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView); // Assigning the RecyclerView Object to the xml View

        mRecyclerView.setHasFixedSize(true);                            // Letting the system know that the list objects are of fixed size

        mAdapter = new MyAdapter(TITLES, ICONS, NAME);       // Creating the Adapter of MyAdapter class(which we are going to see in a bit)
        // And passing the titles,icons,header view name, header view email,
        // and header view profile picture

        mRecyclerView.setAdapter(mAdapter);                              // Setting the adapter to RecyclerView


        mLayoutManager = new LinearLayoutManager(this);                 // Creating a layout Manager

        mRecyclerView.setLayoutManager(mLayoutManager);                 // Setting the layout Manager

        Drawer = (DrawerLayout) findViewById(R.id.DrawerLayout);        // Drawer object Assigned to the view
        drawer();
        if (savedInstanceState == null) {
            transaction = getSupportFragmentManager().beginTransaction();
            fragment = new SlidingFragment();
            transaction.replace(R.id.container, fragment);
            transaction.commit();
        }

        final GestureDetector mGestureDetector = new GestureDetector(MainActivity.this, new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

        });


        mRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
                View child = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());


                if (child != null && mGestureDetector.onTouchEvent(motionEvent)) {
                    Drawer.closeDrawers();
                    //      Toast.makeText(MainActivity.this, "The Item Clicked is: " + recyclerView.getChildAdapterPosition(child), Toast.LENGTH_SHORT).show();
                    if (recyclerView.getChildAdapterPosition(child) == 1) {
                        Intent i = new Intent(getApplicationContext(), UserSettingActivity.class);
                        startActivityForResult(i, SETTINGS_RESULT);
                    }
                    return true;
                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

    }

    public void drawer() {
        // Finally we set the drawer toggle sync State
        mDrawerToggle = new ActionBarDrawerToggle(this, Drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                // code here will execute once the drawer is opened( As I dont want anything happened whe drawer is
                // open I am not going to put anything here)
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                // Code here will execute once drawer is closed
            }

        }; // Drawer Toggle Object Made
        Drawer.setDrawerListener(mDrawerToggle); // Drawer Listener set to the Drawer toggle
        mDrawerToggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent i = new Intent(getApplicationContext(), UserSettingActivity.class);
                startActivityForResult(i, SETTINGS_RESULT);
//                Toast.makeText(this, "No Settings found!", Toast.LENGTH_SHORT).show();
                return (true);
            case R.id.action_refresh:
                tRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
                settingAdapter(mRecyclerView);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSupportActionModeStarted(ActionMode mode) {

    }

    @Override
    public void onSupportActionModeFinished(ActionMode mode) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SETTINGS_RESULT) {
            displayUserSettings();
        }

    }


    private void displayUserSettings() {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        String settings = "";

        settings = settings + "Password: " + sharedPrefs.getString("prefUserPassword", "NOPASSWORD");

        settings = settings + "\nRemind For Update:" + sharedPrefs.getBoolean("prefLockScreen", false);

        settings = settings + "\nUpdate Frequency: "
                + sharedPrefs.getString("prefUpdateFrequency", "NOUPDATE");

        //  TextView textViewSetting = (TextView) findViewById(R.id.textViewSettings);
        Toast.makeText(this, settings, Toast.LENGTH_LONG).show();
        //  textViewSetting.setText(settings);
    }

    public void settingAdapter(RecyclerView rv) {
        NewsDataAdapter mAdapter = new NewsDataAdapter(activity, mRssFeedList);
        int count = mRssFeedList.size();
        if (count != 0 && mAdapter != null) {
            rv.setAdapter(mAdapter);
        }
    }
}