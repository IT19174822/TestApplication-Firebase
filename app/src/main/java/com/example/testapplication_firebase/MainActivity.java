package com.example.testapplication_firebase;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView register;
    private EditText eEmail,ePassword;
    private Button eLogin;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        register=(TextView) findViewById(R.id.reg);
        register.setOnClickListener(this);

        eEmail=findViewById(R.id.email);
        ePassword=findViewById(R.id.password);
        eLogin=findViewById(R.id.btnLogin);

        mAuth=FirebaseAuth.getInstance();



        eLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

                //Authenticate User
                mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(MainActivity.this,"Login successfully",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
                        }else{
                            Toast.makeText(MainActivity.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                        }

                    }
                });

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.reg:
                startActivity(new Intent(this,Register.class));
                break;

        }
    }
}