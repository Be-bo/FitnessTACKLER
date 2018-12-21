package com.example.macbook.fitnesstackler;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseViewHolder> {
    private List<ExerciseItem> data;
    public event EventHandler<int> ItemClick;

    public ExerciseAdapter(List<ExerciseItem> exercises)
    {
        data = exercises;
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
    public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.list_item_exercise, viewGroup, false);

        return new ExerciseViewHolder(itemView, OnClick);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseViewHolder exerciseViewHolder, int i) {
        exerciseViewHolder.ExerciseName.setText(data.get(i).toString());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
