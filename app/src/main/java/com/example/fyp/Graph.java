package com.example.fyp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fyp.Model.PointValue;
import com.example.fyp.Model.temperature;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class Graph extends AppCompatActivity {


    FirebaseDatabase database;
    DatabaseReference reference,reference2;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance(); // You are not initializing firebase auth object

    temperature tempobj;

    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
    GraphView graphView;
    LineGraphSeries series;

    final String currentUserID = firebaseAuth.getCurrentUser().getUid();

    ArrayList<DataPoint> myList = new ArrayList<DataPoint>();

    int a;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);


        graphView = (GraphView)findViewById(R.id.graphView);


        graphView.getViewport().setScalable(true);  // activate horizontal zooming and scrolling
        graphView.getViewport().setScrollable(true);  // activate horizontal scrolling
        graphView.getViewport().setScalableY(true);  // activate horizontal and vertical zooming and scrolling
        graphView.getViewport().setScrollableY(true);  // activate vertical scrolling ll try shooting in the dark ;)
        series =  new LineGraphSeries();
        graphView.addSeries(series);


        graphView.getViewport().setMinY(1);
        graphView.getViewport().setMaxY(40);

        graphView.getViewport().setYAxisBoundsManual(true);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Temp");





        setListeners();
        //graphView.getGridLabelRenderer().setNumVerticalLabels(40);
        graphView.getGridLabelRenderer().setNumHorizontalLabels(5);

        graphView.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter(){
            @Override
            public String formatLabel(double value, boolean isValueX) {

                if(isValueX) {
                    return sdf.format(new Date((long) value));
                }else {
                    return super.formatLabel(value, isValueX);
                }
            }
        });
    }

    private void setListeners() {


        reference2 = FirebaseDatabase.getInstance().getReference("Users").child(currentUserID).child("TEMP");
        reference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tempobj=dataSnapshot.getValue(temperature.class);
                a=tempobj.getCelsius();

                updateGraph(a);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        String id = reference.push().getKey();
        long x = new Date().getTime();
        int y = a;

        PointValue pointValue = new PointValue(x,y);

        reference.child(id).setValue(pointValue);
    }

    private void updateGraph(int celsiusReading) {
        String id = reference.push().getKey();
        long x = new Date().getTime();
        int y = celsiusReading;

        PointValue pointValue = new PointValue(x,y); // show me where you are updating graph

        DataPoint newDataPoint = new DataPoint(pointValue.getxValue(),pointValue.getyValue());
        myList.add(newDataPoint);

        DataPoint[] array = (DataPoint[]) myList.toArray(new DataPoint[myList.size()]);

        series.resetData(array);

    }


    @Override
    protected void onStart() {
        super.onStart();

       reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DataPoint[] dp = new DataPoint[(int) dataSnapshot.getChildrenCount()];
                int index = 0;

                for(DataSnapshot myDataSnapshot : dataSnapshot.getChildren())
                {
                    PointValue pointValue = myDataSnapshot.getValue(PointValue.class);

                    dp[index] = new DataPoint(pointValue.getxValue(),pointValue.getyValue());
                    index++;
                }

                series.resetData(dp);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }
}
// is it possible for you to share the code?
// i can upload it on my google drive? no brother. Github.. Do you have an account? not really... i can make one? yeah please do give me a few