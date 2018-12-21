package com.example.macbook.fitnesstackler;

public class WorkoutItem {

    private String title;
    private boolean expanded;
    private String exercises;
    private boolean editNewWorkout;
    private boolean editModeExisting;

    public WorkoutItem(){

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public String getExercises() {
        return exercises;
    }

    public void setExercises(String exercises) {
        this.exercises = exercises;
    }

    public boolean isEditNewWorkout() {
        return editNewWorkout;
    }

    public void setEditNewWorkout(boolean editNewWorkout) {
        this.editNewWorkout = editNewWorkout;
    }

    public boolean isEditModeExisting() {
        return editModeExisting;
    }

    public void setEditModeExisting(boolean editModeExisting) {
        this.editModeExisting = editModeExisting;
    }
}
