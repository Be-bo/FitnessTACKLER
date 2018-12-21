package com.example.macbook.fitnesstackler;

import android.app.AlertDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class WorkoutAdapter extends RecyclerView.Adapter<WorkoutAdapter.WorkoutViewHolder>{

    private List<WorkoutItem> data;
    public EventHandler<int> ItemClick;
    private Context context;
    private View itemView;
    private View root_view;
    private Keyboard keyboard;


    public class WorkoutViewHolder extends RecyclerView.ViewHolder {

        public TextView Title;
        public TextView Exercises;
        public LinearLayout ExpandedLayout;
        public LinearLayout ExerciseItem;
        public Button AddExerciseBtn;
        public CardView Root;
        public ImageButton MoreOptionsButton;
        public LinearLayout RootWorkoutLayout;
        public LinearLayout LayoutTitleAndMenu;

        //Edit mode UI
        public LinearLayout EditModeRoot;
        public EditText NewWorkoutName;
        public ImageButton DeleteWorkoutBtn;
        public Button SaveChangesBtn;

        //More options menu
        public LinearLayout MoreOptionsMenu;
        public Button SaveWorkoutBtn;
        public Button EditWorkoutBtn;

        public WorkoutViewHolder(View view) {
            super(view);
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

//            // AddExerciseBtn.Click += (sender, e) => listener(base.AdapterPosition);
//            itemView.Click += (sender, e) => listener(base.AdapterPosition);
//            Title.Click += (sender, e) => listener(base.AdapterPosition);
//            // MoreOptionsButton.Click += (sender, e) => listener(base.AdapterPosition);
        }
    }

    public WorkoutAdapter(Context c, ArrayList<WorkoutItem> workouts, Keyboard key, View v){
        data = workouts;
        context = c;
        keyboard = key;
        root_view = v;
    }


    @NonNull
    @Override
    public WorkoutAdapter.WorkoutViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull WorkoutAdapter.WorkoutViewHolder workoutHolder, int position) {
        workoutHolder.MoreOptionsButton.setVisibility(View.VISIBLE);
        workoutHolder.MoreOptionsMenu.setVisibility(View.GONE);

        if(data.get(position).isEditNewWorkout()){
            ToggleEditModeExisting(false, workoutHolder, position);
            ToggleEditModeNewWorkout(true, workoutHolder, position);
        } else if (data.get(position).isEditModeExisting()){
            ToggleEditModeNewWorkout(false, workoutHolder, position);
            ToggleEditModeExisting(true, workoutHolder, position);
        }else {
            ToggleEditModeNewWorkout(false, workoutHolder, position);
            ToggleEditModeExisting(false, workoutHolder, position);

            workoutHolder.Title.setText(data.get(position).getTitle());
            workoutHolder.Exercises.setText(data.get(position).getExercises() != null && data.get(position).getExercises().length() > 0 ? data.get(position).getExercises() : "");

            if (!workoutHolder.AddExerciseBtn.hasOnClickListeners()) {
                //not sure how to deal with this it's .setOnClickListener but not sure where to go from there
                workoutHolder.AddExerciseBtn.Click += delegate(object senderExercise, EventArgs eExercise)
                {
                    AddExerciseOnClick(senderExercise, eExercise, workoutHolder.AddExerciseBtn, workoutHolder.ExpandedLayout, position, workoutHolder.Root);
                }
                ;
            }

            ConfigureMoreOptionsMenu(workoutHolder, position);

            if (data.get(position).isExpanded()) {
                // expand
                workoutHolder.ExpandedLayout.removeAllViews();
                workoutHolder.ExpandedLayout.setVisibility(View.VISIBLE);
                workoutHolder.Exercises.setVisibility(View.GONE);
                workoutHolder.AddExerciseBtn.setVisibility(View.VISIBLE);

                // setup widgets for expanded view
                String[] exercisesList = new String[0];
                if (data.get(position).getExercises() != null) {
                    exercisesList = data.get(position).getExercises().split("\n");
                    for (int i = 0; i < exercisesList.length; i++) {
                        View exerciseView = LayoutInflater.from(context).inflate(R.layout.list_item_exercise, null);
                        TextView t = exerciseView.findViewById(R.id.exercise_name);
                        t.setText(exercisesList[i]);
                        LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        ll.topMargin = 8;
                        ll.bottomMargin = 8;
                        ll.leftMargin = 18;
                        ll.rightMargin = 24;
                        exerciseView.setLayoutParams(ll);
                        workoutHolder.ExpandedLayout.addView(exerciseView);

                        EditText target = exerciseView.findViewById(R.id.target_weight);
                        EditText finalWeight = exerciseView.findViewById(R.id.final_weight);
                        setKeyboard(target);
                        setKeyboard(finalWeight);

                        if (i == 0) {
                            // expand first item
                            exerciseView.findViewById(R.id.layout_set).setVisibility(View.VISIBLE);
                            exerciseView.findViewById(R.id.layout_set_title).setVisibility(View.VISIBLE);
                            exerciseView.findViewById(R.id.add_set_btn).setVisibility(View.VISIBLE);
                        } else {
                            exerciseView.findViewById(R.id.layout_set).setVisibility(View.GONE);
                            exerciseView.findViewById(R.id.layout_set_title).setVisibility(View.GONE);
                            exerciseView.findViewById(R.id.add_set_btn).setVisibility(View.GONE);
                        }

                        LinearLayout rootExerciseItem = exerciseView.findViewById(R.id.root_exercise_item);
                        Button addSetBtn = exerciseView.findViewById(R.id.add_set_btn);

                        if (!rootExerciseItem.hasOnClickListeners()) {
                            rootExerciseItem.Click += delegate(object rootSender, EventArgs rootE) {
                                ExerciseItemOnClick(rootSender, rootE);
                            }
                        }

                        if (!addSetBtn.hasOnClickListeners()) {
                            addSetBtn.Click += delegate(object addSetSender, EventArgs addSetE) {
                                ExerciseItemOnClick(addSetSender, addSetE);
                            }
                        }
                    }
                }
            }
        }
    }


    
    public void ResetListeners(WorkoutViewHolder workoutHolder, int position)
    {
        if (workoutHolder.AddExerciseBtn.hasOnClickListeners())
        {
            workoutHolder.AddExerciseBtn.Click -= delegate (object senderExercise, EventArgs eExercise) {
            AddExerciseOnClick(senderExercise, eExercise, workoutHolder.AddExerciseBtn, workoutHolder.ExpandedLayout, position, workoutHolder.Root);
        };
        }

        if (workoutHolder.MoreOptionsButton.hasOnClickListeners())
        {
            workoutHolder.MoreOptionsButton.Click -= delegate (object sender, EventArgs e)
            {
                workoutHolder.MoreOptionsButton.setVisibility(View.INVISIBLE);
                workoutHolder.MoreOptionsMenu.setVisibility(View.VISIBLE);
            };
        }
        if (workoutHolder.SaveWorkoutBtn.hasOnClickListeners())
        {
            workoutHolder.SaveWorkoutBtn.Click -= delegate (object sender2, EventArgs e2)
            {
                workoutHolder.MoreOptionsButton.setVisibility(View.VISIBLE);
                workoutHolder.MoreOptionsMenu.setVisibility(View.GONE);
                Toast.makeText(context, context.getString(R.String.workout_saved, workoutHolder.Title.getText().toString()), Toast.length()_LONG).show();
            };
        }
        if (workoutHolder.EditWorkoutBtn.hasOnClickListeners())
        {
            workoutHolder.EditWorkoutBtn.Click -= delegate (object sender3, EventArgs e3)
            {
                data.get(position).setEditModeExisting(true);
                NotifyItemChanged(position);
            };
        }
        String[] exercisesList = new String[0];
        if (data.get(position).getExercises() != null)
        {
            exercisesList = data.get(position).getExercises().split("\n");
            for (int i = 0; i < exercisesList.length; i++)
            {
                View exerciseView = workoutHolder.ExpandedLayout.getChildAt(i);
                if (exerciseView != null)
                {
                    LinearLayout rootExerciseItem = exerciseView.findViewById(R.id.root_exercise_item);
                    Button addSetBtn = exerciseView.findViewById(R.id.add_set_btn);

                    if (rootExerciseItem != null && rootExerciseItem.hasOnClickListeners())
                    {
                        rootExerciseItem.Click -= delegate (object rootSender, EventArgs rootE) { ExerciseItemOnClick(rootSender, rootE); };
                    }

                    if (addSetBtn != null && addSetBtn.hasOnClickListeners())
                    {
                        addSetBtn.Click -= delegate (object addSetSender, EventArgs addSetE) { ExerciseItemOnClick(addSetSender, addSetE); };
                    }
                }
            }
        }
    }



    private void ConfigureMoreOptionsMenu(WorkoutViewHolder workoutViewHolder, int position)
    {
        if (!workoutViewHolder.MoreOptionsButton.hasOnClickListeners())
        {
            workoutViewHolder.MoreOptionsButton.Click += delegate (object sender, EventArgs e)
            {
                workoutViewHolder.MoreOptionsButton.setVisibility(View.INVISIBLE);
                workoutViewHolder.MoreOptionsMenu.setVisibility(View.VISIBLE);
            };
        }
        if (!workoutViewHolder.SaveWorkoutBtn.hasOnClickListeners())
        {
            workoutViewHolder.SaveWorkoutBtn.Click += delegate (object sender2, EventArgs e2)
            {
                workoutViewHolder.MoreOptionsButton.setVisibility(View.VISIBLE);
                workoutViewHolder.MoreOptionsMenu.setVisibility(View.GONE);
                Toast.makeText(context, context.getString(R.String.workout_saved, workoutViewHolder.Title.getText().toString()), Toast.length()_LONG).show();
            };
        }
        if (!workoutViewHolder.EditWorkoutBtn.hasOnClickListeners())
        {
            workoutViewHolder.EditWorkoutBtn.Click += delegate (object sender3, EventArgs e3)
            {
                data.get(position).setEditModeExisting(true);
                NotifyItemChanged(position);
            };
        }
    }


    // edit mode for existing workout
    private void ToggleEditModeExisting(boolean edit, WorkoutViewHolder workoutHolder, int position)
    {
        if (edit)
        {
            workoutHolder.EditModeRoot.setVisibility(View.VISIBLE);
            workoutHolder.SaveChangesBtn.setVisibility(View.VISIBLE);

            workoutHolder.LayoutTitleAndMenu.setVisibility(View.GONE);
            workoutHolder.Exercises.setVisibility(View.GONE);
            workoutHolder.AddExerciseBtn.setVisibility(View.GONE);

            workoutHolder.ExpandedLayout.setVisibility(View.VISIBLE);
            workoutHolder.ExpandedLayout.removeAllViews();

            workoutHolder.NewWorkoutName.setText(data.get(position).getTitle());
            workoutHolder.NewWorkoutName.requestFocus();

            workoutHolder.NewWorkoutName.TextChanged += delegate (object newNameTextChanged, TextChangedEventArgs eNewName)
            {
                WorkoutNameTextChangeEvent(newNameTextChanged, eNewName, workoutHolder.SaveChangesBtn);
            };

            String[] exercises = null;
            if (data.get(position).getExercises() != null && data.get(position).getExercises().length() > 0)
            {
                exercises = data.get(position).getExercises().split("\n");
            }

            LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            {
                TopMargin = 4,
                BottomMargin = 4
            };

            if (exercises!=null)
            {
                for (int i = 0; i < exercises.length; i++)
                {
                    View editModeExerciseView = LayoutInflater.from(context).inflate(R.layout.list_item_exercise_edit_mode, null);
                    EditText t = editModeExerciseView.findViewById(R.id.exercise_name_edittext);
                    t.setText(exercises[i]);
                    ImageButton deleteExerciseBtn = editModeExerciseView.findViewById(R.id.delete_exercise_btn);
                    SetSuggestions(editModeExerciseView.findViewById(R.id.exercise_name_edittext));

                    editModeExerciseView.setLayoutParams(ll);
                    workoutHolder.ExpandedLayout.addView(editModeExerciseView);

                    SetupTrashcansListenerEditMode(workoutHolder.ExpandedLayout, deleteExerciseBtn, editModeExerciseView);
                }

            }

            if (!workoutHolder.DeleteWorkoutBtn.hasOnClickListeners())
            {
                workoutHolder.DeleteWorkoutBtn.Click += delegate (object s, EventArgs e2) { ShowDialogDeleteWorkout(position); };
            }

            workoutHolder.SaveChangesBtn.Click += delegate (object saveChangesSender, EventArgs eSaveChanges) {
            SaveChangesOnClick(saveChangesSender, eSaveChanges, workoutHolder, position);
        };

        } else
        {
            workoutHolder.LayoutTitleAndMenu.setVisibility(View.VISIBLE);
            workoutHolder.Exercises.setVisibility(View.VISIBLE);
            workoutHolder.AddExerciseBtn.setVisibility(View.VISIBLE);

            workoutHolder.SaveChangesBtn.setVisibility(View.GONE);

            workoutHolder.EditModeRoot.setVisibility(View.GONE);
            workoutHolder.ExpandedLayout.setVisibility(View.GONE);

            // reset listeners
            workoutHolder.NewWorkoutName.TextChanged -= delegate (object newNameTextChanged, TextChangedEventArgs eNewName)
            {
                WorkoutNameTextChangeEvent(newNameTextChanged, eNewName, workoutHolder.SaveChangesBtn);
            };

            String[] exercises = null;
            if (data.get(position).getExercises() != null && data.get(position).getExercises().length() > 0)
            {
                exercises = data.get(position).getExercises().split("\n");
            }

            if (exercises != null)
            {
                for (int i = 0; i < exercises.length; i++)
                {
                    View editModeExerciseView = workoutHolder.ExpandedLayout.getChildAt(i);
                    if (editModeExerciseView != null && editModeExerciseView.findViewById(R.id.delete_exercise_btn)!=null)
                    {
                        editModeExerciseView.findViewById(R.id.delete_exercise_btn).Click -= delegate (object sender, EventArgs e)
                        {
                            EditText t = editModeExerciseView.findViewById(R.id.exercise_name_edittext);
                            int indexToDelete = FindChildIndexByExerciseName(workoutHolder.ExpandedLayout, t.getText());
                            if (indexToDelete != -1)
                            {
                                DeleteExerciseEditModeOnClick(sender, e, workoutHolder.ExpandedLayout, indexToDelete);
                            }
                        };
                    }
                }

            }

            if (workoutHolder.DeleteWorkoutBtn.hasOnClickListeners())
            {
                workoutHolder.DeleteWorkoutBtn.Click -= delegate (Object s, EventArgs e2) { ShowDialogDeleteWorkout(position); };
            }

            if (workoutHolder.SaveChangesBtn.hasOnClickListeners())
            {
                workoutHolder.SaveChangesBtn.Click -= delegate (object saveChangesSender, EventArgs eSaveChanges) {
                SaveChangesOnClick(saveChangesSender, eSaveChanges, workoutHolder, position);
            };
            }
        }
    }



    private void SetupTrashcansListenerEditMode(LinearLayout expandedLayout, ImageButton deleteExerciseBtn, View editModeExerciseView)
    {
        deleteExerciseBtn.Click += delegate (object sender, EventArgs e)
        {
            EditText t = editModeExerciseView.findViewById(R.id.exercise_name_edittext);
            int indexToDelete = FindChildIndexByExerciseName(expandedLayout, t.getText().toString());
            if (indexToDelete != -1)
            {
                DeleteExerciseEditModeOnClick(sender, e, expandedLayout, indexToDelete);
            }
        };
    }



    public int FindChildIndexByExerciseName(LinearLayout expandedLayout, String exerciseName)
    {
        for (int i=0; i<expandedLayout.getChildCount(); i++)
        {
            EditText t = expandedLayout.getChildAt(i).findViewById(R.id.exercise_name_edittext);
            if (t.getText().equals(exerciseName))
                return i;
        }
        return -1;
    }

    public void DeleteExerciseEditModeOnClick(Object sender, EventArgs e, LinearLayout expandedLayout, int i)
    {
        expandedLayout.removeViewAt(i);
    }

    public void WorkoutNameTextChangeEvent(Object sender, TextChangedEventArgs e, Button saveChangesBtn)
    {
        saveChangesBtn.isEnabled() = e.getText().toString().length() > 0;
    }

    public void SaveChangesOnClick(Object sender, EventArgs e, WorkoutViewHolder workoutHolder, int position)
    {
        try
        {
            data.get(position).setTitle(workoutHolder.NewWorkoutName.getText().toString());
            data.get(position).getExercises().equals("");
            for (int i = 0; i < workoutHolder.ExpandedLayout.getChildCount(); i++)
            {
                EditText t = workoutHolder.ExpandedLayout.getChildAt(i).findViewById(R.id.exercise_name_edittext);
                String exercise = t.getText().toString();
                if (exercise.length() > 0)
                {
                    data.get(position).exercises += exercise + (i == workoutHolder.ExpandedLayout.getChildCount() - 1 ? "" : "\n"); // TODO fix this ?
                }
            }
            data.get(position).setEditModeExisting(false);
            NotifyItemChanged(position);
        } catch (Exception error)
        {
            Log.d("SHIT", "some shit happened "+error.getMessage());
        }
    }

    // edit mode for newly added workout
    private void ToggleEditModeNewWorkout(boolean edit, WorkoutViewHolder workoutHolder, int position)
    {
        if (edit)
        {
            workoutHolder.RootWorkoutLayout.setVisibility(View.GONE);
            workoutHolder.EditModeRoot.setVisibility(View.VISIBLE);

            ShowKeyboard(workoutHolder.NewWorkoutName);

            workoutHolder.NewWorkoutName.FocusChange += new EventHandler<View.FocusChangeEventArgs>((sender, e) =>
            {
                String newWorkoutName = workoutHolder.NewWorkoutName.getText().toString();
                if (!e.HasFocus && newWorkoutName.length() > 0)
                {
                    // save new workout name
                    if (position > -1 && data.size() > position)
                    {
                        data.get(position).setTitle(newWorkoutName);
                        data.get(position).setEditNewWorkout(false);
                        HideKeyboard(workoutHolder.NewWorkoutName);
                    }
                }
            });
            workoutHolder.NewWorkoutName.EditorAction += (sender, e) => {
            if (e.ActionId == ImeAction.Done)
            {
                String newWorkoutName = workoutHolder.NewWorkoutName.getText().toString();
                if (newWorkoutName.length() > 0)
                {
                    // save new workout name
                    data.get(position).setTitle(newWorkoutName);
                    data.get(position).setEditNewWorkout(false);
                    data.get(position).setExpanded(true);
                    HideKeyboard(workoutHolder.NewWorkoutName);
                    NotifyItemChanged(position);
                }
            }
            else
            {
                e.Handled = false;
            }
        };
            if (!workoutHolder.DeleteWorkoutBtn.hasOnClickListeners())
            {
                workoutHolder.DeleteWorkoutBtn.Click += delegate (object senderDeleteWorkout, EventArgs eDeleteWorkout)
                {
                    if (workoutHolder.NewWorkoutName.getText().toString().length() > 0)
                    {
                        String newWorkoutName = workoutHolder.NewWorkoutName.getText().toString();
                        ShowConfirmDialog(context.getString(R.string.confirm_save_changes),
                                context.getString(R.string.save), context.getString(R.string.dont_save), position,
                                newWorkoutName, workoutHolder);
                    }
                    else
                    {
                        data.remove(position);
                        // NotifyItemRemoved(position);
                        NotifyDataSetChanged();
                        HideKeyboard(workoutHolder.NewWorkoutName);
                    }
                };
            }

        }
        else
        {
            workoutHolder.EditModeRoot.setVisibility(View.GONE);
            workoutHolder.RootWorkoutLayout.setVisibility(View.VISIBLE);

            // reset listeners
            workoutHolder.NewWorkoutName.FocusChange -= new EventHandler<View.FocusChangeEventArgs>((sender, e) =>
            {
                String newWorkoutName = workoutHolder.NewWorkoutName.getText().toString();
                if (!e.HasFocus && newWorkoutName.length() > 0)
                {
                    // save new workout name
                    if (position > -1 && data.size() > position)
                    {
                        data.get(position).setTitle(newWorkoutName);
                        data.get(position).setEditNewWorkout(false);
                        HideKeyboard(workoutHolder.NewWorkoutName);
                    }
                }
            });
            workoutHolder.NewWorkoutName.EditorAction -= (sender, e) => {
            if (e.ActionId == ImeAction.Done)
            {
                String newWorkoutName = workoutHolder.NewWorkoutName.getText().toString();
                if (newWorkoutName.length() > 0)
                {
                    // save new workout name
                    data.get(position).setTitle(newWorkoutName);
                    data.get(position).setEditNewWorkout(false);
                    data.get(position).setExpanded(true);
                    HideKeyboard(workoutHolder.NewWorkoutName);
                    NotifyItemChanged(position);
                }
            }
            else
            {
                e.Handled = false;
            }
        };
            if (workoutHolder.DeleteWorkoutBtn.hasOnClickListeners())
            {
                workoutHolder.DeleteWorkoutBtn.Click -= delegate (object senderDeleteWorkout, EventArgs eDeleteWorkout)
                {
                    if (workoutHolder.NewWorkoutName.getText().toString().length() > 0)
                    {
                        String newWorkoutName = workoutHolder.NewWorkoutName.getText().toString();
                        ShowConfirmDialog(context.getString(R.string.confirm_save_changes),
                                context.getString(R.string.save), context.getString(R.string.dont_save), position,
                                newWorkoutName, workoutHolder);
                    }
                    else
                    {
                        data.remove(position);
                        // NotifyItemRemoved(position);
                        NotifyDataSetChanged();
                        HideKeyboard(workoutHolder.NewWorkoutName);
                    }
                };
            }

        }
    }

    private void ShowDialogDeleteWorkout(int position)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        AlertDialog dialog = null;
        builder.setTitle("Are you sure you want to delete this workout?");
        builder.setPositiveButton("Delete", delegate (object s, DialogClickEventArgs ev) {
        if (position > -1 && position < data.size())
        {
            data.remove(position);
            NotifyDataSetChanged();
        }
    });
        builder.setNegativeButton("Cancel", delegate (object s2, DialogClickEventArgs ev2) {
        if (dialog != null) dialog.dismiss();
    });

        dialog = builder.create();
        dialog.show();
    }

    public void ShowConfirmDialog(String title, String pos, String neg, int adapterPosition, String newWorkoutName, WorkoutViewHolder workoutHolder)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setPositiveButton(pos, delegate (object s, DialogClickEventArgs ev) {
        // save new workout name
        data.get(adapterPosition).setTitle(newWorkoutName);
        data.get(adapterPosition).setEditNewWorkout(false);
        data.get(adapterPosition).setExpanded(true);
        HideKeyboard(workoutHolder.NewWorkoutName);
        NotifyItemChanged(adapterPosition);
    });
        builder.setNegativeButton()neg, delegate (object s2, DialogClickEventArgs ev2) {
        data.remove(adapterPosition);
        NotifyDataSetChanged();
        // NotifyItemRemoved(adapterPosition);
        HideKeyboard(workoutHolder.NewWorkoutName);
    });

        builder.create().show();

    }

    public void AddExerciseOnClick(Object sender, EventArgs e, Button addExerciseBtn, LinearLayout currentExpandedLayout, int currentPosition, CardView rootCardView)
    {
        View newExerciseView = LayoutInflater.from(context).inflate(R.layout.list_item_exercise_edit_mode, null);
        LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ll.topMargin = 8; ll.bottomMargin = 8;
        newExerciseView.setLayoutParams(ll);
        currentExpandedLayout.addView(newExerciseView);
        // save view's current position for later use
        int viewPosition = currentExpandedLayout.getChildCount() - 1;

        AutoCompleteTextView exerciseEdittext = newExerciseView.findViewById(R.id.exercise_name_edittext);
        SetSuggestions(exerciseEdittext);

        ShowKeyboard(exerciseEdittext);

        ConfigureExerciseTrashcanListener(newExerciseView, viewPosition, currentExpandedLayout, exerciseEdittext);

        // TODO show exercise suggestions

        exerciseEdittext.EditorAction += (senderExerciseEt, eExerciseEt) => {
        if (eExerciseEt.ActionId == ImeAction.Done)
        {
            String newExerciseName = exerciseEdittext.getText().toString();
            if (newExerciseName.length() > 0)
            {
                OnAddExerciseEvent(exerciseEdittext, currentPosition, currentExpandedLayout, ll, viewPosition, rootCardView);
            };
        }
        else
        {
            eExerciseEt.Handled = false;
        }
    };
        // exerciseEdittext.FocusChange +=

    }

    private void SetSuggestions(AutoCompleteTextView text)
    {
        //Suggestions
        String[] autoCompleteOptions = new String[]{"Lat Pulldown", "Barbell Bench Press", "Dumbbell Bench Press", "Pull Up", "Chin Up", "Dumbbell Shoulder Press",
                "Dumbbell Lateral Raise", "Lateral Raise Machine", "Dumbbell Front Raise", "Dumbbell Row", "Barbell Row", "Rowing Machine", "Rear Delt Fly Machine", "Chest Fly Machine",
                "Pec Deck", "Deadlift", "Barbell Back Squat", "Barbell Front Squat", "Leg Press Horizontal", "Leg Press Vertical", "Calf Raise", "Glute Machine", "Glute Bridge",
                "Arnold Press", "T Bar Row", "Cable Fly", "Dumbbell Kick Back", "Narrow Bench Press", "Rope Pushdown", "Dip", "Push Up", "Straight Bar Pushdown", "Barbell Curl",
                "Dumbbell Curl", "Hammer Curl", "EZ Bar Curl", "Incline Barbell Bench Press", "Decline Barbell Bench Press", "Incline Dumbbell Press", "Decline Dumbbell Press",
                "Dumbbell Fly", "Farmer Walk", "Over Head Press", "Seated Dumbbell Curl", "Concentration Curl", "Hack Squat", "Smith Machine Lunge", "Smith Machine Press",
                "Smith Machine Over Head Press", "Seated Calf Raise", "Crunch", "Sit Up", "Leg Raise", "Handing Leg Raise", "Rope Crunch", "Rope Tricep Extension",
                "Behind Head Dumbbell Extension", "Read Delt Row", "Dumbbell Shrug", "Hyperextension"};
        ArrayAdapter autoCompleteAdapter = new ArrayAdapter(context, Android.R.layout.SimpleDropDownItem1Line, autoCompleteOptions);
        text.setAdapter(autoCompleteAdapter);
    }

    private void ConfigureExerciseTrashcanListener(View newExerciseView, int viewPosition, LinearLayout currentExpandedLayout, EditText exerciseEdittext)
    {
        ImageButton deleteExerciseBtn = newExerciseView.findViewById(R.id.delete_exercise_btn);
        if (!deleteExerciseBtn.hasOnClickListeners())
        {
            deleteExerciseBtn.Click += delegate (object sender, EventArgs e)
            {
                currentExpandedLayout.removeViewAt(viewPosition);
                HideKeyboard(exerciseEdittext);
            };
        };

    }

    public void OnAddExerciseEvent(EditText exerciseEdittext, int currentPosition,
                                   LinearLayout currentExpandedLayout, LinearLayout.LayoutParams ll, int index, CardView rootCardView)
    {
        HideKeyboard(exerciseEdittext);

        // format exercises list
        if (currentPosition > -1 && currentPosition < data.size())
            data.get(currentPosition).exercises += (data.get(currentPosition).getExercises() != null ? "\n" : "") + exerciseEdittext.getText().toString();

        // replace edit view with normal exercise view 
        currentExpandedLayout.removeViewAt(index);
        View exerciseView = LayoutInflater.from(context).inflate(R.layout.list_item_exercise, null);
        EditText t = exerciseView.findViewById(R.id.exercise_name);
        t.setText(exerciseEdittext.getText().toString());
        ll.leftMargin = 24; ll.rightMargin = 24;
        exerciseView.LayoutParameters = ll;
        currentExpandedLayout.addView(exerciseView, index);

        LinearLayout rootExerciseItem = exerciseView.findViewById(R.id.root_exercise_item);
        Button addSetBtn = exerciseView.findViewById(R.id.add_set_btn);

        if (!rootExerciseItem.hasOnClickListeners())
        {
            rootExerciseItem.Click += delegate (object rootSender, EventArgs rootE) { ExerciseItemOnClick(rootSender, rootE); };
        }

        if (!addSetBtn.hasOnClickListeners())
        {
            addSetBtn.Click += delegate (object addSetSender, EventArgs addSetE) { ExerciseItemOnClick(addSetSender, addSetE); };
        }
    }

    private void ShowKeyboard(View pView)
    {
        pView.requestFocus();

        InputMethodManager inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager;
        inputMethodManager.showSoftInput(pView, ShowFlags.Forced);
        inputMethodManager.toggleSoftInput(ShowFlags.Forced, HideSoftInputFlags.ImplicitOnly);
    }

    private void HideKeyboard(EditText et)
    {
        if (et.hasFocus())
            et.clearFocus();
        InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        ((InputMethodManager) imm).hideSoftInputFromWindow(et.getWindowToken(), 0);
    }

    public void ExerciseItemOnClick(object sender, EventArgs e)
    {
        int senderId = ((View)sender).Id;
        if (senderId == R.id.add_set_btn)
        {
            View setView = LayoutInflater.From(context).Inflate(R.layout.exercise_set_item, null);
            LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            ll.topMargin = 8; ll.bottomMargin = 8;
            setView.setLayoutParams(ll);
            ((((Button)sender).Parent as LinearLayout).getChildAt(2) as LinearLayout).addView(setView);
            EditText target = setView.findViewById(R.id.target_weight);
            EditText finalWeight = setView.findViewById(R.id.final_weight);
            setKeyboard(target);
            setKeyboard(finalWeight);

        }
        else if (senderId == R.id.root_exercise_item)
        {
            if (((LinearLayout)sender).findViewById(R.id.layout_set).getVisibility() == View.GONE)
            {
                // expand
                ((LinearLayout)sender).findViewById(R.id.layout_set).setVisibility(View.VISIBLE);
                ((LinearLayout)sender).findViewById(R.id.layout_set_title).setVisibility(View.VISIBLE);
                ((LinearLayout)sender).findViewById(R.id.add_set_btn).setVisibility(View.VISIBLE);

                //set keyboard for both EditTexts where user enters weight
                EditText target = ((LinearLayout)sender).findViewById(R.id.target_weight);
                EditText finalWeight = ((LinearLayout)sender).findViewById(R.id.final_weight);
                setKeyboard(target);
                setKeyboard(finalWeight);
            }
            else
            {
                // collapse
                LinearLayout setsLayout = ((LinearLayout)sender).findViewById(R.id.layout_set);
                setsLayout.setVisibility(View.GONE);
                ((LinearLayout)sender).findViewById(R.id.layout_set_title).setVisibility(View.GONE);
                ((LinearLayout)sender).findViewById(R.id.add_set_btn).setVisibility(View.GONE);
                var im = ((InputMethodManager)context.getSystemService(Android.Content.Context.InputMethodService));

                if (!((LinearLayout)sender).findViewById(R.id.target_weight).HasFocus)
                {
                    im.HideSoftInputFromWindow(((LinearLayout)sender).findViewById(R.id.target_weight).WindowToken, 0);
                };

                CleanUpEmptySets(setsLayout, 1);
            }
        }

    }

    private void CleanUpEmptySets(LinearLayout parent, int maxCount)
    {
        if (maxCount <= parent.getChildCount())
        {
            for (int i = 1; i < parent.getChildCount(); i++)
            {
                View setView = parent.getChildAt(i);
                
                EditText t1 = setView.findViewById(R.id.target_sets);
                EditText t2 = setView.findViewById(R.id.target_weight);
                EditText t3 = setView.findViewById(R.id.final_sets);
                EditText t4 = setView.findViewById(R.id.final_weight);

                EditText t5 = parent.getChildAt(0).findViewById(R.id.target_sets);
                EditText t6 = parent.getChildAt(0).findViewById(R.id.target_weight);
                EditText t7 = parent.getChildAt(0).findViewById(R.id.final_sets);
                EditText t8 = parent.getChildAt(0).findViewById(R.id.final_weight);
                
                if (t1.getText().length() == 0 &&
                        t2.getText().length() == 0 &&
                        t3.getText().length() == 0 &&
                        t4.getText().length() == 0)
                {
                    parent.removeViewAt(i);
                    maxCount--;
                    break;
                } else if (t5.getText().length() == 0 &&
                        t6.getText().length() == 0 &&
                        t7.getText().length() == 0 &&
                        t8.getText().length() == 0)
                {
                    t5.setText(t1.getText());
                    t6.setText(t2.getText());
                    t7.setText(t3.getText());
                    t8.setText(t4.getText());
                    parent.removeViewAt(i);
                    maxCount--;
                    break;
                }
            }
            CleanUpEmptySets(parent, maxCount+1);
        }
    }

    private void setKeyboard(EditText target)
    {
        if (target != null && keyboard!=null) // temporary null check until bug is fixed
        {
            //.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
            target.setRawInputType(InputType.TYPE_CLASS_TEXT);
            target.setTextIsSelectable(true);
            target.Touch += delegate
            {
                keyboard.setCurrentEditText(target);
                InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(root_view.getWindowToken(), 0);
                keyboard.setVisibility(View.VISIBLE);
            };
        }
    }

    public override RecyclerView.ViewHolder OnCreateViewHolder(ViewGroup parent, int viewType)
    {
        itemView = LayoutInflater.from(parent.Context).
                Inflate(R.layout.list_item_workout, parent, false);

        return new WorkoutViewHolder(itemView, OnClick);
    }

    // workout item onClick
    private void OnClick(int position)
    {
        if (ItemClick != null)
        {
            ItemClick(this, position);
        }
    }



    @Override
    public int getItemCount() {
        return data.size();
    }
}
