package com.practice.taskmaster.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;


public class AddTaskActivity extends AppCompatActivity {
    int x=1;
    public static final String TAG="AddTaskActivity";
    CompletableFuture<List<Team>> teamFuture=new CompletableFuture<>();
    ActivityResultLauncher<Intent> activityResultLauncher;
    private String s3ImageKey = "";

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

        activityResultLauncher = getImagePickingActivityResultLauncher();
//        setUpNonButtonUiElements();

        setUpSubmitButton();
        backButton();
        setUpAddImageButton();
        setUpDeleteImageButton();
        updateImageButtons();
    }
    private void setUpSubmitButton() {
        Button saveButton = findViewById(R.id.add);
        saveButton.setOnClickListener(v -> {
            SubmitButton(s3ImageKey);
        });
    }

    private void SubmitButton(String imageS3Key){

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
                    .taskImageS3Key(imageS3Key)
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

    private void setUpAddImageButton(){
        Button addImageButton = (Button) findViewById(R.id.addTaskAddImageButton);
        addImageButton.setOnClickListener(b ->
        {
            launchImageSelectionIntent();
        });

    }

    private void launchImageSelectionIntent()
    {
        Intent imageFilePickingIntent = new Intent(Intent.ACTION_GET_CONTENT);
        imageFilePickingIntent.setType("*/*");
        imageFilePickingIntent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"image/jpeg", "image/png"});
        activityResultLauncher.launch(imageFilePickingIntent);

    }

    private ActivityResultLauncher<Intent> getImagePickingActivityResultLauncher()
    {
        ActivityResultLauncher<Intent> imagePickingActivityResultLauncher =
                registerForActivityResult(
                        new ActivityResultContracts.StartActivityForResult(),
                        new ActivityResultCallback<ActivityResult>()
                        {
                            @Override
                            public void onActivityResult(ActivityResult result)
                            {
                                Button addImageButton = findViewById(R.id.addTaskAddImageButton);
                                if (result.getResultCode() == Activity.RESULT_OK)
                                {
                                    if (result.getData() != null)
                                    {
                                        Uri pickedImageFileUri = result.getData().getData();
                                        try
                                        {
                                            InputStream pickedImageInputStream = getContentResolver().openInputStream(pickedImageFileUri);
                                            String pickedImageFilename = getFileNameFromUri(pickedImageFileUri);
                                            Log.i(TAG, "Succeeded in getting input stream from file on phone! Filename is: " + pickedImageFilename);

                                            switchFromAddButtonToDeleteButton(addImageButton);
                                            uploadInputStreamToS3(pickedImageInputStream, pickedImageFilename,pickedImageFileUri);

                                        } catch (FileNotFoundException fnfe)
                                        {
                                            Log.e(TAG, "Could not get file from file picker! " + fnfe.getMessage(), fnfe);
                                        }
                                    }
                                }
                                else
                                {
                                    Log.e(TAG, "Activity result error in ActivityResultLauncher.onActivityResult");
                                }
                            }
                        }
                );
        return imagePickingActivityResultLauncher;
    }

    private void uploadInputStreamToS3(InputStream pickedImageInputStream, String pickedImageFilename,Uri pickedImageFileUri)
    {
        Amplify.Storage.uploadInputStream(
                pickedImageFilename,
                pickedImageInputStream,
                success ->
                {
                    Log.i(TAG, "Succeeded in getting file uploaded to S3! Key is: " + success.getKey());

                    SubmitButton(success.getKey());
                    updateImageButtons();
                    ImageView taskImageView = findViewById(R.id.taskImageImageView);
                    InputStream pickedImageInputStreamCopy = null;
                    try
                    {
                        pickedImageInputStreamCopy = getContentResolver().openInputStream(pickedImageFileUri);
                    }
                    catch (FileNotFoundException fnfe)
                    {
                        Log.e(TAG, "Could not get file stream from URI! " + fnfe.getMessage(), fnfe);
                    }
                    taskImageView.setImageBitmap(BitmapFactory.decodeStream(pickedImageInputStreamCopy));

                },
                failure ->
                {
                    Log.e(TAG, "Failure in uploading file to S3 with filename: " + pickedImageFilename + " with error: " + failure.getMessage());
                }
        );
    }



    private void setUpDeleteImageButton()
    {
        Button deleteImageButton = (Button)findViewById(R.id.addTaskDeleteImageButton);
        String s3ImageKey = this.s3ImageKey;
        deleteImageButton.setOnClickListener(v ->
        {
            Amplify.Storage.remove(
                    s3ImageKey,
                    success ->
                    {
                        Log.i(TAG, "Succeeded in deleting file on S3! Key is: " + success.getKey());

                    },
                    failure ->
                    {
                        Log.e(TAG, "Failure in deleting file on S3 with key: " + s3ImageKey + " with error: " + failure.getMessage());
                    }
            );
            ImageView productImageView = findViewById(R.id.taskImageImageView);
            productImageView.setImageResource(android.R.color.transparent);

            SubmitButton("");
            switchFromDeleteButtonToAddButton(deleteImageButton);
        });
    }

    private void updateImageButtons() {
        Button addImageButton = findViewById(R.id.addTaskAddImageButton);
        Button deleteImageButton = findViewById(R.id.addTaskDeleteImageButton);
        runOnUiThread(() -> {
            if (s3ImageKey.isEmpty()) {
                deleteImageButton.setVisibility(View.INVISIBLE);
                addImageButton.setVisibility(View.VISIBLE);
            } else {
                deleteImageButton.setVisibility(View.VISIBLE);
                addImageButton.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void switchFromDeleteButtonToAddButton(Button deleteImageButton) {
        Button addImageButton = findViewById(R.id.addTaskAddImageButton);
        deleteImageButton.setVisibility(View.INVISIBLE);
        addImageButton.setVisibility(View.VISIBLE);
    }
    private void switchFromAddButtonToDeleteButton(Button addImageButton) {
        Button deleteImageButton = findViewById(R.id.addTaskDeleteImageButton);
        deleteImageButton.setVisibility(View.VISIBLE);
        addImageButton.setVisibility(View.INVISIBLE);
    }


    // Taken from https://stackoverflow.com/a/25005243/16889809
    @SuppressLint("Range")
    public String getFileNameFromUri(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }
}
