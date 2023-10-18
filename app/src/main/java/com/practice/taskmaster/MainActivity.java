package com.practice.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button addTaskButton= findViewById(R.id.addTask);
        addTaskButton.setOnClickListener(view -> {
            Intent goToAddTaskFormIntent = new Intent(MainActivity.this, AddTaskActivity.class);
            startActivity(goToAddTaskFormIntent);
        });

        //===============================================================

        Button allTaskButton=findViewById(R.id.allTasks);
        allTaskButton.setOnClickListener(view -> {
            Intent goToAllTasksIntent = new Intent(MainActivity.this, AllTasksActivity.class);
            startActivity(goToAllTasksIntent);
        });

        //================================================================

        Button settingsPage=findViewById(R.id.settingsButton);
        settingsPage.setOnClickListener(view -> {
            Intent goToSettings=new Intent(MainActivity.this,SettingsActivity.class);
            startActivity(goToSettings);
        });

        //=================================================================

        TextView user=findViewById(R.id.usernameTextView);
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "DefaultUsername");
        user.setText(username +"'s Tasks:");

        //==================================================================

        Button firstTask=findViewById(R.id.task1);
        firstTask.setOnClickListener(view -> {
            Intent sendTitle=new Intent(MainActivity.this,TaskDetailsActivity.class);
            sendTitle.putExtra("taskTitle","Cleaning");
            startActivity(sendTitle);
        });


        Button secondTask=findViewById(R.id.task2);
        secondTask.setOnClickListener(view -> {
            Intent sendTitle=new Intent(MainActivity.this,TaskDetailsActivity.class);
            sendTitle.putExtra("taskTitle","Studying");
            startActivity(sendTitle);
        });


        Button thirdTask=findViewById(R.id.task3);
        thirdTask.setOnClickListener(view -> {
            Intent sendTitle=new Intent(MainActivity.this,TaskDetailsActivity.class);
            sendTitle.putExtra("taskTitle","Playing");
            startActivity(sendTitle);
        });
    }
}