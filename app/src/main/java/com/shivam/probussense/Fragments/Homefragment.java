package com.shivam.probussense.Fragments;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.analytics.FirebaseAnalytics;
import com.shivam.probussense.Activities.login;
import com.shivam.probussense.Adaptors.Adaptor;
import com.shivam.probussense.Classes.HttpHandler;
import com.shivam.probussense.Classes.swimmingpools;
import com.shivam.probussense.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class Homefragment extends Fragment {

    private FirebaseAnalytics mFirebaseAnalytics;

    View rootview;

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    private String TAG = Homefragment.class.getSimpleName();

    String organizationNamestr;

    RecyclerView recyclerView;

    public static ArrayList<swimmingpools> pools=new ArrayList<>();

    TextView organizationName;

    private static int NUM_PAGES=0;


    private ViewPager mPager;

    private PagerAdapter mPagerAdapter;

    ProgressBar progressBartable;

    Context context;
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public Homefragment() {
        // Required empty public constructor
    }


    @SuppressLint("ValidFragment")
    public Homefragment(Context context) {
        this.context=context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);

        // Inflate the layout for this fragment
        rootview= inflater.inflate(R.layout.fragment_homefragment, container, false);

        organizationName=rootview.findViewById(R.id.organizationName);

        progressBartable=rootview.findViewById(R.id.progressbartable);

        ConnectivityManager connectivityManager =(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo != null) {
            new GetContacts().execute();

        }else{
            Toast.makeText(context, "No Internet Avaliable", Toast.LENGTH_SHORT).show();
        }





        return rootview;}



    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }



        @Override
        public Fragment getItem(int position) {
            return new tableswipFragment(pools.get(position),context);
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

            ConnectivityManager connectivityManager =(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if (activeNetworkInfo != null) {


                String user_id = "";

                SharedPreferences prefs = context.getSharedPreferences("probussense", context.MODE_PRIVATE);
                user_id = prefs.getString("user_id", "");//"No name defined" is the default value.


                Log.e(TAG, "Response from sharepreferences: TABLES:: " + user_id);

                String url = "http://water.probussense.com/application/assetdata?user_id=" + user_id;
                // Making a request to url and getting response
                String jsonStr = sh.makeServiceCall(url);

                if(jsonStr!=null){
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    Boolean error = jsonObj.getBoolean("error");

                    if (!error) {
                        JSONArray data = jsonObj.getJSONArray("data");

                        for (int i = 0; i < data.length(); i++) {
                            JSONObject object = data.getJSONObject(i);
                            JSONObject assests = object.getJSONObject("asset");
                            JSONObject user = object.getJSONObject("user");

                            pools.add(new swimmingpools(user.getString("organisation"), assests.getString("calling_name"), assests.getString("address"), object.getDouble("ph"), object.getDouble("cl"), object.getDouble("tp"), object.getString("dt"), assests.getDouble("asset_id")));
                            organizationNamestr = user.getString("organisation");
                        }


                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Intent i=new Intent(context,login.class);
                    startActivity(i);
                }
            }else{
                    Intent i=new Intent(context,login.class);
                    startActivity(i);
            }}
            else{
                Intent i=new Intent(context,login.class);
                startActivity(i);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            progressBartable.setVisibility(View.GONE);

            super.onPostExecute(result);
            organizationName.setText(organizationNamestr);


            if(pools.size()<=3){
                NUM_PAGES = pools.size();
                mPager = (ViewPager)rootview. findViewById(R.id.pager);
                mPager.setVisibility(View.VISIBLE);

                FragmentManager fragmentManager=getFragmentManager();

                mPagerAdapter = new ScreenSlidePagerAdapter(fragmentManager);
                mPager.setAdapter(mPagerAdapter);
                Toast.makeText(context, "Slide Horizontally ", Toast.LENGTH_SHORT).show();
            }
            else{
                recyclerView=rootview.findViewById(R.id.recycleview);
                recyclerView.setVisibility(View.VISIBLE);
                Adaptor adaptor=new Adaptor(context,pools);
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
                recyclerView.setAdapter(adaptor);
            }



        }

    }

}
