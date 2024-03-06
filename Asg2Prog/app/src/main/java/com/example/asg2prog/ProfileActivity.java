package com.example.asg2prog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {
    protected Button delete;
    protected TextView first,last,gpa,id,time;
    protected List<Access> studentsList1;
    protected ArrayAdapter<String> adapter1;
    protected ListView listview1;
    protected Toolbar toolbar2;
   protected Profile profile;
   protected MySQL sql;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        profileStudent();
        profileStudentAccess();
        loadTextView();
        ToolbarSetup();
    }
// displaying the information of the student that has been clicked on the Main activity based on the listview id and position
    public void profileStudent() {
        toolbar2 =(Toolbar)findViewById(R.id.toolbar2);
         first = findViewById(R.id.first);
         last = findViewById(R.id.last);
         gpa = findViewById(R.id.gpa);
         id = findViewById(R.id.ID);
         time = findViewById(R.id.timestamp);
        delete=findViewById(R.id.delete);
        int pid = getIntent().getIntExtra("pid", 0);
         sql = new MySQL(getApplicationContext());
         profile = sql.getProfile(pid);
        if (profile != null) {
            first.setText("FIRST NAME: " + profile.getFirst());
            last.setText("LAST NAME: " + profile.getLast());
            gpa.setText("STUDENT GPA: " + profile.getGPA());
            id.setText("STUDENT ID: " + profile.getPid());
             time.setText("TIMESTAMP: " + profile.getTime());
        } else {
            Toast.makeText(getApplicationContext(), "Profile not found for ID: " + pid, Toast.LENGTH_LONG).show();
        }

        //// it delete the profile when requested
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String delete="deleted";
                Access deleteProfile= new Access(pid,delete, sql.getTime());
                sql.insertStudentAccess(deleteProfile);
                sql.deleteProfile(pid);
               finish();
            }
        });
    }


    public void profileStudentAccess() {
        listview1=findViewById(R.id.label2);
        adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        listview1.setAdapter(adapter1);
    }


// displaying the access mode based on the lastet to the newest , method accessed from the database
    public void loadTextView() {
        sql = new MySQL(getApplicationContext());
        int Pid = getIntent().getIntExtra("pid", 0);
        studentsList1 = sql.getAllStudentsAccess1(Pid);
        List<String> studentNamesList1 = new ArrayList<>();
        for (Access student : studentsList1) {
            String type = student.getType();
            String time = student.getTime();
            studentNamesList1.add(time + "   " + type);
        }
        adapter1.clear();
        adapter1.addAll(studentNamesList1);
        adapter1.notifyDataSetChanged();
    }

/// method for the toolbar and and return button and when the return button is pressed the closed access goes into the database

    private void ToolbarSetup(){
        setSupportActionBar(toolbar2);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            sql= new MySQL(getApplicationContext());
            String closed="closed";
            int pid=profile.getPid();
            Access accesProfile= new Access(pid,closed,sql.getTime());
            sql.insertStudentAccess(accesProfile);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}