package com.practice.taskmaster.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.TaskState;
import com.practice.taskmaster.R;

import com.practice.taskmaster.adapters.TaskAdapter;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

//    public static  final String DATABASE_NAME = "tasks_stuff";
//    TaskDatabase taskDatabase;
    public static final String TAG="homeActivity";
    private TaskAdapter taskAdapter;
    List<Task> tasks=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

//        taskDatabase = Room.databaseBuilder(
//                        getApplicationContext(),
//                        TaskDatabase.class,
//                        DATABASE_NAME)
//                .fallbackToDestructiveMigration()
//                .allowMainThreadQueries()
//                .build();
//        tasks= taskDatabase.taskDao().findAll();

        amplifier();
        setUpTaskListRecyclerView();
        AddTaskButton();
        AllTasksButton();
        SettingsButton();

    }

    // Shared Preference
    @Override
    protected void onResume() {
        super.onResume();
        TextView user=findViewById(R.id.usernameTextView);
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "DefaultUsername");
        user.setText(username +"'s Tasks:");

//        tasks.addAll(taskDatabase.taskDao().findAll());
//        taskAdapter.notifyDataSetChanged();
    }

    public void amplifier(){
        Amplify.API.query(
                ModelQuery.list(Task.class),
                success->{
                    Log.i(TAG,"Read tasks successfully");
                        tasks.clear();
                        for (Task databaseTask : success.getData()) {
                            tasks.add(databaseTask);
                        }
                        runOnUiThread(() -> {
                            taskAdapter.notifyDataSetChanged();
                        });
                },
                failure-> Log.i(TAG,"failed to read tasks")
        );
    }

    private void setUpTaskListRecyclerView(){
//        tasks.add(new Task("Cleaning","First Task", TaskState.NEW));
        RecyclerView taskListRecycleReview = (RecyclerView) findViewById(R.id.recycleView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        taskListRecycleReview.setLayoutManager(layoutManager);
        taskAdapter = new TaskAdapter(tasks, this);
        taskListRecycleReview.setAdapter(taskAdapter);

    }

    private void AddTaskButton() {
        Button addTaskButton = findViewById(R.id.addTask);
        addTaskButton.setOnClickListener(view -> {
            Intent goToAddTaskFormIntent = new Intent(HomeActivity.this, AddTaskActivity.class);
            startActivity(goToAddTaskFormIntent);
        });
    }

    private void AllTasksButton() {
        Button allTaskButton = findViewById(R.id.allTasks);
        allTaskButton.setOnClickListener(view -> {
            Intent goToAllTasksIntent = new Intent(HomeActivity.this, AllTasksActivity.class);
            startActivity(goToAllTasksIntent);
        });
    }


    private void SettingsButton() {
        Button settingsPage = findViewById(R.id.settingsButton);
        settingsPage.setOnClickListener(view -> {
            Intent goToSettings = new Intent(HomeActivity.this, SettingsActivity.class);
            startActivity(goToSettings);
        });
    }
}