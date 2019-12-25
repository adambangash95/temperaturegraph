package com.example.fyp;


import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TimeActivity extends AppCompatActivity {
    TimePicker picker, picker1;
    Button btnGet, btnGet1;
    RadioGroup radioGroup;
    RadioButton radioButton, rbtnWarm, rbtnHot, rbtnVHot;
    TextView tvw, tvw1, tvw2;
    DatabaseReference timereff, timereff1, servoreff;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time);
        btnGet = (Button) findViewById(R.id.button);
        tvw = (TextView) findViewById(R.id.textView);
        tvw2 = (TextView) findViewById(R.id.textView2);
        picker = (TimePicker) findViewById(R.id.timePicker);
        picker.setIs24HourView(true);

        btnGet1 = (Button) findViewById(R.id.button1);
        tvw1 = (TextView) findViewById(R.id.textView1);
        picker1 = (TimePicker) findViewById(R.id.timePicker1);
        picker1.setIs24HourView(true);

        radioGroup = (RadioGroup) findViewById(R.id.RGmode);
        rbtnWarm = (RadioButton) findViewById(R.id.rdbtnwarm);
        rbtnHot = (RadioButton) findViewById(R.id.rdbtnhot);
        rbtnVHot = (RadioButton) findViewById(R.id.rdbtnvhot);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        final String currentUserID = firebaseAuth.getCurrentUser().getUid();

        btnGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour, minute;
                String am_pm;
                if (Build.VERSION.SDK_INT >= 23) {
                    hour = picker.getHour();
                    minute = picker.getMinute();
                } else {
                    hour = picker.getCurrentHour();
                    minute = picker.getCurrentMinute();
                }

                tvw.setText("Start Time: " + hour + ":" + minute);

                int radioID = radioGroup.getCheckedRadioButtonId();
                radioButton = findViewById(radioID);

                tvw2.setText("Mode : " + radioButton.getText());

                final int H = hour;
                final int M = minute;

                timereff = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID).child("TimeStart");
                timereff.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        timereff.child("Hour").setValue(H);
                        timereff.child("Minutes").setValue(M);

                                if (rbtnWarm.isChecked()) {
                                    timereff.child("Sangle").setValue("60");
                                }
                                if (rbtnHot.isChecked()) {
                                    timereff.child("Sangle").setValue("120");
                                }
                                if (rbtnVHot.isChecked()) {
                                    timereff.child("Sangle").setValue("179");
                                }
                            }


                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


                btnGet1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int hour, minute;
                        String am_pm;
                        if (Build.VERSION.SDK_INT >= 23) {
                            hour = picker1.getHour();
                            minute = picker1.getMinute();
                        } else {
                            hour = picker1.getCurrentHour();
                            minute = picker1.getCurrentMinute();
                        }

                        tvw1.setText("End Time: " + hour + ":" + minute);

                        final int H1 = hour;
                        final int M1 = minute;

                        timereff1 = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID).child("TimeEnd");
                        timereff1.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                timereff1.child("Hour").setValue(H1);
                                timereff1.child("Minutes").setValue(M1);

                                timereff1.child("Eangle").setValue("1");
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                });


            }
        });
    }
}