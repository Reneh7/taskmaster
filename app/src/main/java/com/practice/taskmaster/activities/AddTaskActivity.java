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
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.TaskState;
import com.amplifyframework.datastore.generated.model.Team;
import com.google.android.material.snackbar.Snackbar;
import com.practice.taskmaster.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;


public class AddTaskActivity extends AppCompatActivity {
    int x=1;
    public static final String TAG="AddTaskActivity";
    CompletableFuture<List<Team>> teamFuture=new CompletableFuture<>();

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
//        teamFuture=new CompletableFuture<>();
        SubmitButton();
        backButton();
    }

    private void SubmitButton(){

        // TaskStateSpinner
        Spinner taskCategorySpinner = (Spinner) findViewById(R.id.spinner);
        taskCategorySpinner.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                TaskState.values()));

        // TeamSpinner
        Spinner teamSpinner = (Spinner) findViewById(R.id.teamSpinner);
        Amplify.API.query(
                ModelQuery.list(Team.class),
                successResponse->{
           Log.i(TAG,"Read Team Successfully");
                   ArrayList<String> teamNames=new ArrayList<>();
                   ArrayList<Team> teams=new ArrayList<>();
                   for (Team team:successResponse.getData()){
                       teams.add(team);
                       teamNames.add(team.getName());
                   }
                   teamFuture.complete(teams);
                   runOnUiThread(()->{
                       teamSpinner.setAdapter(new ArrayAdapter<>(
                               this,
                               android.R.layout.simple_spinner_item,
                               teamNames
                       ));
                   });
               },
               failure->
               {
                   teamFuture.complete(null);
                   Log.i(TAG,"Failed to read team");
               }
       );

        Button submitButton= findViewById(R.id.add);
        submitButton.setOnClickListener(view -> {
            Toast.makeText(this, "Submitted!", Toast.LENGTH_SHORT).show();
//            Task newTask=new Task(
//                    ((EditText) findViewById(R.id.taskTitle)).getText().toString(),
//                    ((EditText) findViewById(R.id.taskBody)).getText().toString(),
//                    TaskState.fromString(taskCategorySpinner.getSelectedItem().toString())
//            );
//            taskDatabase.taskDao().insertATask(newTask);
            // save in database
            String title=((EditText) findViewById(R.id.taskTitle)).getText().toString();
            String body= ((EditText) findViewById(R.id.taskBody)).getText().toString();
            TaskState state=(TaskState) taskCategorySpinner.getSelectedItem();
            String selectedTeamString= teamSpinner.getSelectedItem().toString();

            List<Team> teams=null;
            try {
                teams=teamFuture.get();
            }catch (InterruptedException e){
                Log.e(TAG,"Interruption exception while getting teams");
            }catch (ExecutionException ee){
                Log.e(TAG,"Execution exception while getting teams");
            }

            Team selectedTeams= teams.stream().filter(c->c.getName().equals(selectedTeamString)).findAny().orElseThrow(RuntimeException::new);
            Task newTask=Task.builder()
                    .name(title)
                    .body(body)
                    .state(state)
                    .teamTask(selectedTeams)
                    .build();
            Amplify.API.mutate(
                    ModelMutation.create(newTask),
                    successResponse -> Log.i(TAG,"AddTaskActivity.onCreate(): created a task successfully"),
                    failResponse -> Log.i(TAG,"AddTaskActivity.onCreate(): failed to create a task"+failResponse)
                    );

            // counter
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