package com.practice.taskmaster.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.auth.AuthUser;
import com.amplifyframework.auth.AuthUserAttribute;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.Team;
import com.practice.taskmaster.R;

import com.practice.taskmaster.adapters.TaskAdapter;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    public static final String TASK_ID_TAG="task_Id_Tag";
    //    public static  final String DATABASE_NAME = "tasks_stuff";
//    TaskDatabase taskDatabase;
    private String selectedTeam;
    public static final String TAG="homeActivity";
    private TaskAdapter taskAdapter;
    List<Task> tasks=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

//        taskDatabase = Room.databaseBuilder(
//                        getApplicationContext(),
//                        TaskDatabase.class,
//                        DATABASE_NAME)
//                .fallbackToDestructiveMigration()
//                .allowMainThreadQueries()
//                .build();
//        tasks= taskDatabase.taskDao().findAll();

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        selectedTeam = sharedPreferences.getString(SettingsActivity.TEAM_TAG, "");

        setUpLoginAndLogOutButton();
        amplifier();
        setUpTaskListRecyclerView();
        queryTasks();
        AddTaskButton();
        AllTasksButton();
        SettingsButton();
    }

    // Shared Preference
    @Override
    protected void onResume() {
        super.onResume();
        TextView user=findViewById(R.id.usernameTextView);
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "DefaultUsername");
        user.setText(username +"'s Tasks:");

        AuthUser authUser=Amplify.Auth.getCurrentUser();
        if(authUser==null){
            Button loginButton=findViewById(R.id.loginButtonInHome);
            loginButton.setVisibility(View.VISIBLE);
            Button logoutButton=findViewById(R.id.logOutButtonInHome);
            logoutButton.setVisibility(View.INVISIBLE);
        }else {
            String nickName =authUser.getUsername();
            Log.i(TAG,"UserName"+nickName);
            Button loginButton=findViewById(R.id.loginButtonInHome);
            loginButton.setVisibility(View.INVISIBLE);
            Button logoutButton=findViewById(R.id.logOutButtonInHome);
            logoutButton.setVisibility(View.VISIBLE);
        }
        //==================================================================
        String username2 = username;
        Amplify.Auth.fetchUserAttributes(
                success ->
                {
                    Log.i(TAG, "Fetch user attributes succeeded for username: "+username2);
                    for (AuthUserAttribute userAttribute: success){
                        if(userAttribute.getKey().getKeyString().equals("email")){
                            String userEmail = userAttribute.getValue();
                            runOnUiThread(() ->
                            {
                                ((TextView)findViewById(R.id.userNicknameTextView)).setText(userEmail);
                            });
                        }
                    }
                },
                failure ->
                {
                    Log.i(TAG, "Fetch user attributes failed: "+failure.toString());
                }
        );
//        tasks.addAll(taskDatabase.taskDao().findAll());
//        taskAdapter.notifyDataSetChanged();
    }

    public void amplifier(){
//        Team team1=Team.builder()
//                .name("Reneh").build();
//
//        Team team2=Team.builder()
//                .name("Balqees").build();
//
//        Team team3=Team.builder()
//                .name("Farah").build();
//
//        Amplify.API.mutate(
//                ModelMutation.create(team1),
//                successResponse->Log.i(TAG,"HomeActivity.amplifier(): made team successfully."),
//                failedResponse->Log.i(TAG,"HomeActivity.amplifier(): failed to make team."+failedResponse)
//        );
//
//        Amplify.API.mutate(
//                ModelMutation.create(team2),
//                successResponse->Log.i(TAG,"HomeActivity.amplifier(): made team successfully."),
//                failedResponse->Log.i(TAG,"HomeActivity.amplifier(): failed to make team."+failedResponse)
//        );
//
//        Amplify.API.mutate(
//                ModelMutation.create(team3),
//                successResponse->Log.i(TAG,"HomeActivity.amplifier(): made team successfully."),
//                failedResponse->Log.i(TAG,"HomeActivity.amplifier(): failed to make team."+failedResponse)
//        );

        Amplify.API.query(
                ModelQuery.list(Task.class),
                success->{
                    Log.i(TAG,"Read tasks successfully");
                    tasks.clear();
                    for (Task databaseTask : success.getData()) {;
                        tasks.add(databaseTask);
                    }
                    runOnUiThread(() -> {
                        taskAdapter.notifyDataSetChanged();
                    });
                },
                failure-> Log.i(TAG,"failed to read tasks")
        );
    }

    private void queryTasks() {
        Amplify.API.query(
                ModelQuery.list(Task.class),
                success -> {
                    Log.i(TAG, "Read Task successfully");
                    tasks.clear();
                    for (Task databaseTask : success.getData()) {
                        Team teamTask = databaseTask.getTeamTask();
                        if (teamTask != null && teamTask.getName().equals(selectedTeam)) {
                            tasks.add(databaseTask);
                        }
                    }
                    runOnUiThread(() -> {
                        taskAdapter.notifyDataSetChanged();
                    });
                },
                failure -> Log.i(TAG, "Couldn't read tasks from DynamoDB ")
        );
    }


    private void setUpTaskListRecyclerView(){
        RecyclerView taskListRecycleReview = (RecyclerView) findViewById(R.id.recycleView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        taskListRecycleReview.setLayoutManager(layoutManager);
        taskAdapter = new TaskAdapter(tasks, this);
        taskListRecycleReview.setAdapter(taskAdapter);

    }

    private void AddTaskButton() {
        Button addTaskButton = findViewById(R.id.addTask);
        addTaskButton.setOnClickListener(view -> {
            Intent goToAddTaskFormIntent = new Intent(HomeActivity.this, AddTaskActivity.class);
            startActivity(goToAddTaskFormIntent);
        });
    }

    private void AllTasksButton() {
        Button allTaskButton = findViewById(R.id.allTasks);
        allTaskButton.setOnClickListener(view -> {
            Intent goToAllTasksIntent = new Intent(HomeActivity.this, AllTasksActivity.class);
            startActivity(goToAllTasksIntent);
        });
    }


    private void SettingsButton() {
        Button settingsPage = findViewById(R.id.settingsButton);
        settingsPage.setOnClickListener(view -> {
            Intent goToSettings = new Intent(HomeActivity.this, SettingsActivity.class);
            startActivity(goToSettings);
        });
    }

    public void setUpLoginAndLogOutButton(){
        Button loginButton=findViewById(R.id.loginButtonInHome);
        loginButton.setOnClickListener(v->{
            Intent goToLogin = new Intent(HomeActivity.this, LogInActivity.class);
            startActivity(goToLogin);
        });

        Button logoutButton=findViewById(R.id.logOutButtonInHome);
        logoutButton.setOnClickListener(v->{
            Amplify.Auth.signOut(
                    ()-> {Log.i(TAG,"logOut successfully");
                        runOnUiThread(()->{
                            ((TextView)findViewById(R.id.userNicknameTextView)).setText("");
                        });
                        Intent goToLogin = new Intent(HomeActivity.this, LogInActivity.class);
                        startActivity(goToLogin);
                        },
                    failure->{Log.i(TAG,"logOut failed");
                        runOnUiThread(()-> {
                            Toast.makeText(HomeActivity.this,"Logout Failed",Toast.LENGTH_LONG);
                        });
                    });
        });
    }
}