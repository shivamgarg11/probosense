package com.shivam.probussense.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

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
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.shivam.probussense.Classes.HttpHandler;
import com.shivam.probussense.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Subchart extends AppCompatActivity {

    BarChart chart ;
    ArrayList<BarEntry> BARENTRY ;
    ArrayList<String> BarEntryLabels ;
    BarDataSet Bardataset ;
    BarData BARDATA ;

TextView subchartyaxis;

    String hubid;
    int []weeks1=new int[7];

    int type=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subchart);

        subchartyaxis=findViewById(R.id.subchartyaxis);

        hubid=getIntent().getStringExtra("assert_id");

        type=Integer.valueOf(hubid.charAt(0))-49;
        hubid=hubid.substring(1);

        Log.e("TAG", "onCreate: " + hubid);

        Log.e("TAG", "onCreate: type : " + type);

        new GetContacts().execute();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Double hubiddouble=Double.valueOf(hubid);

        Intent i=new Intent(Subchart.this, Charts.class);
        i.putExtra("assert_id",hubiddouble);
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


            chart = (BarChart) findViewById(R.id.subchart1);
            chart.setVisibility(View.VISIBLE);

            subchartyaxis.setVisibility(View.VISIBLE);

            BARENTRY = new ArrayList<>();

            BarEntryLabels = new ArrayList<String>();

            AddValuesToBARENTRY();

            AddValuesToBarEntryLabels();

            Bardataset = new BarDataSet(BARENTRY, "Projects");

            BARDATA = new BarData(BarEntryLabels, Bardataset);

            Bardataset.setColor(Color.rgb(19, 138, 176));

            chart.setData(BARDATA);

            chart.animateY(1500);


        }

        public void AddValuesToBARENTRY() {

            BARENTRY.add(new BarEntry(weeks1[0], 0));
            BARENTRY.add(new BarEntry(weeks1[1], 1));
            BARENTRY.add(new BarEntry(weeks1[2], 2));
            BARENTRY.add(new BarEntry(weeks1[3], 3));
            BARENTRY.add(new BarEntry(weeks1[4], 4));
            BARENTRY.add(new BarEntry(weeks1[5], 5));
            BARENTRY.add(new BarEntry(weeks1[6], 6));

        }

        public void AddValuesToBarEntryLabels() {

            BarEntryLabels.add("DAY 1");
            BarEntryLabels.add("DAY 2");
            BarEntryLabels.add("DAY 3");
            BarEntryLabels.add("DAY 4");
            BarEntryLabels.add("DAY 5");
            BarEntryLabels.add("DAY 6");
            BarEntryLabels.add("DAY 7");


        }

    }


}
