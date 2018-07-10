package com.shivam.probussense.horizontalswip;


import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.shivam.probussense.Classes.HttpHandler;
import com.shivam.probussense.Classes.swimmingpools;
import com.shivam.probussense.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class horizontal extends AppCompatActivity {

    private String TAG = horizontal.class.getSimpleName();

    String organizationNamestr;

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

    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
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

                        pools.add(new swimmingpools(assests.getString("calling_name"),assests.getString("address"),object.getDouble("ph"),object.getDouble("cl"),object.getDouble("tp"),object.getString("dt"),assests.getDouble("asset_id")));
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
             NUM_PAGES = pools.size();
            mPager = (ViewPager) findViewById(R.id.pager);
            mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
            mPager.setAdapter(mPagerAdapter);
        }

    }

}
