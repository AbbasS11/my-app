package com.example.afterweb;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private FirebaseUser firebaseUser;

    @Override protected void onStart() {
        super.onStart();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser != null) {
            final Intent intent = new Intent(MainActivity.this, MainOne.class);
            startActivity(intent);
            finish();
        }
    }

    @Override protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

       // final Button login = findViewById(R.id.btn_login);
       // final Button signUp = findViewById(R.id.btn_signup);
       // firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        new Handler().postDelayed (() -> {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }, 1000);


        /**login.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(final View v) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(final View v) {
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
            }
        });**/
    }

    @Override
    protected void onResume() {
        super.onResume();
        new Handler().postDelayed (() -> {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }, 1000);
    }
}