package com.practice.taskmaster.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.practice.taskmaster.R;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        EditText usernameEditText = findViewById(R.id.username);

        Button saveUsername= findViewById(R.id.button);
        saveUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameEditText.getText().toString();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("username", username);
                editor.apply();
                Toast.makeText(SettingsActivity.this, "UserName Changed!", Toast.LENGTH_SHORT).show();

            }
        });



        Button settingsBackButton=findViewById(R.id.settingsBackButton);
        settingsBackButton.setOnClickListener(view -> {
            Intent backToHomeFromSettings=new Intent(SettingsActivity.this, HomeActivity.class);
            startActivity(backToHomeFromSettings);
        });



    }
}