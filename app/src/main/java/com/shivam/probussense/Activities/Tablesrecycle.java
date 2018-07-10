package com.shivam.probussense.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.shivam.probussense.Classes.swimmingpools;
import com.shivam.probussense.R;
import com.shivam.probussense.horizontalswip.horizontal;

public class Tablesrecycle extends AppCompatActivity {


    swimmingpools pool;

    TextView Swimmingpoolname,temp, phvalue,remarkph,clvalue,remarkcl,date,time,hubid;
    ImageView analytics;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_tableswip);

        int i=getIntent().getIntExtra("position",-1);

        if(i==-1){
            Toast.makeText(this, " No Data Avaliable ", Toast.LENGTH_SHORT).show();
            onBackPressed();
        }else {

            pool= horizontal.pools.get(i);

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
