package com.shivam.probussense.Fragments;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.shivam.probussense.Activities.Charts;
import com.shivam.probussense.Classes.swimmingpools;
import com.shivam.probussense.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class tableswipFragment extends Fragment {

    private FirebaseAnalytics mFirebaseAnalytics;

    swimmingpools pool;
    Context context;

    TextView companyname,Swimmingpoolname,temp, phvalue,remarkph,clvalue,remarkcl,date,time,hubid;
    ImageView analytics;

    public tableswipFragment() {
        // Required empty public constructor
    }


    @SuppressLint("ValidFragment")
    public tableswipFragment(swimmingpools pool, Context context) {
        this.pool=pool;
        this.context=context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView= inflater.inflate(R.layout.fragment_tableswip, container, false);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);

        companyname=rootView.findViewById(R.id.companyname);
        Swimmingpoolname = rootView.findViewById(R.id.fragSwimmingpoolname);
        temp = rootView.findViewById(R.id.fragtexttemp);
        phvalue = rootView.findViewById(R.id.fragtextphvalue);
        remarkph = rootView.findViewById(R.id.fragtextphremark);
        clvalue = rootView.findViewById(R.id.fragtextclvalue);
        remarkcl = rootView.findViewById(R.id.fragtextclremark);
        date = rootView.findViewById(R.id.fragtextdate);
        time = rootView.findViewById(R.id.fragtexttime);
        hubid = rootView.findViewById(R.id.fragtexthubid);

        analytics=rootView.findViewById(R.id.fraganalytics);



        companyname.setText(pool.getOrganizationname());

        Swimmingpoolname.setText(pool.getName());
        phvalue.setText(String.valueOf(pool.getPh()));
        clvalue.setText(String.valueOf(pool.getChlorine()));

        temp.setText(String.valueOf("T="+pool.getTemp()+"C"));
        hubid.setText(String.valueOf(pool.getHubid()));
        date.setText(pool.getDatetime().substring(0,10));
        time.setText(pool.getDatetime().substring(11,16));



        if(pool.getPh()<7.2||pool.getPh()>8.0){
            phvalue.setTextColor(Color.RED);
            remarkph.setText("Poor");
            remarkph.setTextColor(Color.RED);
        }
        else{
            phvalue.setTextColor(Color.rgb(24,124,46));
            remarkph.setText("Satisfactory");
            remarkph.setTextColor(Color.rgb(24,124,46));
        }


        if(pool.getChlorine()<1.0||pool.getChlorine()>3.0){
            clvalue.setTextColor(Color.RED);
            remarkcl.setText("Poor");
            remarkcl.setTextColor(Color.RED);

        }else{
            clvalue.setTextColor(Color.rgb(24,124,46));
            remarkcl.setText("Satisfactory");
            remarkcl.setTextColor(Color.rgb(24,124,46));
        }

        analytics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(context, Charts.class);
                i.putExtra("assert_id",pool.getHubid());
                context.startActivity(i);
            }
        });




        return rootView;
    }
}
