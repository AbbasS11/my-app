package com.example.afterweb;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import eightbitlab.com.blurview.BlurView;
import eightbitlab.com.blurview.RenderScriptBlur;

public class LoginActivity extends AppCompatActivity {
    private FirebaseUser firebaseUser;

    @Override protected void onStart() {
        super.onStart();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser != null) {
            final Intent intent = new Intent(LoginActivity.this, MainOne.class);
            startActivity(intent);
            finish();
        }
    }


    private EditText email;
    private EditText password;
    private FirebaseAuth auth;
    private TextView forgetPass;
    public  final  String CHANNEL_ID ="001";


    @Override
    public void onBackPressed() {
      /**  Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);**/
        finishAffinity();
    }

    @Override protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final BlurView blurView = findViewById(R.id.blurView);
        final View decorView = getWindow().getDecorView();
        final ViewGroup rootView = decorView.findViewById(android.R.id.content);
        final Drawable windowBackground = decorView.getBackground();
        blurView.setupWith(rootView).setFrameClearDrawable(windowBackground).setBlurAlgorithm(new RenderScriptBlur(this)).setBlurRadius(4.0f).setHasFixedTransformationMatrix(true);


        forgetPass=findViewById(R.id.txt_forgetPass);


        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Sign In");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        auth = FirebaseAuth.getInstance();
        password = findViewById(R.id.password1);
        email = findViewById(R.id.email1);
        final Button login = findViewById(R.id.btn_login);




        login.setOnClickListener(v -> {
            if (!isNetworkAvailable()){
                Toast.makeText(LoginActivity.this, "Please Check you Network", Toast.LENGTH_SHORT).show();
            }
           else if (email.getText() != null && password.getText() != null) {
                final String edt_email = email.getText().toString();
                final String edt_pass = password.getText().toString();
                if (TextUtils.isEmpty(edt_email) || TextUtils.isEmpty(edt_pass)) {
                    Toast.makeText(LoginActivity.this, "Please fill all", Toast.LENGTH_SHORT).show();
                } else {
                    ProgressBar progressBar=findViewById(R.id.progressBar);
                    progressBar.setVisibility(View.VISIBLE);

                    auth.signInWithEmailAndPassword(edt_email, edt_pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override public void onComplete(@NonNull final Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                final Intent intent = new Intent(LoginActivity.this, MainOne.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            } else {
                                    progressBar.setVisibility(View.GONE);

                                Toast.makeText(LoginActivity.this, "Wrong user or pass", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                }
            }
        });
        forgetPass.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this,ResetPasswordActivity.class)));
        final Button signUp = findViewById(R.id.btn_signup);
        signUp.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));



       /* Button btn_not= (Button)findViewById(R.id.btn_trynot);
        btn_not.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shoNotification(v);
            }
        });*/
    }
 /*   public void shoNotification(View view){

       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, "001", NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription("discription");

            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);

            Notification.Builder builder = new Notification.Builder(this, CHANNEL_ID);
            builder.setSmallIcon(R.mipmap.ic_launcher).setContentText("chatnotification").setContentTitle("title of noti").setPriority(Notification.PRIORITY_DEFAULT);

            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
            notificationManagerCompat.notify(001, builder.build());


        }
        else
       {
           Notification.Builder builder = new Notification.Builder(this);
           builder.setSmallIcon(R.mipmap.ic_launcher).setContentText("chatnotification for old").setContentTitle("api old").setPriority(Notification.PRIORITY_DEFAULT);


           NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
           notificationManagerCompat.notify(001, builder.build());

       }

    }*/
/*    public class CheckTypesTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog asyncDialog = new ProgressDialog(LoginActivity.this);
        String typeStatus;


        @Override
        protected void onPreExecute() {
            //set message of the dialog
            asyncDialog.setMessage("Please Waite");
            //show dialog
            asyncDialog.show();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {




        }

        @Override
        protected void onPostExecute(Void result) {
            //hide the dialog
            asyncDialog.dismiss();

            super.onPostExecute(result);
        }

    }*/
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