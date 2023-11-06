package com.practice.taskmaster.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.practice.taskmaster.R;
public class TaskDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);

        TaskDetails();
        BackButton();
    }

    private void TaskDetails(){
        TextView title=findViewById(R.id.taskDetailsTitle);
        TextView body = findViewById(R.id.taskDetailsBody);
        TextView status = findViewById(R.id.taskDetailsStatus);

        Intent intent=getIntent();
        String taskTitle=intent.getStringExtra("taskTitle");
        String taskBody = intent.getStringExtra("taskBody");
        String taskStatus = intent.getStringExtra("taskStatus");

        if(taskTitle!=null)
            title.setText(taskTitle);
        if (taskBody != null)
            body.setText(taskBody);
        if (taskStatus != null)
            status.setText(taskStatus);
    }

    private void BackButton(){
        Button detailsBackButton=findViewById(R.id.taskDetailsBackButton);
        detailsBackButton.setOnClickListener(view -> {
            Intent backToHomeFromDetails= new Intent(TaskDetailsActivity.this,HomeActivity.class);
            startActivity(backToHomeFromDetails);
        });
    }

}
