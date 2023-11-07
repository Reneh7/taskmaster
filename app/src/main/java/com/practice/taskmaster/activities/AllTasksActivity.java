package com.practice.taskmaster.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.practice.taskmaster.R;

public class AllTasksActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_tasks);

        BackButton();
    }

    private void BackButton() {
        Button allTasksBackButton = findViewById(R.id.allTaskBack);
        allTasksBackButton.setOnClickListener(view -> {
            Intent goBackToHome = new Intent(AllTasksActivity.this, HomeActivity.class);
            startActivity(goBackToHome);
        });
    }
}