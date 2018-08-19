package com.shivam.probussense.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crash.FirebaseCrash;
import com.shivam.probussense.Classes.HttpHandler;
import com.shivam.probussense.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class Charts extends AppCompatActivity {

    public FirebaseAnalytics mFirebaseAnalytics;
    //
    BarChart chartsub ;
    ArrayList<BarEntry> BARENTRYsub ;
    ArrayList<String> BarEntryLabelssub ;
    BarDataSet Bardatasetsub ;
    BarData BARDATAsub ;

    int []weeks1=new int[7];

    int type=0;
    //

    BarChart chart ;
    ArrayList<BarEntry> BARENTRY ;
    ArrayList<String> BarEntryLabels ;
    BarDataSet Bardataset ;
    BarData BARDATA ;

    double hubid;
    int []weeks=new int[5];

    Button week1,week2,week3,week4,maingraph;

    TextView graphheading;

    LinearLayout maingraphbtnlayout,weekgraphbthlayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charts);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);


        hubid=getIntent().getDoubleExtra("assert_id",0);

        Log.e("TAG", "onCreate: "+hubid );


        week1=findViewById(R.id.btnweek1);
        week2=findViewById(R.id.btnweek2);
        week3=findViewById(R.id.btnweek3);
        week4=findViewById(R.id.btnweek4);
        maingraph=findViewById(R.id.btnmaingraph);

        maingraphbtnlayout=findViewById(R.id.layoutmaingraphbtn);
        weekgraphbthlayout=findViewById(R.id.layoutweekgraphbtn);

        graphheading=findViewById(R.id.graphheading);


        new GetContacts().execute();

        week1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ConnectivityManager connectivityManager =(ConnectivityManager)getApplicationContext().getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);
                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                if (activeNetworkInfo != null) {
                    type = 0;
                    new GetContactsSUBCHART().execute();
                }else{
                    Toast.makeText(Charts.this, "No Internet Avaliable", Toast.LENGTH_SHORT).show();
                }
            }
        });

        week2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ConnectivityManager connectivityManager =(ConnectivityManager)getApplicationContext().getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);
                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                if (activeNetworkInfo != null) {
                type=1;
                new GetContactsSUBCHART().execute();
            }else{
                    Toast.makeText(Charts.this, "No Internet Avaliable", Toast.LENGTH_SHORT).show();
                }
            }
        });

        week3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ConnectivityManager connectivityManager =(ConnectivityManager)getApplicationContext().getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);
                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                if (activeNetworkInfo != null) {
                type=2;
                new GetContactsSUBCHART().execute();
            }else{
                    Toast.makeText(Charts.this, "No Internet Avaliable", Toast.LENGTH_SHORT).show();
                }
            }
        });

        week4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager connectivityManager =(ConnectivityManager)getApplicationContext().getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);
                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                if (activeNetworkInfo != null) {
                    type = 3;
                    new GetContactsSUBCHART().execute();
                }else{
                    Toast.makeText(Charts.this, "No Internet Avaliable", Toast.LENGTH_SHORT).show();
                }
            }
        });

        maingraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager connectivityManager =(ConnectivityManager)getApplicationContext().getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);
                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                if (activeNetworkInfo != null) {
                new GetContacts().execute();
            }else{
                    Toast.makeText(Charts.this, "No Internet Avaliable", Toast.LENGTH_SHORT).show();
                }
            }
        });



    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();



    }

    private class GetContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            chartsub = (BarChart) findViewById(R.id.subchart1);
            chartsub.setVisibility(View.GONE);
            maingraphbtnlayout.setVisibility(View.GONE);
            weekgraphbthlayout.setVisibility(View.VISIBLE);
            graphheading.setText("Number of variation from standard values \n (Last 4 weeks)");

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            ConnectivityManager connectivityManager =(ConnectivityManager)getApplicationContext().getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if (activeNetworkInfo != null){



            String url = "http://water.probussense.com/application/chart?asset_id="+hubid;
            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);

            if(jsonStr!=null&&jsonStr.length()!=0){
            try {
                JSONArray jsonObj = new JSONArray(jsonStr);



                for (int i = 0; i < jsonObj.length(); i++) {
                    JSONArray array1 = jsonObj.getJSONArray(i);
                    weeks[i] = array1.length();




                }

            } catch (JSONException e) {
                e.printStackTrace();
                onBackPressed();
            }
            }}else{
                onBackPressed();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);



            chart = (BarChart) findViewById(R.id.chart1);
            chart.setVisibility(View.VISIBLE);

            chart.setDescriptionColor(Color.WHITE);


            chart.setDrawGridBackground(false);
            chart.setDrawValueAboveBar(true);
            chart.setGridBackgroundColor(Color.WHITE);
            chart.setBorderColor(Color.WHITE);


            XAxis xaxis=chart.getXAxis();
            xaxis.setDrawGridLines(false);
            xaxis.setPosition(XAxis.XAxisPosition.BOTTOM);

            YAxis left=chart.getAxisLeft();// no axis labels
           // left.setDrawGridLines(false); // no grid lines
            left.setDrawZeroLine(true); // draw a zero line
            chart.getAxisRight().setEnabled(false);




            BARENTRY = new ArrayList<>();

            BarEntryLabels = new ArrayList<String>();

            AddValuesToBARENTRY();

            AddValuesToBarEntryLabels();

            Bardataset = new BarDataSet(BARENTRY, "NUMBER OF TIMES THRESHOLD VALUE HITS");

            BARDATA = new BarData(BarEntryLabels, Bardataset);

            Bardataset.setColor(Color.rgb(51,188,232));
            Bardataset.setBarSpacePercent(50);
            Bardataset.setDrawValues(false);

            chart.setData(BARDATA);

            chart.animateY(3000);

        }

        public void AddValuesToBARENTRY(){

            BARENTRY.add(new BarEntry(weeks[0], 0));
            BARENTRY.add(new BarEntry(weeks[1], 1));
            BARENTRY.add(new BarEntry(weeks[2], 2));
            BARENTRY.add(new BarEntry(weeks[3], 3));

        }

        public void AddValuesToBarEntryLabels(){

            BarEntryLabels.add("WEEK 1");
            BarEntryLabels.add("WEEK 2");
            BarEntryLabels.add("WEEK 3");
            BarEntryLabels.add("WEEK 4");


        }

    }






    private class GetContactsSUBCHART extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            chart = (BarChart) findViewById(R.id.chart1);
            chart.setVisibility(View.GONE);

            chartsub = (BarChart) findViewById(R.id.subchart1);
            chartsub.setVisibility(View.GONE);
            maingraphbtnlayout.setVisibility(View.VISIBLE);
            weekgraphbthlayout.setVisibility(View.GONE);
            graphheading.setText("Average time taken to retain standard values ");

        }


        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            weeks1[0]=0;
            weeks1[1]=0;
            weeks1[2]=0;
            weeks1[3]=0;
            weeks1[4]=0;
            weeks1[5]=0;
            weeks1[6]=0;


            ConnectivityManager connectivityManager =(ConnectivityManager)getApplicationContext().getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if (activeNetworkInfo != null) {


                String url = "http://water.probussense.com/application/chart?asset_id=" + hubid;
                // Making a request to url and getting response
                String jsonStr = sh.makeServiceCall(url);

                if(jsonStr!=null&&jsonStr.length()!=0){
                try {
                    JSONArray jsonObj = new JSONArray(jsonStr);

                    JSONArray array1 = jsonObj.getJSONArray(type);

                    for (int j = 0; j < array1.length(); j++) {

                        JSONObject object = array1.getJSONObject(j);

                        String dt = object.getString("dt");


                        int val = Integer.valueOf(dt.substring(8, 10));


                        String time = object.getString("duration");


                        switch ((val % 7)) {
                            case 0:
                                weeks1[0] += Integer.valueOf(time.substring(0, time.length() - 7));
                                break;

                            case 1:
                                weeks1[1] += Integer.valueOf(time.substring(0, time.length() - 7));
                                break;

                            case 2:
                                weeks1[2] += Integer.valueOf(time.substring(0, time.length() - 7));
                                break;

                            case 3:
                                weeks1[3] += Integer.valueOf(time.substring(0, time.length() - 7));
                                break;

                            case 4:
                                weeks1[4] += Integer.valueOf(time.substring(0, time.length() - 7));
                                break;

                            case 5:
                                weeks1[5] += Integer.valueOf(time.substring(0, time.length() - 7));
                                break;

                            case 6:
                                weeks1[6] += Integer.valueOf(time.substring(0, time.length() - 7));
                                break;
                        }

                    }


                } catch (JSONException e) {

                    e.printStackTrace();
                    onBackPressed();
                }
            }}else{
                onBackPressed();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);


            chartsub = (BarChart) findViewById(R.id.subchart1);
            chartsub.setVisibility(View.VISIBLE);



            chartsub.setDescriptionColor(Color.WHITE);


            chartsub.setDrawGridBackground(false);
            chartsub.setDrawValueAboveBar(true);
            chartsub.setGridBackgroundColor(Color.WHITE);
            chartsub.setBorderColor(Color.WHITE);

            XAxis xaxis=chartsub.getXAxis();
            xaxis.setDrawGridLines(false);
            xaxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xaxis.setSpaceBetweenLabels(0);

            YAxis left=chartsub.getAxisLeft();// no axis labels
            left.setDrawZeroLine(true); // draw a zero line
            chartsub.getAxisRight().setEnabled(false);


            BARENTRYsub = new ArrayList<>();

            BarEntryLabelssub = new ArrayList<String>();

            AddValuesToBARENTRY();

            AddValuesToBarEntryLabels();



            Bardatasetsub = new BarDataSet(BARENTRYsub, "DURATION IN MINUTES");

            BARDATAsub = new BarData(BarEntryLabelssub, Bardatasetsub);

            Bardatasetsub.setColor(Color.rgb(51, 188, 232));
            Bardatasetsub.setBarSpacePercent(30);
            chartsub.setData(BARDATAsub);

            chartsub.animateY(1500);


        }

        public void AddValuesToBARENTRY() {

            BARENTRYsub.add(new BarEntry(weeks1[0], 0));
            BARENTRYsub.add(new BarEntry(weeks1[1], 1));
            BARENTRYsub.add(new BarEntry(weeks1[2], 2));
            BARENTRYsub.add(new BarEntry(weeks1[3], 3));
            BARENTRYsub.add(new BarEntry(weeks1[4], 4));
            BARENTRYsub.add(new BarEntry(weeks1[5], 5));
            BARENTRYsub.add(new BarEntry(weeks1[6], 6));

        }

        public void AddValuesToBarEntryLabels() {


            String[] name=new String[7];

            Calendar cal = Calendar.getInstance();
            SimpleDateFormat s = new SimpleDateFormat("MMM d");
int currentday=cal.get(Calendar.DAY_OF_WEEK);

                cal.add(Calendar.DAY_OF_YEAR, -(currentday)-(7*type)+1);
                name[0]=s.format(new Date(cal.getTimeInMillis()));


                cal.add(Calendar.DAY_OF_YEAR, 1);
                name[1]=s.format(new Date(cal.getTimeInMillis()));


                cal.add(Calendar.DAY_OF_YEAR, 1);
                name[2]=s.format(new Date(cal.getTimeInMillis()));


                cal.add(Calendar.DAY_OF_YEAR, 1);
                name[3]=s.format(new Date(cal.getTimeInMillis()));


                cal.add(Calendar.DAY_OF_YEAR, 1);
                name[4]=s.format(new Date(cal.getTimeInMillis()));


                cal.add(Calendar.DAY_OF_YEAR, 1);
                name[5]=s.format(new Date(cal.getTimeInMillis()));


                cal.add(Calendar.DAY_OF_YEAR, 1);
                name[6]=s.format(new Date(cal.getTimeInMillis()));


            BarEntryLabelssub.add(name[0]);
            BarEntryLabelssub.add(name[1]);
            BarEntryLabelssub.add(name[2]);
            BarEntryLabelssub.add(name[3]);
            BarEntryLabelssub.add(name[4]);
            BarEntryLabelssub.add(name[5]);
            BarEntryLabelssub.add(name[6]);

        }

    }
}
