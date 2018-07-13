package com.shivam.probussense.horizontalswip;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.shivam.probussense.Adaptor.Adaptor;
import com.shivam.probussense.Classes.HttpHandler;
import com.shivam.probussense.Classes.swimmingpools;
import com.shivam.probussense.R;
import com.shivam.probussense.Services.Mynotifyservice;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class horizontal extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private String TAG = horizontal.class.getSimpleName();

    String organizationNamestr;

    RecyclerView recyclerView;

    public static ArrayList<swimmingpools> pools=new ArrayList<>();
    TextView organizationName;

    private static int NUM_PAGES=0;


    private ViewPager mPager;

    private PagerAdapter mPagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horizontal);


        organizationName=findViewById(R.id.organizationName);

        new GetContacts().execute();

        Intent i=new Intent(horizontal.this, Mynotifyservice.class);
        startService(i);


        // Attach the view pager to the tab strip

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


























    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }


        if (mPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }


    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return new tableswipFragment(pools.get(position),horizontal.this);
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }

    }




    public class GetContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected Void doInBackground(Void... arg0) {
            pools.clear();
            HttpHandler sh = new HttpHandler();

            String user_id="";

            SharedPreferences prefs = getSharedPreferences("probussense", MODE_PRIVATE);
            user_id = prefs.getString("user_id", "");//"No name defined" is the default value.


            Log.e(TAG, "Response from sharepreferences: TABLES:: " + user_id);

            String url = "http://water.probussense.com/application/assetdata?user_id="+user_id;
            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);

            try {
                JSONObject jsonObj = new JSONObject(jsonStr);
                Boolean error=jsonObj.getBoolean("error");

                if(!error){
                    JSONArray data=jsonObj.getJSONArray("data");

                    for(int i=0;i<data.length();i++){
                        JSONObject object=data.getJSONObject(i);
                        JSONObject assests=object.getJSONObject("asset");
                        JSONObject user=object.getJSONObject("user");

                        pools.add(new swimmingpools(user.getString("organisation"),assests.getString("calling_name"),assests.getString("address"),object.getDouble("ph"),object.getDouble("cl"),object.getDouble("tp"),object.getString("dt"),assests.getDouble("asset_id")));
                        organizationNamestr=user.getString("organisation");
                    }



                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            organizationName.setText(organizationNamestr);


if(pools.size()<3){
            NUM_PAGES = pools.size();
            mPager = (ViewPager) findViewById(R.id.pager);
            mPager.setVisibility(View.VISIBLE);
            mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
            mPager.setAdapter(mPagerAdapter);
            Toast.makeText(horizontal.this, "Slide Horizontally ", Toast.LENGTH_SHORT).show();
        }
        else{
               recyclerView=findViewById(R.id.recycleview);
               recyclerView.setVisibility(View.VISIBLE);
                Adaptor adaptor=new Adaptor(horizontal.this,pools);
                recyclerView.setLayoutManager(new LinearLayoutManager(horizontal.this));
                recyclerView.setAdapter(adaptor);
        }



        }

    }

}