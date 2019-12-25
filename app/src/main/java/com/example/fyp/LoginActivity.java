package com.example.fyp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class LoginActivity extends AppCompatActivity {



    EditText edtEmail, edtPassword;
    Button btnSignIn,btnToSignUp;
    ProgressBar PBar;
    TextView TVForgotpass;
    DatabaseReference reff;


    FirebaseAuth firebaseAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtPassword = (EditText) findViewById(R.id.edtPassword);

        btnSignIn = (Button) findViewById(R.id.btnSignIn);
        btnToSignUp = (Button) findViewById(R.id.btnToSignUp);
        PBar = (ProgressBar) findViewById(R.id.progressBar);
        TVForgotpass = (TextView)findViewById(R.id.TVforgotpass);

        firebaseAuth = FirebaseAuth.getInstance();




        //Button For Sign Up
        btnToSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent s = new Intent(getApplicationContext(),SignupActivity.class);
                startActivity(s);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });




        //Button For Sign In
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PBar.setVisibility(View.VISIBLE);

                    firebaseAuth.signInWithEmailAndPassword(edtEmail.getText().toString(), edtPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            PBar.setVisibility(View.GONE);

                                if (task.isSuccessful()) {

                                    String currentUserID = firebaseAuth.getCurrentUser().getUid();
                                    reff = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID).child("TEMP");
                                    reff.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                            if (dataSnapshot.hasChild("Celsius")) {
                                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                                Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_LONG).show();
                                            }
                                            else
                                                {
                                                Intent homeIntent = new Intent(LoginActivity.this, AddnewDevice.class);
                                                startActivity(homeIntent);
                                            }
                                        }
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                }
                                else {
                                    Toast.makeText(LoginActivity.this, "oops", Toast.LENGTH_LONG).show();
                                }
                        }


                    });

            }
        });

        TVForgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent s = new Intent(getApplicationContext(),ForgotPasswordActivity.class);
                startActivity(s);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
    }
}
