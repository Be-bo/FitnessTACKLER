package com.example.macbook.fitnesstackler;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WorkoutViewHolder extends RecyclerView.ViewHolder {
    public TextView Title ;
    public TextView Exercises ;
    public LinearLayout ExpandedLayout ;
    public LinearLayout ExerciseItem ;
    public Button AddExerciseBtn ;
    public CardView Root ;
    public ImageButton MoreOptionsButton ;
    public LinearLayout RootWorkoutLayout ;
    public LinearLayout LayoutTitleAndMenu ;

    // EDIT MODE UI
    public LinearLayout EditModeRoot ;
    public EditText NewWorkoutName ;
    public ImageButton DeleteWorkoutBtn ;
    public Button SaveChangesBtn ;

    // MORE OPTIONS MENU
    public LinearLayout MoreOptionsMenu ;
    public Button SaveWorkoutBtn ;
    public Button EditWorkoutBtn ;

    public WorkoutViewHolder(View itemView, Action<int> listener) : base(itemView)
    {
        Title = itemView.findViewById(R.id.workout_title);
        Exercises = itemView.findViewById(R.id.tv_exercises);
        ExpandedLayout = itemView.findViewById(R.id.expanded_layout);
        ExerciseItem = itemView.findViewById(R.id.root_exercise_item);
        AddExerciseBtn = itemView.findViewById(R.id.add_exercise_btn);
        Root = itemView.findViewById(R.id.root_list_item_workout);
        MoreOptionsButton = itemView.findViewById(R.id.more_options_btn);
        RootWorkoutLayout = itemView.findViewById(R.id.root_workout_layout);
        LayoutTitleAndMenu = itemView.findViewById(R.id.linearlayout_workouttitle_menu);

        EditModeRoot = itemView.findViewById(R.id.layout_edit_mode);
        NewWorkoutName = itemView.findViewById(R.id.new_workout_name_edittext);
        DeleteWorkoutBtn = itemView.findViewById(R.id.delete_workout_btn);
        SaveChangesBtn = itemView.findViewById(R.id.save_changes_btn);

        MoreOptionsMenu = itemView.findViewById(R.id.more_options_menu);
        SaveWorkoutBtn = itemView.findViewById(R.id.save_workout_btn);
        EditWorkoutBtn = itemView.findViewById(R.id.edit_workout_btn);

        // AddExerciseBtn.Click += (sender, e) => listener(base.AdapterPosition);
        itemView.Click += (sender, e) => listener(base.AdapterPosition);
        Title.Click += (sender, e) => listener(base.AdapterPosition);
        // MoreOptionsButton.Click += (sender, e) => listener(base.AdapterPosition);
    }
}
}
}
