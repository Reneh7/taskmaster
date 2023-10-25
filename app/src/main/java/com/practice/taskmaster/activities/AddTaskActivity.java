package com.practice.taskmaster.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.practice.taskmaster.R;
import com.practice.taskmaster.database.TaskDatabase;
import com.practice.taskmaster.enums.TaskState;
import com.practice.taskmaster.models.Task;

public class AddTaskActivity extends AppCompatActivity {
    int x=0;
    TaskDatabase taskDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        taskDatabase = Room.databaseBuilder(
                        getApplicationContext(),
                        TaskDatabase.class
                        ,"tasks_stuff")
                .allowMainThreadQueries()
                .build();
        Spinner taskCategorySpinner = (Spinner) findViewById(R.id.spinner);
        taskCategorySpinner.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                TaskState.values()));

        Button addTaskBackButton= findViewById(R.id.addTaskBackButton);
        Button submitButton= findViewById(R.id.add);


        submitButton.setOnClickListener(view -> {
            Toast.makeText(this, "Submitted!", Toast.LENGTH_SHORT).show();
            Task newTask=new Task(
                    ((EditText) findViewById(R.id.taskTitle)).getText().toString(),
                    ((EditText) findViewById(R.id.taskBody)).getText().toString(),
                    TaskState.fromString(taskCategorySpinner.getSelectedItem().toString())
            );
            taskDatabase.taskDao().insertATask(newTask);
            TextView count=findViewById(R.id.counter);
            count.setText(String.valueOf(x++));
        });

        addTaskBackButton.setOnClickListener(view -> {
            Intent goBackToHome = new Intent(AddTaskActivity.this, HomeActivity.class);
            startActivity(goBackToHome);
        });
    }
}