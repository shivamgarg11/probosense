package com.shivam.probussense.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.shivam.probussense.Classes.HttpHandler;
import com.shivam.probussense.R;
import com.shivam.probussense.horizontalswip.horizontal;

import org.json.JSONException;
import org.json.JSONObject;

public class Login extends AppCompatActivity {

    TextInputEditText userid,password;
    Button login;

    private String TAG = Login.class.getSimpleName();
    public  static String USERID;
    String useridstr,passwordstr;
    boolean error;
    String msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userid=findViewById(R.id.edituserid);
        password=findViewById(R.id.editpassword);
        login=findViewById(R.id.btnlogin);



        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                useridstr=userid.getText().toString();
                passwordstr=password.getText().toString();
                new GetContacts().execute();
            }
        });





    }



    private class GetContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();




           String url = "http://water.probussense.com/application/login?username="+useridstr+"&password="+passwordstr;
            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);

            try {
                JSONObject jsonObj = new JSONObject(jsonStr);
                //Log.e(TAG, "Response from json: " + jsonObj+"\n");
                 error=jsonObj.getBoolean("error");
                if(!error){
                JSONObject data = jsonObj.getJSONObject("data");
                USERID=data.getString("user_id");

                }else {
                     msg=jsonObj.getString("msg");

                }



            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if(error) {
                Toast.makeText(Login.this, msg, Toast.LENGTH_SHORT).show();
                userid.setText("");
                password.setText("");
                useridstr = "";
                passwordstr = "";
            }else{
                SharedPreferences.Editor editor = getSharedPreferences("probussense", MODE_PRIVATE).edit();
                editor.putString("user_id", USERID);
                editor.apply();

                Intent i=new Intent(Login.this,horizontal.class);
                startActivity(i);
                finish();
            }
        }

    }
}
