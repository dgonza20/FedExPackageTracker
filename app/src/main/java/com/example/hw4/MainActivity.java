package com.example.hw4;

import android.content.Context;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.*;
import android.database.Cursor;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private static final String TAG = "MainActivity";

    // My Code - Declaring
    DatabaseHelper myDB;
    EditText editTrack1, editTrack2;
    Button btnAdd, btnViewAll, btnDelete, btnTrack1, btnTrack2;

    TextView textStatus;
    TextView textID1, textSource1, textCurr1, textDeliv1;
    TextView textID2, textSource2, textCurr2, textDeliv2;

    Spinner spinnerSource, spinnerDest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initializing the variables
        myDB = new DatabaseHelper(this);

        textStatus = findViewById(R.id.StatusText);
        textID1 = findViewById(R.id.ID1Text);
        textSource1 = findViewById(R.id.SourceLoc1Text);
        textCurr1 = findViewById(R.id.CurrLoc1Text);
        textDeliv1 = findViewById(R.id.Deliv1Text);

        textID2 = findViewById(R.id.ID2Text);
        textSource2 = findViewById(R.id.SourceLoc2Text);
        textCurr2 = findViewById(R.id.CurrLoc2Text);
        textDeliv2 = findViewById(R.id.Deliv2Text);


        editTrack1 = findViewById(R.id.editTrack1);
        editTrack2 = findViewById(R.id.editTrack2);

        btnAdd = findViewById(R.id.AddButton);
        btnViewAll = findViewById(R.id.ViewButton);
        btnDelete = findViewById(R.id.DeleteButton);
        btnTrack1 = findViewById(R.id.Track1Button);
        btnTrack2 = findViewById(R.id.Track2Button);

        spinnerSource = findViewById(R.id.spinnerSource);
        ArrayAdapter<CharSequence> adapter =ArrayAdapter.createFromResource(
                this,
                R.array.cities,
                android.R.layout.simple_spinner_item
                );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSource.setAdapter(adapter);
        spinnerSource.setOnItemSelectedListener(this);

        spinnerDest = findViewById(R.id.spinnerDest);
        spinnerDest.setAdapter(adapter);
        spinnerDest.setOnItemSelectedListener(this);



        AddData();
        viewData();
        dropDB(this);
        trackPackage(this);


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }

    // Track buttons and new thread creation to track the packages
    public void trackPackage(@Nullable final Context context){
        btnTrack1.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        myDB = new DatabaseHelper(context);

                        String track_id = editTrack1.getText().toString();


                        Track1Thread RunTrack1 = new Track1Thread(track_id);
                        new Thread(RunTrack1).start();
                    }
                }
        );

        btnTrack2.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String track_id = editTrack2.getText().toString();

                        Track2Thread RunTrack2 = new Track2Thread(track_id);
                        new Thread(RunTrack2).start();
                    }
                }
        );
    }

    // Drop the database. TODO: not necessary for now
    public void dropDB(final Context context){
        btnDelete.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        myDB.deleteDB(context);
                    }
                }
        );
    }

    // View the Database in a 'showMessage'
    public void viewData(){
        btnViewAll.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Cursor res = myDB.getAllData();

                        if(res.getCount() == 0){
                            showMessage("Error", "Nothing found");
                            return;
                        }

                        StringBuffer buffer = new StringBuffer();
                        buffer.append("ID\t TrackID \t From \t To \t Curr \t\t Deliv \n");
                        while(res.moveToNext()){
                            buffer.append(res.getString(0) + "\t\t");       // ID
                            buffer.append(res.getString(1) + " \t\t\t\t");  // TRACK_ID
                            buffer.append(res.getString(2) + "\t\t\t\t\t"); // FROM
                            buffer.append(res.getString(3) + "\t\t\t");     // TO
                            buffer.append(res.getString(4) + "\t\t\t\t\t"); // CURRENT
                            buffer.append(res.getString(5) + "\n");         // DELIVERED
                        }

                        showMessage("SQLite Database", buffer.toString());
                    }
                }
        );
    }

    public void showMessage(String title, String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();

    }

    // Add package to DB with the current location as source location
    public void AddData(){
        btnAdd.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        FedexTrack newPackage = new FedexTrack();
                        Integer Source = spinnerSource.getSelectedItemPosition();
                        Integer Dest = spinnerDest.getSelectedItemPosition();

                        boolean isInserted = newPackage.insertPackageStart(myDB, Source, Dest);
                        //isInserted = newPackage.InsertPackageRemaining(myDB,newPackage.getTrack_id(),Source,Dest);


                        if(isInserted == true){
                            textStatus.setText("Added: " + newPackage.getTrack_id());
                            Toast.makeText(MainActivity.this, "Data Inserted", Toast.LENGTH_LONG).show();
                        }
                        else
                            Toast.makeText(MainActivity.this, "Data not Inserted", Toast.LENGTH_LONG).show();
                    }
                }
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    // Runnable for Track1 button
    class Track1Thread implements Runnable{

        String TrackID;
        Integer Source, Dest, Curr, Delivered;
        FedexTrack newTrack;
        Vector<Integer> cityList;

        Track1Thread(String TrackID ){
            this.TrackID = TrackID;
            newTrack = new FedexTrack();
        }

        @Override
        public void run() {

            // If not a valid track id
            Cursor res = myDB.getLatestTrack(TrackID);
            if(res.getCount() == 0){
                textStatus.post(new Runnable() {
                    @Override
                    public void run() {
                        textStatus.setText("Invalid Tracking ID");
                    }
                });
                return;
            }

            res.moveToNext();
            Source = Integer.parseInt(res.getString(2));     // FROM
            Dest = Integer.parseInt(res.getString(3));       // TO
            Curr = Integer.parseInt(res.getString(4));       // CURRENT
            Delivered = Integer.parseInt(res.getString(5));  // DELI

            // If already delivered
            if(myDB.IsDelivered(TrackID)){

                textID1.post(new Runnable() {
                    @Override
                    public void run() {
                        textID1.setText(TrackID);
                    }
                });

                textSource1.post(new Runnable() {
                    @Override
                    public void run() {
                        textSource1.setText(newTrack.getNames(Source));
                    }
                });

                textCurr1.post(new Runnable() {
                    @Override
                    public void run() {
                        textCurr1.setText(newTrack.getNames(Curr));
                    }
                });

                textDeliv1.post(new Runnable() {
                    @Override
                    public void run() {
                        if(Delivered == 1) textDeliv1.setText("Yes");
                        else textDeliv1.setText("No");
                    }
                });

            }
            else{
                String checkNull = "Nothing";

                if(myDB == null) checkNull = "myDB";
                Log.d(TAG, "startThread: " + checkNull);
                Log.d(TAG, "startThread: " + Source);
                Log.d(TAG, "startThread: " + Dest);

                newTrack.InsertPackageRemaining(myDB, Integer.parseInt(TrackID), Source, Dest);

                textID1.post(new Runnable() {
                    @Override
                    public void run() {
                        textID1.setText(TrackID);
                    }
                });

                textSource1.post(new Runnable() {
                    @Override
                    public void run() {
                        textSource1.setText(newTrack.getNames(Source));
                    }
                });

                textDeliv1.post(new Runnable() {
                    @Override
                    public void run() {
                        textDeliv1.setText("No");
                    }
                });

                cityList = newTrack.cityList(Source, Dest);
                for(int i = 0; i < cityList.size(); i++){
                    Log.d(TAG, "startThread: " + i);
                    final String cityName = newTrack.getNames(cityList.elementAt(i));

                    textCurr1.post(new Runnable() {
                        @Override
                        public void run() {
                            textCurr1.setText(cityName);
                        }
                    });

                    if(i == cityList.size()-1){
                        textDeliv1.post(new Runnable() {
                            @Override
                            public void run() {
                                textDeliv1.setText("Yes");
                            }
                        });
                    }

                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }

    // Runnable for Track2 button
    class Track2Thread implements Runnable{

        String TrackID;
        Integer Source, Dest, Curr, Delivered;
        FedexTrack newTrack;
        Vector<Integer> cityList;

        Track2Thread(String TrackID ){
            this.TrackID = TrackID;
            newTrack = new FedexTrack();
        }

        @Override
        public void run() {

            // If not a valid track id
            Cursor res = myDB.getLatestTrack(TrackID);
            if(res.getCount() == 0){
                textStatus.post(new Runnable() {
                    @Override
                    public void run() {
                        textStatus.setText("Invalid Tracking ID");
                    }
                });
                return;
            }

            res.moveToNext();
            Source = Integer.parseInt(res.getString(2));     // FROM
            Dest = Integer.parseInt(res.getString(3));       // TO
            Curr = Integer.parseInt(res.getString(4));       // CURRENT
            Delivered = Integer.parseInt(res.getString(5));  // DELI

            // If already delivered
            if(myDB.IsDelivered(TrackID)){

                textID2.post(new Runnable() {
                    @Override
                    public void run() {
                        textID2.setText(TrackID);
                    }
                });

                textSource2.post(new Runnable() {
                    @Override
                    public void run() {
                        textSource2.setText(newTrack.getNames(Source));
                    }
                });

                textCurr2.post(new Runnable() {
                    @Override
                    public void run() {
                        textCurr2.setText(newTrack.getNames(Curr));
                    }
                });

                textDeliv2.post(new Runnable() {
                    @Override
                    public void run() {
                        if(Delivered == 1) textDeliv2.setText("Yes");
                        else textDeliv2.setText("No");
                    }
                });

            }
            else{
                String checkNull = "Nothing";

                if(myDB == null) checkNull = "myDB";
                Log.d(TAG, "startThread: " + checkNull);
                Log.d(TAG, "startThread: " + Source);
                Log.d(TAG, "startThread: " + Dest);

                newTrack.InsertPackageRemaining(myDB, Integer.parseInt(TrackID), Source, Dest);

                textID2.post(new Runnable() {
                    @Override
                    public void run() {
                        textID2.setText(TrackID);
                    }
                });

                textSource2.post(new Runnable() {
                    @Override
                    public void run() {
                        textSource2.setText(newTrack.getNames(Source));
                    }
                });

                textDeliv2.post(new Runnable() {
                    @Override
                    public void run() {
                        textDeliv2.setText("No");
                    }
                });

                cityList = newTrack.cityList(Source, Dest);
                for(int i = 0; i < cityList.size(); i++){
                    Log.d(TAG, "startThread: " + i);
                    final String cityName = newTrack.getNames(cityList.elementAt(i));

                    textCurr2.post(new Runnable() {
                        @Override
                        public void run() {
                            textCurr2.setText(cityName);
                        }
                    });

                    if(i == cityList.size()-1){
                        textDeliv2.post(new Runnable() {
                            @Override
                            public void run() {
                                textDeliv2.setText("Yes");
                            }
                        });
                    }

                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
