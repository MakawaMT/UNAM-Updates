package com.inc.automata.unamupdates.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewConfiguration;
import android.widget.Toast;

import com.inc.automata.unamupdates.R;
import com.inc.automata.unamupdates.adapters.PagerAdapter;
import com.inc.automata.unamupdates.appconstants.AppConst;
import com.inc.automata.unamupdates.appconstants.NewsAlertDialog;
import com.inc.automata.unamupdates.fragments.CourseNewsFragment;
import com.inc.automata.unamupdates.fragments.GeneralNewsFragment;

import java.lang.reflect.Field;
/*
Project by Manfred T Makawa
University of Namibia
201201453
Computer Science and Information Technology
 */
public class MainActivity extends AppCompatActivity implements GeneralNewsFragment.OnFragmentInteractionListener,CourseNewsFragment.OnCourseNewsFragmentInteractionListener,TabLayout.OnTabSelectedListener{
    public static String TAG=MainActivity.class.getSimpleName();
    public static String cheatMessage,cheatTitle;
    Toolbar toolBar;
    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    protected void onNewIntent(Intent intent) {
        Log.d(TAG+" onNewIntent","Triggered onNewIntent");
        super.onNewIntent(intent);
        //access data sent from notification
           setIntent(intent);

        try{
            NewsAlertDialog dialog = NewsAlertDialog.newInstance(cheatMessage,cheatTitle);
            dialog.show(getSupportFragmentManager(),TAG);
        }catch(Exception ex)
        {
            Log.e(TAG,"cheat stuff error displaying alert");}

        try {

            Bundle stuff = getIntent().getExtras();
            if (stuff.getString("message") != null) {
                Toast.makeText(this, "message: " + stuff.getString("message"), Toast.LENGTH_SHORT).show();
                Log.d(TAG,"message "+stuff.getString("message"));
                NewsAlertDialog dialog = NewsAlertDialog.newInstance(stuff.getString("message"),stuff.getString("title"));
                dialog.show(getSupportFragmentManager(),TAG);
            } else {
                Log.d(TAG,"nothing here onNewIntent");
            }

        }catch(Exception ex)
        {
            Log.e(TAG,"empty bundle: "+ex.getMessage());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //if push notification was received then show dialog
        if(cheatMessage!=null && cheatTitle !=null) {
            try {
                onNewIntent(getIntent());
            } catch (Exception ex) {
                Log.e(TAG, "calling onNewIntent " + ex.getMessage());
            }
        }
        //toolbar initilisation etc
        toolBar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);
        toolBar.setLogo(R.mipmap.ic_launcher);

        //tablayout initilisation etc
        tabLayout=(TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getText(R.string.fragment_general_news_name)));
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getText(R.string.fragment_course_news_name)));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        //viewpager,pager adapter initilisation etc
      viewPager=(ViewPager)findViewById(R.id.pager);
      final PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager(),tabLayout.getTabCount());
      viewPager.setAdapter(pagerAdapter);

      viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
      tabLayout.setOnTabSelectedListener(this);

      //force overflow menu on all devices
        forceOverFlowMenu();

    }
    private void forceOverFlowMenu() {
        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");

            if (menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        }
        catch (Exception e) {
            // presumably, not relevant
        }
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

        switch(item.getItemId()) {
            case R.id.action_settings:
                //start up the settings activity
                Intent settingsIntent = new Intent(MainActivity.this,SettingsActivity.class);
                settingsIntent.putExtra(AppConst.ACTIVITY_NAME,TAG);
                startActivity(settingsIntent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onGeneralNewsFragmentInteraction(Uri uri) {

    }

    @Override
    public void onCourseNewsFragmentInteraction(Uri uri) {

    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
