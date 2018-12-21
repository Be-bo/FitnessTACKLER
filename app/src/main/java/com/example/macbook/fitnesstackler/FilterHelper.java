package com.example.macbook.fitnesstackler;

import android.widget.Filter;

import java.util.ArrayList;
import java.util.List;

public class FilterHelper extends Filter {

    static ArrayList<WorkoutItem> currentList;
    static MyAdapter adapter;

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults filterResults = new FilterResults();
        if (constraint != null && constraint.length() > 0)
        {
            //CHANGE TO UPPER
            //constraint = constraint.ToString().ToUpper();
            String query = constraint.toString().toUpperCase();

            //HOLD FILTERS WE FIND
            ArrayList<WorkoutItem> foundFilters = new ArrayList<WorkoutItem>();

            //ITERATE CURRENT LIST
            for (int i = 0; i < currentList.size(); i++)
            {
                String title = currentList.get(i).getTitle();

                //SEARCH
                if (title.toUpperCase().contains(query.toString()))
                {
                    //ADD IF FOUND
                    foundFilters.add(currentList.get(i));
                }
            }
            //SET RESULTS TO FILTER LIST
            filterResults.count = foundFilters.size();
            filterResults.values = foundFilters;
        }
        else
        {
            //NO ITEM FOUND.LIST REMAINS INTACT
            filterResults.count = currentList.size();
            filterResults.values = currentList;
        }

        //RETURN RESULTS
        return filterResults;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        adapter.setData((List<WorkoutItem>)results.values);
        adapter.notifyDataSetChanged();
    }

    public FilterHelper(ArrayList<WorkoutItem> currentList, MyAdapter adapter)
    {
        FilterHelper.adapter = adapter;
        FilterHelper.currentList = currentList;
    }
}
