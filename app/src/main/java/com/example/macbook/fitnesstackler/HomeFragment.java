package com.example.macbook.fitnesstackler;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private ArrayList<WorkoutItem> RecyclerViewData;
    private WorkoutAdapter AdapterHome;
    private RecyclerView RecyclerViewWorkouts;
    private View root;
    private Keyboard keyboard;
    private int Today;
    private TextView ToolBarDate;
    private int IncrementDays;
    private TextView NoScheduledWorkoutsTv;

    public static HomeFragment NewInstance()
    {
        var frag1 = new HomeFragment { Arguments = new Bundle() };
        return frag1;
    }

    @Override
    public View OnCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        root = inflater.inflate(R.layout.home_fragment, null);
        NoScheduledWorkoutsTv = root.findViewById(R.id.no_scheduled_workouts);

        IncrementDays = 0;

        String currentDate = DateTime.UtcNow.Date.ToString("dddd, d");
        ToolBarDate = root.findViewById(R.id.todays_date);
        Today = Int32.Parse(Regex.Match(currentDate, @"\d+").Value);
        ToolBarDate.setText(currentDate + GetDaySuffix(Today));

        RecyclerViewData = new ArrayList<WorkoutItem>();

        keyboard = (Keyboard)getArguments().getSerializable("keyboard");

        RecyclerView recyclerView = root.findViewById(R.id.recyclerview_home);
        AdapterHome = new WorkoutAdapter(root.getContext(), RecyclerViewData, keyboard, root);
        recyclerView.setAdapter(AdapterHome);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setNestedScrollingEnabled(false);
        RecyclerViewWorkouts = root.findViewById(R.id.recyclerview_home);
        AdapterHome = new WorkoutAdapter(root.getContext(), RecyclerViewData, keyboard, root);
        RecyclerViewWorkouts.setAdapter(AdapterHome);
        RecyclerViewWorkouts.setLayoutManager(new LinearLayoutManager(getContext()));
        RecyclerViewWorkouts.setNestedScrollingEnabled(false);
        AdapterHome.ItemClick += OnItemClick;

        RetrieveWorkouts();

        SetupClickListeners(root);

        return root;
    }

    private String GetDaySuffix(int day)
    {
        switch (day)
        {
            case 1:
            case 21:
            case 31:
                return "st";
            case 2:
            case 22:
                return "nd";
            case 3:
            case 23:
                return "rd";
            default:
                return "th";
        }
    }

    private void SetupClickListeners(View root)
    {
        // add workout button on click
        root.findViewById(R.id.add_workout_btn).Click += delegate
        {
            NoScheduledWorkoutsTv.setVisibility(View.GONE);

            // collapse previously expanded item
            for (int i = 0; i < RecyclerViewData.size(); i++)
            {
                if (RecyclerViewData.get(i).isExpanded())
                {
                    RecyclerViewData.get(i).setExpanded(false);
                    AdapterHome.notifyItemChanged(i);
                }
            }
            WorkoutItem wi = new WorkoutItem();
            wi.setEditNewWorkout(true);
            RecyclerViewData.add(wi);
            AdapterHome.notifyDataSetChanged();
        };

        // left/right arrows in actionbar
        root.findViewById(R.id.left_arrow_btn).Click += delegate (object sender, EventArgs e){ ToolBarArrowsClickListener(sender, e, 0);};
        root.findViewById(R.id.right_arrow_btn).Click += delegate (object sender, EventArgs e) { ToolBarArrowsClickListener(sender, e, 1); };

    }

    private void ToolBarArrowsClickListener(Object sender, EventArgs e, int pos)
    {
        IncrementDays += (pos == 0) ? -1 : 1;
        DateTime selectedDate = DateTime.UtcNow.AddDays(IncrementDays);
        String selectedDateStr = selectedDate.toString("dddd, d");
        ToolBarDate.setText(selectedDateStr + GetDaySuffix(Int32.Parse(Regex.Match(selectedDateStr, @"\d+").Value)));

        if (IncrementDays == 0) // current date
        {
            NoScheduledWorkoutsTv.setVisibility(View.GONE);
            RetrieveWorkouts();
        } else
        {
            RecyclerViewData.clear();
            AdapterHome.notifyDataSetChanged();
            NoScheduledWorkoutsTv.setVisibility(View.VISIBLE);
        }
    }

    public void RetrieveWorkouts()
    {
        RecyclerViewData.clear();
        AdapterHome.notifyDataSetChanged();
        WorkoutItem wi = new WorkoutItem();
        wi.setTitle("Friday workout");
        wi.setExercises("Weighted Pull Ups\nBarbell Full Squat\nSingle-Arm Linear Jammer\nLandmine 180's");
        wi.setExpanded(false);
        wi.setEditNewWorkout(false);
        RecyclerViewData.add(wi);
        AdapterHome.notifyDataSetChanged();
        wi = new WorkoutItem();
        wi.setTitle("wednesday prancercise");
        wi.setExercises("Bench Press\nDeadlift with Chains\nBox Squat\nKneeling Squat");
        wi.setExpanded(false);
        wi.setEditNewWorkout(false);
        RecyclerViewData.add(wi);
        AdapterHome.notifyDataSetChanged();
    }

    private void OnItemClick(Object sender, int position)
    {
        // collapse previously expanded item
        for (int i= 0; i < RecyclerViewData.size(); i++)
        {
            if (RecyclerViewData.get(i).isExpanded() && i != position)
            {
                RecyclerViewData.get(i).setExpanded(false);
                AdapterHome.notifyItemChanged(i);
            }
        }

        // expand selected item
        RecyclerViewData.get(position).setExpanded(!RecyclerViewData.get(position).isExpanded());
        HideKeyboard();
        AdapterHome.notifyItemChanged(position);
        RecyclerViewWorkouts.smoothScrollToPosition(position);
    }

    public void HideKeyboard()
    {
        var imm = (InputMethodManager)Activity.GetSystemService(Context.InputMethodService);
        imm.HideSoftInputFromWindow(root.WindowToken, HideSoftInputFlags.NotAlways);
    }
}
