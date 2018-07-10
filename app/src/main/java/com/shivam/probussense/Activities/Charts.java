package com.shivam.probussense.Activities;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;

import com.anychart.anychart.AnyChart;
import com.anychart.anychart.AnyChartView;
import com.anychart.anychart.Cartesian;
import com.anychart.anychart.CartesianSeriesColumn;
import com.anychart.anychart.DataEntry;
import com.anychart.anychart.EnumsAnchor;
import com.anychart.anychart.HoverMode;
import com.anychart.anychart.Pie;
import com.anychart.anychart.Position;
import com.anychart.anychart.TooltipPositionMode;
import com.anychart.anychart.ValueDataEntry;
import com.shivam.probussense.Adaptors.Adaptor;
import com.shivam.probussense.Classes.HttpHandler;
import com.shivam.probussense.Classes.swimmingpools;
import com.shivam.probussense.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Charts extends AppCompatActivity {

    double hubid;
    int []weeks=new int[4];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charts);

        hubid=getIntent().getDoubleExtra("assert_id",0);

        Log.e("TAG", "onCreate: "+hubid );


        new GetContacts().execute();


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


                for(int i=0;i<jsonObj.length();i++)
                {
                    JSONArray array1=jsonObj.getJSONArray(i);
                    weeks[i]=array1.length();
                }







            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            AnyChartView anyChartView = findViewById(R.id.any_chart_view);


            Cartesian cartesian = AnyChart.column();

            List<DataEntry> data = new ArrayList<>();
            data.add(new ValueDataEntry("WEEK 1", weeks[0]));
            data.add(new ValueDataEntry("WEEK 2", weeks[1]));
            data.add(new ValueDataEntry("WEEK 3", weeks[2]));
            data.add(new ValueDataEntry("WEEK 4", weeks[3]));


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

            cartesian.getXAxis().setTitle("WEEK");
            cartesian.getYAxis().setTitle("NUMBER");

            anyChartView.setChart(cartesian);

        }

    }
}
