package com.example.macbook.fitnesstackler;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class ExerciseViewHolder extends RecyclerView.ViewHolder {
    public TextView ExerciseName;
    public ExerciseViewHolder(@NonNull View itemView) {
        super(itemView);
        ExerciseName = itemView.findViewById(R.id.exercise_name);
        itemView.Click += (sender, e) => listener(base.Position);
    }

    public TextView getExerciseName() {
        return ExerciseName;
    }

    public void setExerciseName(TextView exerciseName) {
        ExerciseName = exerciseName;
    }
}
