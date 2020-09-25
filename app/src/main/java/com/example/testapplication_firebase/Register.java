package com.example.testapplication_firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

public class Register extends AppCompatActivity {


    EditText txtID,txtName;
    Button btnSave;
    DatabaseReference dbRef;



    Student std;
    //Notification channel
    //Notification builder
    //Notification Manager thats we want to create

    private static  final String CHANNEL_ID ="channelid";
    private static  final String CHANNEL_NAME ="channel1";
    private static  final String CHANNEL_DES ="Simplified coding notification";

    private EditText eEmail,ePassword;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;


    private void clearControls(){
        txtID.setText("");
        txtName.setText("");

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        txtID=findViewById(R.id.EtID);
        txtName=findViewById(R.id.EtName);


        btnSave=findViewById(R.id.BtnSave);


        std= new Student();

        mAuth= FirebaseAuth.getInstance();


        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel channel= new NotificationChannel(CHANNEL_ID,CHANNEL_NAME,NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(CHANNEL_DES);
            NotificationManager manager=getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);

        }
        progressBar=findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        eEmail=findViewById(R.id.email);
        ePassword=findViewById(R.id.password);

        findViewById(R.id.signup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUser();

            }
        });


        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if(task.isSuccessful()){
                            String token = task.getResult().getToken();


                        }else{

                        }
                    }
                });


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbRef = FirebaseDatabase.getInstance().getReference().child("Student");
                try {


                    if (TextUtils.isEmpty(txtID.getText().toString()))
                        Toast.makeText(getApplicationContext(), "Please Enter ID", Toast.LENGTH_SHORT).show();
                    else if(TextUtils.isEmpty(txtName.getText().toString()))
                        Toast.makeText(getApplicationContext(), "Please Enter Name", Toast.LENGTH_SHORT).show();
                    else {
                        std.setId(txtID.getText().toString().trim());
                        std.setName(txtName.getText().toString().trim());



                        //dbRef.push().setValue(std);
                        dbRef.child("std1").setValue(std);
                        Toast.makeText(getApplicationContext(), "Data Entered Successfully", Toast.LENGTH_SHORT).show();
                        clearControls();

                        displayNotification();

                    }
                }catch (NumberFormatException e){
                    Toast.makeText(getApplicationContext(), "Invalid Name", Toast.LENGTH_SHORT).show();

                }

            }


        });

    }
    public void createUser(){
        final String email =eEmail.getText().toString().trim();
        final String password =ePassword.getText().toString().trim();

        if(email.isEmpty()){
            eEmail.setError("Email Required");
            eEmail.requestFocus();
            return;
        }
        if(password.isEmpty()){
            ePassword.setError("Give a valid password");
            ePassword.requestFocus();
            return;
        }
        if(password.length()<6){
            ePassword.setError("Give a valid password");
            ePassword.requestFocus();
            return;

        }
        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            startProfileActivity();

                        }else{
                            if(task.getException() instanceof FirebaseAuthUserCollisionException){
                                userLogin(email,password);

                            }else {
                                progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(Register.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                            }
                        }


                    }
                });


    }
    private void userLogin(String email,String password){
        mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            startProfileActivity();
                        }else{
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(Register.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }
    public void startProfileActivity(){
        Intent intent= new Intent(this,ProfileActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }


    private void displayNotification(){
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this,CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_android_black_24dp)
                .setContentTitle("The Data Entered successfully")
                .setContentText("My first Notification")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat mNotificationMgr= NotificationManagerCompat.from(this);
        mNotificationMgr.notify(1,mBuilder.build());


    }


}