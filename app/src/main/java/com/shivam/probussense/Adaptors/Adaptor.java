package com.shivam.probussense.Adaptors;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shivam.probussense.Activities.Charts;
import com.shivam.probussense.R;
import com.shivam.probussense.Classes.swimmingpools;

import java.util.ArrayList;

public class Adaptor extends RecyclerView.Adapter<Adaptor.CourseViewHolder> {

    private Context context;
    private ArrayList<swimmingpools> swimmingpools;

    public Adaptor(Context context, ArrayList<swimmingpools> swimmingpools) {
        this.context = context;
        this.swimmingpools = swimmingpools;
    }




    @NonNull
    @Override
    public Adaptor.CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = li.inflate(viewType, parent, false);

        return new CourseViewHolder(itemView);
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.tablerecycleview;
    }

    @Override
    public void onBindViewHolder(@NonNull final Adaptor.CourseViewHolder holder, int position) {

        final swimmingpools pool=swimmingpools.get(position);
        holder.Swimmingpoolname.setText(pool.getName());
        holder.phvalue.setText(String.valueOf(pool.getPh()));
        holder.clvalue.setText(String.valueOf(pool.getChlorine()));

        holder.temp.setText(String.valueOf("T="+pool.getTemp()+"C"));
        holder.hubid.setText(String.valueOf(pool.getHubid()));
        holder.date.setText(pool.getDatetime().substring(0,10));
        holder.time.setText(pool.getDatetime().substring(11,16));



        if(pool.getPh()<7.2||pool.getPh()>8.0){
            holder.phvalue.setTextColor(Color.RED);
            holder.remarkph.setText("Poor");
            holder.remarkph.setTextColor(Color.RED);
        }
        else{
            holder.phvalue.setTextColor(Color.rgb(24,124,46));
            holder.remarkph.setText("Satisfactory");
            holder.remarkph.setTextColor(Color.rgb(24,124,46));
        }


        if(pool.getChlorine()<1.0||pool.getChlorine()>3.0){
            holder.clvalue.setTextColor(Color.RED);
            holder.remarkcl.setText("Poor");
            holder.remarkcl.setTextColor(Color.RED);

        }else{
            holder.clvalue.setTextColor(Color.rgb(24,124,46));
            holder.remarkcl.setText("Satisfactory");
            holder.remarkcl.setTextColor(Color.rgb(24,124,46));
        }

holder.analytics.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent i=new Intent(context, Charts.class);
        i.putExtra("assert_id",pool.getHubid());
        context.startActivity(i);
    }
});


    }

    @Override
    public int getItemCount() {
        return swimmingpools.size();
    }



    static class CourseViewHolder extends RecyclerView.ViewHolder {

        TextView Swimmingpoolname,temp, phvalue,remarkph,clvalue,remarkcl,date,time,hubid;
        ImageView analytics;

        public CourseViewHolder(View itemView) {
            super(itemView);
            Swimmingpoolname = itemView.findViewById(R.id.Swimmingpoolname);
            temp = itemView.findViewById(R.id.texttemp);
            phvalue = itemView.findViewById(R.id.textphvalue);
            remarkph = itemView.findViewById(R.id.textphremark);
            clvalue = itemView.findViewById(R.id.textclvalue);
            remarkcl = itemView.findViewById(R.id.textclremark);
            date = itemView.findViewById(R.id.textdate);
            time = itemView.findViewById(R.id.texttime);
            hubid = itemView.findViewById(R.id.texthubid);

            analytics=itemView.findViewById(R.id.analytics);
        }
    }
}
