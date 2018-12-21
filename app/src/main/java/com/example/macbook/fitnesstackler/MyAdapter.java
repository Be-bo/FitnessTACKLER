package com.example.macbook.fitnesstackler;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import static com.example.macbook.fitnesstackler.FilterHelper.currentList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private ArrayList<WorkoutItem> data;
    private ArrayList<WorkoutItem> currentList;
    public EventHandler<int> ItemClick;

    public MyAdapter(ArrayList<WorkoutItem> data)
    {
        this.data = data;
        this.currentList = data;
    }

    public void setData(ArrayList<WorkoutItem> filteredData)
    {
        this.data = filteredData;
    }
    public Filter Filter
    {
        get { return FilterHelper.newInstance(currentList, this); }
    }

    private void OnClick(int position)
    {
        if (ItemClick != null)
        {
            ItemClick(this, position);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //INFLATE LAYOUT TO VIEW
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.model, viewGroup, false);
        MyViewHolder holder = new MyViewHolder(v, OnClick);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        if (myViewHolder != null)
        {
            myViewHolder.NameTxt.setText(data.get(i).getTitle());
            myViewHolder.ExTxt.setText(data.get(i).getExercises());
        }

        if (data.get(i).isExpanded())
        {
            // expand
            myViewHolder.ExpandedLayout.setVisibility(View.VISIBLE);
        }
        else //collapse
            myViewHolder.ExpandedLayout.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    /*
     * Our ViewHolder class
     */
    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        public TextView NameTxt;
        public LinearLayout ExpandedLayout;
        public TextView ExTxt;
            public MyViewHolder(View itemView, Action<int> listener) : base(itemView)
        {
            NameTxt = itemView.findViewById(R.id.nameTxt);
            ExpandedLayout = itemView.findViewById(R.id.expanded_wo);
            ExTxt = itemView.findViewById(R.id.ex_exercises);
            itemView.Click += (sender, e) => listener(base.AdapterPosition);
            NameTxt.Click += (sender, e) => listener(base.AdapterPosition);
        }
    }
}
