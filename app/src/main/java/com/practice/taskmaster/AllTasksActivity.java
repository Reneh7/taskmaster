package com.practice.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class AllTasksActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_tasks);

        Button allTasksBackButton = findViewById(R.id.allTaskBack);
        allTasksBackButton.setOnClickListener(view -> {
            Intent goBackToHome = new Intent(AllTasksActivity.this,MainActivity.class);
            startActivity(goBackToHome);
        });
    }
}