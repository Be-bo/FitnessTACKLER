package com.example.macbook.fitnesstackler;

import java.util.List;

public class ExerciseItem {
    public String exerciseName;
    public boolean expanded;
    public List<String> sets;

    public ExerciseItem(){}

    public String getExerciseName() {
        return exerciseName;
    }

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public List<String> getSets() {
        return sets;
    }

    public void setSets(List<String> sets) {
        this.sets = sets;
    }
}
