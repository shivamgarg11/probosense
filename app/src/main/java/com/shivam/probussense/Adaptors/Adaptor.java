package com.shivam.probussense.Adaptors;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;


import com.shivam.probussense.Activities.Tablesrecycle;
import com.shivam.probussense.Classes.swimmingpools;
import com.shivam.probussense.R;

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
    public void onBindViewHolder(@NonNull final Adaptor.CourseViewHolder holder, final int position) {

        final swimmingpools pool=swimmingpools.get(position);
        holder.recycleviewSwimmingpoolname.setText(pool.getName());




        if(pool.getPh()<7.2||pool.getPh()>8.0||pool.getChlorine()<1.0||pool.getChlorine()>3.0){
            holder.recycleviewcheck.setBackgroundColor(Color.RED);

        }
        else{
            holder.recycleviewcheck.setBackgroundColor(Color.rgb(24,124,46));

        }


        holder.recycleframe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(context, Tablesrecycle.class);
                i.putExtra("position",position);
                context.startActivity(i);
            }
        });


    }

    @Override
    public int getItemCount() {
        return swimmingpools.size();
    }



    static class CourseViewHolder extends RecyclerView.ViewHolder {
        TextView recycleviewSwimmingpoolname;
        ImageView recycleviewcheck;
        FrameLayout recycleframe;

        public CourseViewHolder(View itemView) {
            super(itemView);
            recycleviewSwimmingpoolname = itemView.findViewById(R.id.recycleviewpoolname);
            recycleviewcheck=itemView.findViewById(R.id.recycleviewcheck);
            recycleframe=itemView.findViewById(R.id.recycleframe);
        }
    }
}

