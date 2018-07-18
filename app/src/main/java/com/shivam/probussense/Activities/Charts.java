package com.shivam.probussense.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.shivam.probussense.R;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.shivam.probussense.Classes.HttpHandler;
import com.shivam.probussense.horizontalswip.horizontal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Charts extends AppCompatActivity {


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charts);

        hubid=getIntent().getDoubleExtra("assert_id",0);

        Log.e("TAG", "onCreate: "+hubid );


        week1=findViewById(R.id.btnweek1);
        week2=findViewById(R.id.btnweek2);
        week3=findViewById(R.id.btnweek3);
        week4=findViewById(R.id.btnweek4);
        maingraph=findViewById(R.id.btnmaingraph);


        new GetContacts().execute();

        week1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type=0;
                new GetContactsSUBCHART().execute();

            }
        });

        week2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                type=1;
                new GetContactsSUBCHART().execute();
            }
        });

        week3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type=2;
                new GetContactsSUBCHART().execute();
            }
        });

        week4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type=3;
                new GetContactsSUBCHART().execute();

            }
        });

        maingraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GetContacts().execute();
            }
        });



    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i=new Intent(Charts.this,horizontal.class);
        startActivity(i);
        finish();



    }

    private class GetContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            chartsub = (BarChart) findViewById(R.id.subchart1);
            chartsub.setVisibility(View.GONE);

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();



            String url = "http://water.probussense.com/application/chart?asset_id="+hubid;
            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);

            try {
                JSONArray jsonObj = new JSONArray(jsonStr);



    for (int i = 0; i < jsonObj.length(); i++) {
        JSONArray array1 = jsonObj.getJSONArray(i);
        weeks[i] = array1.length();




}

} catch (JSONException e) {
                e.printStackTrace();
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
            chart.setDrawValueAboveBar(false);
            chart.setGridBackgroundColor(Color.WHITE);
            chart.setBorderColor(Color.WHITE);

            XAxis xaxis=chart.getXAxis();
            xaxis.setDrawGridLines(false);
            xaxis.setPosition(XAxis.XAxisPosition.BOTTOM);

            YAxis left=chart.getAxisLeft();// no axis labels
            left.setDrawGridLines(false); // no grid lines
            left.setDrawZeroLine(true); // draw a zero line
            chart.getAxisRight().setEnabled(false);




            BARENTRY = new ArrayList<>();

            BarEntryLabels = new ArrayList<String>();

            AddValuesToBARENTRY();

            AddValuesToBarEntryLabels();

            Bardataset = new BarDataSet(BARENTRY, "NUMBER OF TIMES THRESHOLD VALUE HITS");

            BARDATA = new BarData(BarEntryLabels, Bardataset);

            Bardataset.setColor(Color.rgb(19,138,176));
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

            String url = "http://water.probussense.com/application/chart?asset_id=" + hubid;
            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);

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
            chartsub.setDrawValueAboveBar(false);
            chartsub.setGridBackgroundColor(Color.WHITE);
            chartsub.setBorderColor(Color.WHITE);

            XAxis xaxis=chartsub.getXAxis();
            xaxis.setDrawGridLines(false);
            xaxis.setPosition(XAxis.XAxisPosition.BOTTOM);

            YAxis left=chartsub.getAxisLeft();// no axis labels
            left.setDrawGridLines(false); // no grid lines
            left.setDrawZeroLine(true); // draw a zero line
            chartsub.getAxisRight().setEnabled(false);


            BARENTRYsub = new ArrayList<>();

            BarEntryLabelssub = new ArrayList<String>();

            AddValuesToBARENTRY();

            AddValuesToBarEntryLabels();

            Bardatasetsub = new BarDataSet(BARENTRYsub, "DURATION IN MINUTES");

            BARDATAsub = new BarData(BarEntryLabelssub, Bardatasetsub);

            Bardatasetsub.setColor(Color.rgb(19, 138, 176));
            Bardatasetsub.setBarSpacePercent(50);
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

            BarEntryLabelssub.add("DAY 1");
            BarEntryLabelssub.add("DAY 2");
            BarEntryLabelssub.add("DAY 3");
            BarEntryLabelssub.add("DAY 4");
            BarEntryLabelssub.add("DAY 5");
            BarEntryLabelssub.add("DAY 6");
            BarEntryLabelssub.add("DAY 7");


        }

    }



}
