package com.shivam.probussense.Activities;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.shivam.probussense.Adaptors.Adaptor;
import com.shivam.probussense.Classes.HttpHandler;
import com.shivam.probussense.Classes.swimmingpools;
import com.shivam.probussense.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class tables extends AppCompatActivity {

    private String TAG = tables.class.getSimpleName();

    String organizationNamestr;

    public static ArrayList<swimmingpools> pools=new ArrayList<>();
    RecyclerView recyclerView;
    TextView organizationName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tables);

        organizationName=findViewById(R.id.organizationName);

        new tables.GetContacts().execute();

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
            recyclerView=findViewById(R.id.recycleview);
            Adaptor adaptor=new Adaptor(tables.this,pools);
            recyclerView.setLayoutManager(new LinearLayoutManager(tables.this));
            recyclerView.setAdapter(adaptor);

        }

    }
}
