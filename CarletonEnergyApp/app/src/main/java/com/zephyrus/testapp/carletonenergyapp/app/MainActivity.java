package com.zephyrus.testapp.carletonenergyapp.app;
import java.util.Locale;


import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity implements ActionBar.TabListener,  OnButtonClickedListener{

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;
    Boolean isSyncing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }

        //jumps to a specified tab
        if(savedInstanceState == null){
            Bundle extras = getIntent().getExtras();
            if(extras!=null){
                setCurrentView(extras.getInt("jumpToTab"));
            }
        }

        //sets isSyncing to false
        isSyncing = false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_sync) {
            manualSync();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {}
    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {}

    //sets the current tab to display (jump-to or on instantiate)
    public void setCurrentView(int viewNum){
        mViewPager.setCurrentItem(viewNum);
    }

    //calls the datasource to sync
    public void manualSync(){
        if(!isSyncing) {
            Toast.makeText(this, "Syncing...", Toast.LENGTH_SHORT).show();
            isSyncing = true;
            CarletonEnergyDataSource.getSingleton().sync();
            isSyncing = false;
            Toast.makeText(this, "Sync complete!", Toast.LENGTH_SHORT).show();
            mViewPager.invalidate();
        }
        else{
            Toast.makeText(this, "Still syncing...", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onButtonClicked() {
        Intent intent = new Intent(this, MainActivity.class);
        int jumpToSettings = 3;
        intent.putExtra("jumpToTab",jumpToSettings);
        startActivity(intent);
        finish();
        }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public Fragment getItem(int position) {
            //creates the four different fragments associated with tabs
            switch (position) {
                case 0:
                    return new LiveFragment();
                case 1:
                    return new HistoricFragment();
                case 2:
                    return new InfoFragment();
                case 3:
                    return new SettingsFragment();
            }
            return null;
        }

        //4 tabs
        @Override
        public int getCount() { return 4;}

        //generates tab titles
        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_wind).toUpperCase();
                case 1:
                    return getString(R.string.title_data).toUpperCase();
                case 2:
                    return getString(R.string.title_info).toUpperCase();
                case 3:
                    return getString(R.string.title_settings).toUpperCase();
            }
            return null;
        }

        //garbage collect
        public void destroyItem(ViewGroup collection, int position, Object o){
           o = null;
        }
    }
}
