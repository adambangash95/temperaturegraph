package com.example.fyp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fyp.Model.Servo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {


    TextView TempC,TempF,Modetv,outtempC,outtempH;
    RadioGroup radioGroup;
    RadioButton radioButton,rbtnPilot,rbtnWarm,rbtnHot,rbtnVHot;
    Button BTime,BGraph;
    DatabaseReference reff,servoreff,timereff,timereff1;
    ImageView IWLogout;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TempC= (TextView)findViewById(R.id.TVCurrentTempC);
        TempF= (TextView)findViewById(R.id.TVCurrenttempF);
        BTime=(Button)findViewById(R.id.btnTime);
        BGraph=(Button)findViewById(R.id.btnGraph);
        IWLogout = (ImageView)findViewById(R.id.IWLogout);
        outtempC = (TextView)findViewById(R.id.TVoutsideC);
        outtempH = (TextView)findViewById(R.id.TVoutsideH);

        radioGroup = (RadioGroup) findViewById(R.id.RGmode);
        rbtnPilot = (RadioButton) findViewById(R.id.rdbtnpilot);
        rbtnWarm = (RadioButton) findViewById(R.id.rdbtnwarm);
        rbtnHot = (RadioButton) findViewById(R.id.rdbtnhot);
        rbtnVHot = (RadioButton) findViewById(R.id.rdbtnvhot);
        Modetv = (TextView) findViewById(R.id.TVModeName);



        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        final String currentUserID = firebaseAuth.getCurrentUser().getUid();

        timereff = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID).child("TimeStart");
        timereff.child("Sangle").setValue("1");
        timereff1 = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID).child("TimeEnd");
        timereff1.child("Eangle").setValue("1");


        BTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                servoreff = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID).child("servo").child("servo");
                servoreff.child("angle").setValue(1);
                Intent T = new Intent(getApplicationContext(),TimeActivity.class);
                startActivity(T);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

            }
        });

        BGraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent T = new Intent(getApplicationContext(),Graph.class);
                startActivity(T);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });






        reff = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID).child("TEMP");
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final int tempC = dataSnapshot.child("Celsius").getValue(Integer.class);
                final int tempF = dataSnapshot.child("Fahrenheit").getValue(Integer.class);


                if(tempC == -127){
                    TempC.setTextColor(Color.parseColor("#FF0000"));
                    TempF.setTextColor(Color.parseColor("#FF0000"));
                    TempC.setText("Error Reading temperature");
                    TempF.setText("Error Reading temperature");
                }

                if(tempC < 20 && tempC > 1) {
                    TempC.setTextColor(Color.parseColor("#add8e6"));
                    TempF.setTextColor(Color.parseColor("#add8e6"));
                    TempC.setText(tempC + "°C");
                    TempF.setText(tempF + "°F");
                }

                if(tempC > 20 && tempC < 30) {
                    TempC.setTextColor(Color.parseColor("#ffff00"));
                    TempF.setTextColor(Color.parseColor("#ffff00"));
                    TempC.setText(tempC + "°C");
                    TempF.setText(tempF + "°F");
                }

                if(tempC > 30 && tempC < 50) {
                    TempC.setTextColor(Color.parseColor("#FF0000"));
                    TempF.setTextColor(Color.parseColor("#FF0000"));
                    TempC.setText(tempC + "°C");
                    TempF.setText(tempF + "°F");
                }

                reff = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID).child("outsideTEMP");
                reff.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        final float outtemp = dataSnapshot.child("Celsius").getValue(float.class);
                        final int outhumidity = dataSnapshot.child("Humidity").getValue(Integer.class);

                        outtempC.setText(outtemp + "°C");
                        outtempH.setText(outhumidity + "%");

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                servoreff = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID).child("servo").child("servo");
                servoreff.addValueEventListener(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                       final String Mode = dataSnapshot.child("angle").getValue().toString();
                       if(Mode.equals("1"))
                       {
                           rbtnPilot.setChecked(true);
                           Modetv.setTextColor(Color.parseColor("#add8e6"));
                           Modetv.setText("Pilot" );
                       }
                       else if(Mode.equals("60"))
                       {
                           rbtnWarm.setChecked(true);
                           Modetv.setTextColor(Color.parseColor("#FFFFFF"));
                           Modetv.setText("Warm" );
                       }
                       else if(Mode.equals("120"))
                       {
                           rbtnHot.setChecked(true);
                           Modetv.setTextColor(Color.parseColor("#ffff00"));
                           Modetv.setText("Hot" );
                       }
                       else if(Mode.equals("179"))
                       {
                           rbtnVHot.setChecked(true);
                           Modetv.setTextColor(Color.parseColor("#FF0000"));
                           Modetv.setText("Very Hot" );
                       }
                   }

                   @Override
                   public void onCancelled(@NonNull DatabaseError databaseError) {

                   }
               });




                View.OnClickListener optionOnclickListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String mode = null;

                        mode = ((RadioButton)view).getText().toString();
                        Modetv.setText(mode);

                        if(rbtnPilot.isChecked()){
                            int radioID = radioGroup.getCheckedRadioButtonId();
                            radioButton = findViewById(radioID);

                            Modetv.setText(radioButton.getText());

                            servoreff.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    final Servo servo = new Servo("1");

                                    servoreff.setValue(servo);
                                    Toast.makeText(MainActivity.this, "Geyser is Now on Pilot Mode", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }

                        if(rbtnWarm.isChecked()){
                            int radioID = radioGroup.getCheckedRadioButtonId();
                            radioButton = findViewById(radioID);

                            Modetv.setText(radioButton.getText());

                            servoreff.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    final Servo servo = new Servo("60");
                                    if(tempC <= 50) {
                                        servoreff.setValue(servo);
                                        Toast.makeText(MainActivity.this, "Geyser is Now on Warm Mode", Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        Toast.makeText(MainActivity.this, "Temperature is already too high", Toast.LENGTH_SHORT).show();
                                    }

                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }

                        if(rbtnHot.isChecked()){
                            int radioID = radioGroup.getCheckedRadioButtonId();
                            radioButton = findViewById(radioID);

                            Modetv.setText(radioButton.getText());

                            servoreff.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    final Servo servo = new Servo("120");
                                    if(tempC <= 60) {
                                        servoreff.setValue(servo);
                                        Toast.makeText(MainActivity.this, "Geyser is Now on Hot Mode", Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        Toast.makeText(MainActivity.this, "Temperature is already too high", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                        if(rbtnVHot.isChecked()){
                            int radioID = radioGroup.getCheckedRadioButtonId();
                            radioButton = findViewById(radioID);

                            Modetv.setText(radioButton.getText());

                            servoreff.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    final Servo servo = new Servo("179");
                                    if(tempC <= 75) {
                                        servoreff.setValue(servo);
                                        Toast.makeText(MainActivity.this, "Geyser is Now on Very Hot Mode", Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        Toast.makeText(MainActivity.this, "Temperature is already too High", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }

                    }
                };

                rbtnPilot.setOnClickListener(optionOnclickListener);
                rbtnWarm.setOnClickListener(optionOnclickListener);
                rbtnHot.setOnClickListener(optionOnclickListener);
                rbtnVHot.setOnClickListener(optionOnclickListener);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        IWLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                finish();
                startActivity(intent);
                Toast.makeText(MainActivity.this,"Logged Out Successfully",Toast.LENGTH_LONG).show();
            }
        });


    }



}
