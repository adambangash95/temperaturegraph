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
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.SimpleDateFormat;
import java.util.Date;


public class Graph extends AppCompatActivity {


    FirebaseDatabase database;
    DatabaseReference reference, reference2;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance(); // You are not initializing firebase auth object
    final String currentUserID = firebaseAuth.getCurrentUser().getUid();
    temperature tempobj;
    GraphView graphView;
    LineGraphSeries series;
    GraphHandler graph;
    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");


    int a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);


        graphView = findViewById(R.id.graphView); // OK so github obv is a place where people share code. I just logged you in inside your computer into github and added the project files there
        // If you had just copied everything to your drive then /build folder would have copied too. It is supposed to be unique to every device, now i have only the code that I need to run this on my mac

// OK so first we're gonna have 24 hours on it, later we can change the scrolling
        graphView.getViewport().setScalable(true);  // activate horizontal zooming and scrolling
        graphView.getViewport().setScrollable(true);  // activate horizontal scrolling
//        graphView.getViewport().setScalableY(true);  // activate horizontal and vertical zooming and scrolling
//        graphView.getViewport().setScrollableY(true);  // activate vertical scrolling ll try shooting in the dark ;) These are the reason why these 4 lines make the graph extremely long
        series = new LineGraphSeries();
        graphView.addSeries(series);


        graphView.getViewport().setMinY(1);
        graphView.getViewport().setMaxY(40);

        graphView.getViewport().setXAxisBoundsManual(true);
        graphView.getViewport().setMinX(new Date().getTime() - 10 * 1000 * 60);
        graphView.getViewport().setMaxX(new Date().getTime());

        graphView.getViewport().setYAxisBoundsManual(true);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Temp");
        graph = new GraphHandler(series);


        setListeners();
        //graphView.getGridLabelRenderer().setNumVerticalLabels(40);
        graphView.getGridLabelRenderer().setNumHorizontalLabels(5);

        graphView.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {

                if (isValueX) {
                    return sdf.format(new Date((long) value));
                } else {
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
                tempobj = dataSnapshot.getValue(temperature.class);
                a = tempobj.getCelsius();

                graph.updateGraph(a);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        String id = reference.push().getKey();
        long x = new Date().getTime();
        int y = a;

        PointValue pointValue = new PointValue(x, y);

        reference.child(id).setValue(pointValue);
    }


    @Override
    protected void onStart() {
        super.onStart();

//       reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                DataPoint[] dp = new DataPoint[(int) dataSnapshot.getChildrenCount()];
//                int index = 0;
//
//                for(DataSnapshot myDataSnapshot : dataSnapshot.getChildren())
//                {
//                    PointValue pointValue = myDataSnapshot.getValue(PointValue.class);
//
//                    dp[index] = new DataPoint(pointValue.getxValue(),pointValue.getyValue());
//                    index++;
//                }
//
//                series.resetData(dp);
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });


    }
}
// is it possible for you to share the code?
// i can upload it on my google drive? no brother. Github.. Do you have an account? not really... i can make one? yeah please do give me a few