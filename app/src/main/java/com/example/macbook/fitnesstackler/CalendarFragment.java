package com.example.macbook.fitnesstackler;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import java.util.ArrayList;

public class CalendarFragment  extends Fragment {

    private CalendarView calendar;
    private RecyclerView recyclerView;
    private ArrayList<WorkoutItem> RecyclerViewData;
    private WorkoutAdapter AdapterHome;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.calendar_fragment, null);

        calendar = view.findViewById(R.id.calendarView1);
        recyclerView = view.findViewById(R.id.recyclerView1);
        Keyboard keyboard = (Keyboard) getArguments().getSerializable("keyboard");
        RecyclerViewData = new ArrayList<WorkoutItem>();
        AdapterHome = new WorkoutAdapter(view.getContext(), RecyclerViewData, keyboard, view);
        recyclerView.setAdapter(AdapterHome);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        AdapterHome.ItemClick += OnItemClick;
        calendar.DateChange += YourCalendarView_DateChange;

        return view;
    }

    private void YourCalendarView_DateChange(object sender, CalendarView.DateChangeEventArgs e)
    {
        System.console().writer("Date : {0} Month : {1} Year : {2}", e.DayOfMonth, e.Month, e.Year);
        HideWorkout();
        if (e.DayOfMonth == 12)
        {
            ShowWorkout1();
        }
        else if (e.DayOfMonth == 15)
        {
            ShowWorkout2();
        }
        else if (e.DayOfMonth == 23)
        {
            ShowWorkout1();
            ShowWorkout2();
        }
    }

    public void ShowWorkout1()
    {
        WorkoutItem wi = new WorkoutItem();
        wi.setTitle("Jazzercise");
        wi.setExercises("Jazz and cise");
        wi.setExpanded(false);
        RecyclerViewData.add(wi);
        AdapterHome.notifyDataSetChanged();
    }

    public void ShowWorkout2()
    {
        WorkoutItem wi = new WorkoutItem();
        wi.setTitle("Aquacise");
        wi.setExercises("Move them bones");
        wi.setExpanded(false);
        RecyclerViewData.add(wi);
        AdapterHome.notifyDataSetChanged();
    }


    public void HideWorkout()
    {
        RecyclerViewData.clear();
        AdapterHome.notifyDataSetChanged();
    }

    private void OnItemClick(Object sender, int position)
    {
        if (RecyclerViewData.get(position).isExpanded())
        {
            RecyclerViewData.get(position).setExpanded(false);
        }
        else
        {
            RecyclerViewData.get(position).setExpanded(true);
        }
        AdapterHome.notifyItemChanged(position);
    }
}
