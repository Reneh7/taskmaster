package com.practice.taskmaster.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amplifyframework.auth.AuthUserAttributeKey;
import com.amplifyframework.auth.options.AuthSignUpOptions;
import com.amplifyframework.core.Amplify;
import com.practice.taskmaster.R;

public class SignUpActivity extends AppCompatActivity {
    public static final String TAG="signUpActivity";
    public static final String SIGNUP_EMAIL_TAG="signUp_email_Tag";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        signUpButton();
        backButton();
    }

    public void signUpButton(){
        Button signupSubmitButton=findViewById(R.id.signUpSubmitButton);
        signupSubmitButton.setOnClickListener(v->{
            String username=((EditText)findViewById(R.id.signupUsernameEditText)).getText().toString();
            String password=((EditText)findViewById(R.id.signUpPasswordEditText)).getText().toString();

            Amplify.Auth.signUp(username,
                password, AuthSignUpOptions.builder()
                        .userAttribute(AuthUserAttributeKey.email(),username)
                            .userAttribute(AuthUserAttributeKey.nickname(),"Reneh7")
                        .build(),
            good->{
                Log.i(TAG,"SignUp successfully"+ good.toString());
                Intent goToVerifyIntent=new Intent(SignUpActivity.this,VerifyAccountActivity.class);
                goToVerifyIntent.putExtra(SIGNUP_EMAIL_TAG,username);
                startActivity(goToVerifyIntent);
                },
            bad->{Log.i(TAG,"SignUp failed with username: "+username+" with this message:" + bad.toString());
                  runOnUiThread(()->{
                      Toast.makeText(SignUpActivity.this,"SignUp Failed",Toast.LENGTH_LONG);
                  });
            }
        );
        });
    }

    public void backButton(){
        Button signUpBackButton= findViewById(R.id.signupBackButton);
        signUpBackButton.setOnClickListener(view -> {
            Intent goBackToHome = new Intent(SignUpActivity.this, HomeActivity.class);
            startActivity(goBackToHome);
        });
    }
}