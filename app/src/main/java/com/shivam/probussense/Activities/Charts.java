package com.shivam.probussense.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.shivam.probussense.R;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.anychart.anychart.AnyChart;
import com.anychart.anychart.AnyChartView;
import com.anychart.anychart.Cartesian;
import com.anychart.anychart.CartesianSeriesColumn;
import com.anychart.anychart.DataEntry;
import com.anychart.anychart.EnumsAnchor;
import com.anychart.anychart.HoverMode;
import com.anychart.anychart.Position;
import com.anychart.anychart.TooltipPositionMode;
import com.anychart.anychart.ValueDataEntry;
import com.shivam.probussense.Classes.HttpHandler;
import com.shivam.probussense.horizontalswip.horizontal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Charts extends AppCompatActivity {


    BarChart chart ;
    ArrayList<BarEntry> BARENTRY ;
    ArrayList<String> BarEntryLabels ;
    BarDataSet Bardataset ;
    BarData BARDATA ;

    double hubid;
    int []weeks=new int[5];


    Button week1,week2,week3,week4;

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

        new GetContacts().execute();

        week1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Charts.this,Subchart.class);
                i.putExtra("assert_id","1"+hubid);
                startActivity(i);
                finish();
            }
        });

        week2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Charts.this,Subchart.class);
                i.putExtra("assert_id","2"+hubid);
                startActivity(i);
                finish();
            }
        });

        week3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Charts.this,Subchart.class);
                i.putExtra("assert_id","3"+hubid);
                startActivity(i);
                finish();
            }
        });

        week4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Charts.this,Subchart.class);
                i.putExtra("assert_id","4"+hubid);
                startActivity(i);

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

            BARENTRY = new ArrayList<>();

            BarEntryLabels = new ArrayList<String>();

            AddValuesToBARENTRY();

            AddValuesToBarEntryLabels();

            Bardataset = new BarDataSet(BARENTRY, "Projects");

            BARDATA = new BarData(BarEntryLabels, Bardataset);

            Bardataset.setColor(Color.rgb(19,138,176));

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


}
