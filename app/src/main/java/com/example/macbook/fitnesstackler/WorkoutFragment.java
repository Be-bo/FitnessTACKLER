package com.example.macbook.fitnesstackler;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class WorkoutFragment extends Fragment {

    private RecyclerView rv;
    private SearchView sv;
    private View rootView;
    private MyAdapter adapter;

    private ArrayList<WorkoutItem> MyWorkoutList;

    // public override void OnCreate(Bundle savedInstanceState)
    //  {
    //      base.OnCreate(savedInstanceState);

    // Create your fragment here
    //    }

    public static WorkoutFragment NewInstance()
    {
        var frag3 = new WorkoutFragment { Arguments = new Bundle() };
        return frag3;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        rootView = inflater.inflate(R.layout.workout_fragment, container, false);
        MyWorkoutList = new ArrayList<WorkoutItem>();
        //init 
        sv = rootView.findViewById(R.id.searchView1);
        rv = rootView.findViewById(R.id.mRecyclerID);
        adapter = new MyAdapter(MyWorkoutList);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setItemAnimator(new DefaultItemAnimator());

        adapter.ItemClick += OnItemClick;
        RetrieveWorkouts();

        sv.QueryTextChange += sv_QueryTextChange;
        return rootView;
    }

    private void OnItemClick(Object sender, int position)
    {
        MyWorkoutList.get(position).setExpanded(!MyWorkoutList.get(position).isExpanded());

        //var imm = (InputMethodManager)Activity.GetSystemService(Context.InputMethodService);
        //imm.HideSoftInputFromWindow(rootView.WindowToken, HideSoftInputFlags.NotAlways);

        adapter.notifyItemChanged(position);
    }


    void sv_QueryTextChange(Object sender, SearchView.QueryTextChangeEventArgs e)
    {
        adapter.Filter.InvokeFilter(e.NewText);
    }

    public void RetrieveWorkouts()
    {
        WorkoutItem wi = new WorkoutItem();
        wi.setTitle("Friday workout");
        wi.setExercises("Weighted Pull Ups\nBarbell Full Squat\nSingle-Arm Linear Jammer\nLandmine 180's");
        wi.setExpanded(false);
        MyWorkoutList.add(wi);
        wi = new WorkoutItem();
        wi.setTitle("wednesday prancercise");
        wi.setExercises("Bench Press\nDeadlift with Chains\nBox Squat\nKneeling Squat");
        wi.setExpanded(false);
        MyWorkoutList.add(wi);
        adapter.notifyDataSetChanged();
    }
}
