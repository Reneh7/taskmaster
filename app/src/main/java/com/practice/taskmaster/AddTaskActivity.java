package com.practice.taskmaster;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class AddTaskActivity extends AppCompatActivity {
    int x=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        Button addTaskBackButton= findViewById(R.id.addTaskButton);
        Button submitButton= findViewById(R.id.add);

        submitButton.setOnClickListener(view -> {
            Toast.makeText(this, "Submitted!", Toast.LENGTH_SHORT).show();
            TextView count=findViewById(R.id.counter);
            count.setText(String.valueOf(x++));
        });

        addTaskBackButton.setOnClickListener(view -> {
            Intent goBackToHome = new Intent(AddTaskActivity.this,MainActivity.class);
            startActivity(goBackToHome);

        });
    }
}