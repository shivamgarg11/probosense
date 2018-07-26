package com.shivam.probussense.Services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;


import com.shivam.probussense.Activities.login;
import com.shivam.probussense.Classes.HttpHandler;
import com.shivam.probussense.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Mynotifyservice extends Service {

    String user_id;
    int flagsonstart,startidonstart;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();

        SharedPreferences prefs = getSharedPreferences("probussense", MODE_PRIVATE);
        user_id = prefs.getString("user_id", "");//"No name defined" is the default value.
    }

    @Override
    public void onDestroy() {
        startService(new Intent(getApplicationContext(), Mynotifyservice.class));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        flagsonstart=flags;
        startidonstart=startId;

        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {


            public void run() {
                new GetContacts().execute();
                handler.postDelayed(this, 180000);
            }
        };
        handler.postDelayed(runnable, 0);

        return START_STICKY;
    }


    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        onStartCommand(rootIntent,flagsonstart,startidonstart);
    }


    @Override
    public void onLowMemory() {
        super.onLowMemory();
        stopService(new Intent(getApplicationContext(), Mynotifyservice.class));
        startService(new Intent(getApplicationContext(), Mynotifyservice.class));
    }


    private class GetContacts extends AsyncTask<Void, Void, Void> {

        Boolean msgboolean=false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            ConnectivityManager connectivityManager =(ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if (activeNetworkInfo != null) {


                String url = "http://water.probussense.com/application/getnotify?user_id=" + user_id;
                // Making a request to url and getting response
                String jsonStr = sh.makeServiceCall(url);

                if(jsonStr!=null) {

                    try {
                        JSONArray jsonarray = new JSONArray(jsonStr);
                        JSONObject object = jsonarray.getJSONObject(0);
                        String msg = object.getString("msg");

                        if (msg.length() == 4) {
                            msgboolean = true;
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                        return null;

                    }
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if(!msgboolean){
                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(Mynotifyservice.this)
                                .setSmallIcon(R.drawable.logo)
                                .setContentTitle("WARNING")
                                .setColor(Color.RED)
                                .setContentText("Pools are not safe");


                PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0,
                        new Intent(getApplicationContext(), login.class), PendingIntent.FLAG_UPDATE_CURRENT);


                mBuilder.setContentIntent(contentIntent);

                // Gets an instance of the NotificationManager service//

                NotificationManager mNotificationManager = (NotificationManager) getSystemService(Mynotifyservice.this.NOTIFICATION_SERVICE);

                mNotificationManager.notify(001, mBuilder.build());



            }
        }


    }
}
