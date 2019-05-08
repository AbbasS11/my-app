package com.example.afterweb;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    private EditText username;
    private EditText email;
    private EditText password;
    private FirebaseAuth auth;
    private DatabaseReference reference;
    private ProgressBar progressBar;


    @Override protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);
         progressBar=findViewById(R.id.progressBar_register);

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Sign Up");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        final Button register = findViewById(R.id.btn_register);
        email = findViewById(R.id.email);
        auth = FirebaseAuth.getInstance();

        register.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(final View v) {
                if (!isNetworkAvailable()){
                    Toast.makeText(RegisterActivity.this, "Please Check you Network", Toast.LENGTH_SHORT).show();
                }
               else if (username.getText() != null && email.getText() != null && password.getText() != null) {
                    final String edt_user = username.getText().toString().trim();
                    final String edt_email = email.getText().toString().trim();
                    final String edt_pass = password.getText().toString().trim();
                    if (TextUtils.isEmpty(edt_user) || TextUtils.isEmpty(edt_email) || TextUtils.isEmpty(edt_pass)) {
                        Toast.makeText(RegisterActivity.this, "Please fill all", Toast.LENGTH_SHORT).show();
                    } else if (edt_pass.length() < 6) {
                        Toast.makeText(RegisterActivity.this, "Your password must be 6 characters or more", Toast.LENGTH_SHORT).show();
                    } else {

                        progressBar.setVisibility(View.VISIBLE);
                        reg(edt_user, edt_email, edt_pass);
                    }
                }
            }
        });
    }

    private void reg(final String username, final String email, final String pass){
        auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override public void onComplete(@NonNull final Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    final FirebaseUser firebaseUser = auth.getCurrentUser();
                    if (firebaseUser != null) {
                        final String userId = firebaseUser.getUid();
                        reference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
                        final HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("id", userId);
                        hashMap.put("username", username);
                        hashMap.put("imageURL", "default");
                        reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override public void onComplete(@NonNull final Task<Void> task) {
                                if (task.isSuccessful()) {
                                    final Intent intent = new Intent(RegisterActivity.this, MainOne.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });
                    }
                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(RegisterActivity.this,"Used", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private boolean isNetworkAvailable() {
   /* boolean haveConnectedWifi = false;
    boolean haveConnectedMobile = false;

    ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo[] netInfo = cm.getAllNetworkInfo();
    for (NetworkInfo ni : netInfo) {
        if (ni.getTypeName().equalsIgnoreCase("WIFI"))
            if (ni.isConnected())
                haveConnectedWifi = true;
        if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
            if (ni.isConnected())
                haveConnectedMobile = true;
    }
    return haveConnectedWifi || haveConnectedMobile;*/
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}