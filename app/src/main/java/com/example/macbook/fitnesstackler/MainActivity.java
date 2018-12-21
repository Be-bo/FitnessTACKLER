package com.example.macbook.fitnesstackler;

import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigation;
    Keyboard keyboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.NavigationItemSelected += BottomNavigation_NavigationItemSelected;
        bottomNavigation.setSelectedItemId(R.id.action_home);
        LoadFragment(R.id.action_home);
    }

    private void BottomNavigation_NavigationItemSelected(Object sender, BottomNavigationView.NavigationItemSelectedEventArgs e)
    {
        LoadFragment(e.Item.ItemId);
    }

    void LoadFragment(int id)
    {
        //creating, linking and putting the keyboard into a bundle
        keyboard = findViewById(R.id.keyboard_weights);
        Bundle mybundle = new Bundle();
        mybundle.putSerializable("keyboard", keyboard);

        Android.Support.V4.App.Fragment fragment = null;
        //int title = 0;
        switch (id)
        {
            case R.id.action_home:
                fragment = new HomeFragment();
                fragment.setArguments(mybundle);
                //title = Resource.String.home;
                break;
            case R.id.action_calender:
                fragment = CalendarFragment.NewInstance();
                fragment.setArguments(mybundle);
                //title = Resource.String.calendar;
                break;
            case R.id.action_workout:
                fragment = WorkoutFragment.NewInstance();
                //title = Resource.String.workout;
                break;
            case R.id.action_settings:
                fragment = SettingsFragment.NewInstance();
                //title = Resource.String.settings;
                break;
        }
        if (fragment == null)
            return;

        SupportFragmentManager.BeginTransaction()
                .Replace(R.id.content_frame, fragment)
                .Commit();

        //if (title!=0) SupportActionBar.SetTitle(title);
    }
}
