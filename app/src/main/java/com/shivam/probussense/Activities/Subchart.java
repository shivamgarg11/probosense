package com.shivam.probussense.Activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

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
import com.shivam.probussense.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Subchart extends AppCompatActivity {

    String hubid;
    int []weeks1=new int[7];

    int type=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subchart);

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



            String url = "http://water.probussense.com/application/chart?asset_id="+hubid;
            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);

            try {
                JSONArray jsonObj = new JSONArray(jsonStr);

                JSONArray array1 = jsonObj.getJSONArray(type);

                    for(int j=0;j<array1.length();j++){

                        JSONObject object=array1.getJSONObject(j);

                        String dt=object.getString("dt");



                        int val=Integer.valueOf(dt.substring(8,10));


                        String time=object.getString("duration");


                        switch ((val%7)){
                            case 0: weeks1[0]+=Integer.valueOf(time.substring(0,time.length()-7));
                                break;

                            case 1: weeks1[1]+=Integer.valueOf(time.substring(0,time.length()-7));
                                break;

                            case 2:weeks1[2]+=Integer.valueOf(time.substring(0,time.length()-7));
                                break;

                            case 3:weeks1[3]+=Integer.valueOf(time.substring(0,time.length()-7));
                                break;

                            case 4:weeks1[4]+=Integer.valueOf(time.substring(0,time.length()-7));
                                break;

                            case 5:weeks1[5]+=Integer.valueOf(time.substring(0,time.length()-7));
                                break;

                            case 6:weeks1[6]+=Integer.valueOf(time.substring(0,time.length()-7));
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

            AnyChartView anyChartView = findViewById(R.id.subany_chart_view);
            Cartesian cartesian = AnyChart.column();

            List<DataEntry> data = new ArrayList<>();



                data.clear();
                data.add(new ValueDataEntry("DAY 1", weeks1[0]));
                data.add(new ValueDataEntry("DAY 2", weeks1[1]));
                data.add(new ValueDataEntry("DAY 3", weeks1[2]));
                data.add(new ValueDataEntry("DAY 4", weeks1[3]));
                data.add(new ValueDataEntry("DAY 5", weeks1[4]));
                data.add(new ValueDataEntry("DAY 6", weeks1[5]));
                data.add(new ValueDataEntry("DAY 7", weeks1[6]));
                cartesian.getXAxis().setTitle("DAYS");



            CartesianSeriesColumn column = cartesian.column(data);

            column.getTooltip()
                    .setTitleFormat("{%X}")
                    .setPosition(Position.CENTER_BOTTOM)
                    .setAnchor(EnumsAnchor.CENTER_BOTTOM)
                    .setOffsetX(0d)
                    .setOffsetY(5d)
                    .setFormat("{%Value}{groupsSeparator: }");

            cartesian.setAnimation(true);
            cartesian.setTitle("");

            cartesian.getYScale().setMinimum(0d);

            cartesian.getYAxis().getLabels().setFormat("{%Value}{groupsSeparator: }");

            cartesian.getTooltip().setPositionMode(TooltipPositionMode.POINT);
            cartesian.getInteractivity().setHoverMode(HoverMode.BY_X);


            cartesian.getYAxis().setTitle("DURATION in MINUTE");

                anyChartView.setChart(cartesian);}


        }


}
