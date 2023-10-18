package com.practice.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class TaskDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);

        Button detailsBackButton=findViewById(R.id.taskDetailsBackButton);
        detailsBackButton.setOnClickListener(view -> {
            Intent backToHomeFromDetails= new Intent(TaskDetailsActivity.this,MainActivity.class);
            startActivity(backToHomeFromDetails);
        });

        TextView title=findViewById(R.id.taskDetailsTitle);
        Intent intent=getIntent();
        String taskTitle=intent.getStringExtra("taskTitle");
        if(taskTitle!=null)
            title.setText(taskTitle);

    }
}