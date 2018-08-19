package com.shivam.probussense.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.shivam.probussense.Adaptors.Adaptor;
import com.shivam.probussense.Classes.HttpHandler;
import com.shivam.probussense.Classes.swimmingpools;
import com.shivam.probussense.Fragments.Homefragment;
import com.shivam.probussense.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Tablesrecycle extends AppCompatActivity {

    public FirebaseAnalytics mFirebaseAnalytics;

    swimmingpools pool;

    TextView companyname,Swimmingpoolname,temp, phvalue,remarkph,clvalue,remarkcl,date,time,hubid;
    ImageView analytics;

    int positioninarray=-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tablesrecycle);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        positioninarray = getIntent().getIntExtra("position", -1);

        if (positioninarray == -1) {
            Toast.makeText(this, " No Data Avaliable ", Toast.LENGTH_SHORT).show();
            onBackPressed();
        } else {
            ConnectivityManager connectivityManager =(ConnectivityManager)Tablesrecycle.this.getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if (activeNetworkInfo != null) {
                new GetContacts().execute();
            }else{
                Toast.makeText(this, " Network Problem ", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }


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

            ConnectivityManager connectivityManager =(ConnectivityManager)Tablesrecycle.this.getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if (activeNetworkInfo != null) {


                String user_id = "";

                SharedPreferences prefs = Tablesrecycle.this.getSharedPreferences("probussense", Tablesrecycle.this.MODE_PRIVATE);
                user_id = prefs.getString("user_id", "");//"No name defined" is the default value.


                Log.e("TAG", "Response from sharepreferences: TABLES:: " + user_id);

                String url = "http://water.probussense.com/application/assetdata?user_id=" + user_id;
                // Making a request to url and getting response
                String jsonStr = sh.makeServiceCall(url);

                if(jsonStr!=null&&jsonStr.length()!=0){
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    Boolean error = jsonObj.getBoolean("error");

                    if (!error) {
                        JSONArray data = jsonObj.getJSONArray("data");


                            JSONObject object = data.getJSONObject(positioninarray);
                            JSONObject assests = object.getJSONObject("asset");
                            JSONObject user = object.getJSONObject("user");

                            pool=new swimmingpools(user.getString("organisation"), assests.getString("calling_name"), assests.getString("address"), object.getDouble("ph"), object.getDouble("cl"), object.getDouble("tp"), object.getString("dt"), assests.getDouble("asset_id"));



                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    onBackPressed();
                }}
                else {
                    onBackPressed();
                }
            }
            else{
                onBackPressed();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            companyname=findViewById(R.id.companyname);
            Swimmingpoolname = findViewById(R.id.fragSwimmingpoolname);
            temp = findViewById(R.id.fragtexttemp);
            phvalue = findViewById(R.id.fragtextphvalue);
            remarkph = findViewById(R.id.fragtextphremark);
            clvalue = findViewById(R.id.fragtextclvalue);
            remarkcl = findViewById(R.id.fragtextclremark);
            date = findViewById(R.id.fragtextdate);
            time = findViewById(R.id.fragtexttime);
            hubid = findViewById(R.id.fragtexthubid);

            analytics = findViewById(R.id.fraganalytics);

            companyname.setText(pool.getOrganizationname());
            Swimmingpoolname.setText(pool.getName());
            phvalue.setText(String.valueOf(pool.getPh()));
            clvalue.setText(String.valueOf(pool.getChlorine()));

            temp.setText(String.valueOf("T=" + pool.getTemp() + "C"));
            hubid.setText(String.valueOf(pool.getHubid()));
            date.setText(pool.getDatetime().substring(0, 10));
            time.setText(pool.getDatetime().substring(11, 16));


            if (pool.getPh() < 7.2 || pool.getPh() > 8.0) {
                phvalue.setTextColor(Color.RED);
                remarkph.setText("Poor");
                remarkph.setTextColor(Color.RED);
            } else {
                phvalue.setTextColor(Color.rgb(24, 124, 46));
                remarkph.setText("Satisfactory");
                remarkph.setTextColor(Color.rgb(24, 124, 46));
            }


            if (pool.getChlorine() < 1.0 || pool.getChlorine() > 3.0) {
                clvalue.setTextColor(Color.RED);
                remarkcl.setText("Poor");
                remarkcl.setTextColor(Color.RED);

            } else {
                clvalue.setTextColor(Color.rgb(24, 124, 46));
                remarkcl.setText("Satisfactory");
                remarkcl.setTextColor(Color.rgb(24, 124, 46));
            }

            analytics.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(Tablesrecycle.this, Charts.class);
                    i.putExtra("assert_id", pool.getHubid());
                    Tablesrecycle.this.startActivity(i);
                }
            });

        }




    }


}
