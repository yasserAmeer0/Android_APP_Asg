package com.example.asg2prog;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    protected TextView total;
    protected ListView listview;
    protected FloatingActionButton dialog;
    protected Toolbar toolbar1;
    protected ArrayAdapter<String> adapter;
    protected  List<Profile> studentsList;
    protected MySQL sql;
    protected  Access accesProfile;
    protected Profile profile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listview = findViewById(R.id.label1);
        dialog = findViewById(R.id.floatingActionButton2);
        total=findViewById(R.id.total);
        toolbar1=findViewById(R.id.toolbar1);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        listview.setAdapter(adapter);
        loadTextView();
        ToolbarSetup();
        dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog student = new Dialog();
                student.show(getSupportFragmentManager(), "AddStudent");
            }
        });

// when ever we click an item in the listview it goes to the next activity profile activity and it records an access type opened and insert it in the database
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                sql = new MySQL(getApplicationContext());
                String opened ="opened";
                profile = studentsList.get(position);
                accesProfile= new Access(profile.getPid(),opened,sql.getTime());
                sql.insertStudentAccess(accesProfile);
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                intent.putExtra("pid", profile.getPid());
                startActivity(intent);
                updateListView();

            }
        });
    }
    // this update thing when it deleted
    public void updateListView() {
        loadTextView();
        updateTotal();
    }
// updating the total profile based on the name
    public void updateTotal() {
        sql = new MySQL(getApplicationContext());
        int total1= sql.getTotalNumberOfProfiles();
        total.setText(total1+" Profile by Surname");

    }
    // updating based on the ID
    public void updateTotal1() {
        sql = new MySQL(getApplicationContext());
        int total1= sql.getTotalNumberOfProfiles();
        total.setText(total1+" Profile by ID");

    }
// a display method for the listview based on the surname and name with an incremented counter, it take the the sorted by alphetical number
    public void loadTextView() {
        sql = new MySQL(getApplicationContext());
        studentsList = sql.getAllStudentsSortedByFirstName();
        List<String> studentList = new ArrayList<>();
        int counter = 1;
        for (Profile student : studentsList) {
            String first = student.getFirst();
            String last = student.getLast();
            String studentName = (counter + " . " + first + " , " + last);
            studentList.add(studentName);
            counter++;
        }
        adapter.clear();
        adapter.addAll(studentList);
        adapter.notifyDataSetChanged();
    }

// same as above but for the ID that are increasing order
    public void loadTextView1() {
         sql = new MySQL(getApplicationContext());
        studentsList = sql.getAllStudentsSortedByID();
        List<String> studentNamesList = new ArrayList<>();
        for (Profile student : studentsList) {
           int pid= student.getPid();
            studentNamesList.add(pid + " ");
        }
        adapter.clear();
        adapter.addAll(studentNamesList);
        adapter.notifyDataSetChanged();
    }


    /// function related to initialiaze the toolbar and toggle
    private void ToolbarSetup(){
        setSupportActionBar(toolbar1);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toggle, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.toggle) {
                loadTextView1();
                onStart();
                updateTotal1();
            return true;
        }
                if(item.getItemId() == R.id.toggle1) {
                loadTextView();
                onStart();
                updateTotal();
                    return true;
            }
        return super.onOptionsItemSelected(item);
    }
    protected void onResume() {
        super.onResume();
        // Update your data source if needed
        updateListView();
        // Notify the adapter that the underlying data has changed
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }
}