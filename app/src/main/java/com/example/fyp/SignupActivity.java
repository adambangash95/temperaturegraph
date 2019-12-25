package com.example.fyp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.HashMap;





public class SignupActivity extends AppCompatActivity {


    //FireBase Authentication
    FirebaseAuth firebaseAuth;
    DatabaseReference Rootref;


    EditText edtUsername, edtPassword, edtMail;
    Button btnSignUp, btnToSignIn;
    ProgressBar PBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        edtUsername = (EditText)findViewById(R.id.edtEmail);
        edtPassword = (EditText)findViewById(R.id.edtPassword);
        edtMail = (EditText)findViewById(R.id.edtMail);

        btnSignUp = (Button)findViewById(R.id.btnSignUp);
        btnToSignIn = (Button)findViewById(R.id.btnToSignIn);

        PBar = (ProgressBar)findViewById(R.id.progressBar);

        //FireBase Authentication
        firebaseAuth = FirebaseAuth.getInstance();
        Rootref = FirebaseDatabase.getInstance().getReference();


        //Button for Sign In
        btnToSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent s = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(s);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        //Button for Registering
        btnSignUp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                PBar.setVisibility(View.VISIBLE);
                firebaseAuth.createUserWithEmailAndPassword(edtMail.getText().toString(),
                        edtPassword.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                                PBar.setVisibility(View.GONE);
                                if(task.isSuccessful()){
                                    Toast.makeText(SignupActivity.this, "Registered Successfully", Toast.LENGTH_LONG).show();

                                    String currentUserID = firebaseAuth.getCurrentUser().getUid();

                                    Rootref.child("Users").child(currentUserID).setValue("");
                                    String NAME = edtUsername.getText().toString();
                                    String PASSWORD = edtPassword.getText().toString();
                                    String EMAIL = edtMail.getText().toString();

                                    HashMap<String, Object> registerMap = new HashMap<>();
                                    registerMap.put("uid", currentUserID);
                                    registerMap.put("Email", EMAIL);
                                    registerMap.put("name", NAME);
                                    registerMap.put("Password", PASSWORD);

                                    Rootref.child("Users").child(currentUserID).updateChildren(registerMap);

                                    edtMail.setText("");
                                    edtUsername.setText("");
                                    edtPassword.setText("");
                                }else{
                                    Toast.makeText(SignupActivity.this,
                                            task.getException().getMessage(),
                                            Toast.LENGTH_LONG).show();
                                }
                    }
                });
            }

        });

    }
}
