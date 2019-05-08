package com.example.afterweb;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.Objects;

public class ResetPasswordActivity extends AppCompatActivity {

    MaterialEditText edt_sendEmail;
    Button btn_send;
    FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Reset Password");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        edt_sendEmail= findViewById(R.id.edt_send_email);
        btn_send=findViewById(R.id.btn_reset);

        firebaseAuth=FirebaseAuth.getInstance();

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edt_sendEmail.getText().toString().equals("")) {
                    Toast.makeText(ResetPasswordActivity.this, "Please fill Email", Toast.LENGTH_SHORT).show();
                }
                else   {
                    final String edt_email = Objects.requireNonNull(edt_sendEmail.getText()).toString();
                    firebaseAuth.sendPasswordResetEmail(edt_email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(ResetPasswordActivity.this, "Please Check your Email", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(ResetPasswordActivity.this,LoginActivity.class));
                            } else {
                                String error= task.getException().toString();
                            }
                        }
                    });
                }
            }
        });
    }
}
