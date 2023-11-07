package com.practice.taskmaster.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.TaskState;
import com.practice.taskmaster.R;



public class AddTaskActivity extends AppCompatActivity {
    int x=0;
    public static final String TAG="AddTaskActivity";
//    TaskDatabase taskDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

//        taskDatabase = Room.databaseBuilder(
//                        getApplicationContext(),
//                        TaskDatabase.class
//                        ,"tasks_stuff")
//                .allowMainThreadQueries()
//                .build();

        SubmitButton();
        backButton();
    }

    private void SubmitButton(){
        Spinner taskCategorySpinner = (Spinner) findViewById(R.id.spinner);
        taskCategorySpinner.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                TaskState.values()));

        Button submitButton= findViewById(R.id.add);
        submitButton.setOnClickListener(view -> {
            Toast.makeText(this, "Submitted!", Toast.LENGTH_SHORT).show();
//            Task newTask=new Task(
//                    ((EditText) findViewById(R.id.taskTitle)).getText().toString(),
//                    ((EditText) findViewById(R.id.taskBody)).getText().toString(),
//                    TaskState.fromString(taskCategorySpinner.getSelectedItem().toString())
//            );
//            taskDatabase.taskDao().insertATask(newTask);
            String title=((EditText) findViewById(R.id.taskTitle)).getText().toString();
            String body= ((EditText) findViewById(R.id.taskBody)).getText().toString();
            TaskState state=(TaskState) taskCategorySpinner.getSelectedItem();
            Task newTask=Task.builder()
                    .name(title)
                    .body(body)
                    .state(state).build();
            Amplify.API.mutate(
                    ModelMutation.create(newTask),
                    successResponse -> Log.i(TAG,"AddTaskActivity.onCreate(): created a task successfully"),
                    failResponse -> Log.i(TAG,"AddTaskActivity.onCreate(): failed to create a task"+failResponse)
                    );

            TextView count=findViewById(R.id.counter);
            count.setText(String.valueOf(x++));
        });
    }

    private void backButton(){
        Button addTaskBackButton= findViewById(R.id.addTaskBackButton);
        addTaskBackButton.setOnClickListener(view -> {
            Intent goBackToHome = new Intent(AddTaskActivity.this, HomeActivity.class);
            startActivity(goBackToHome);
        });
    }

}