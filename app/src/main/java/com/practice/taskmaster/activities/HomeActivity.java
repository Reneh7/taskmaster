package com.practice.taskmaster.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.practice.taskmaster.R;
import com.practice.taskmaster.database.TaskDatabase;
import com.practice.taskmaster.models.Task;
import com.practice.taskmaster.adapters.TaskAdapter;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TaskAdapter taskAdapter;
    public static  final String DATABASE_NAME = "tasks_stuff";
    TaskDatabase taskDatabase;
    List<Task> tasks=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        taskDatabase = Room.databaseBuilder(
                        getApplicationContext(),
                        TaskDatabase.class,
                        DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
        tasks= taskDatabase.taskDao().findAll();

        setUpTaskListRecyclerView();

        Button addTaskButton= findViewById(R.id.addTask);
        addTaskButton.setOnClickListener(view -> {
            Intent goToAddTaskFormIntent = new Intent(HomeActivity.this, AddTaskActivity.class);
            startActivity(goToAddTaskFormIntent);
        });

        //===============================================================

        Button allTaskButton=findViewById(R.id.allTasks);
        allTaskButton.setOnClickListener(view -> {
            Intent goToAllTasksIntent = new Intent(HomeActivity.this, AllTasksActivity.class);
            startActivity(goToAllTasksIntent);
        });

        //================================================================

        Button settingsPage=findViewById(R.id.settingsButton);
        settingsPage.setOnClickListener(view -> {
            Intent goToSettings=new Intent(HomeActivity.this, SettingsActivity.class);
            startActivity(goToSettings);
        });

        //=================================================================
    }

    @Override
    protected void onResume() {
        super.onResume();
        TextView user=findViewById(R.id.usernameTextView);
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "DefaultUsername");
        user.setText(username +"'s Tasks:");

        tasks.addAll(taskDatabase.taskDao().findAll());
        taskAdapter.notifyDataSetChanged();
    }

    private void setUpTaskListRecyclerView(){
        RecyclerView taskListRecycleReview = (RecyclerView) findViewById(R.id.recycleView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        taskListRecycleReview.setLayoutManager(layoutManager);
        taskAdapter = new TaskAdapter(tasks, this);
        taskListRecycleReview.setAdapter(taskAdapter);

    }
}