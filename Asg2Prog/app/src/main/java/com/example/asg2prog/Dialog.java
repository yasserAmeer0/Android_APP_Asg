package com.example.asg2prog;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

public class Dialog extends DialogFragment {

    protected Button save,cancel;
    protected EditText FirstName, LastName, StudentID,GPA;
    protected MySQL sql;


    public Dialog() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=   inflater.inflate(R.layout.fragment_dialog, container, false);
        save=view.findViewById(R.id.Save);
        cancel=view.findViewById(R.id.Cancel);
        FirstName=view.findViewById(R.id.InpFirst);
        LastName=view.findViewById(R.id.InpLast);
        StudentID=view.findViewById(R.id.InptID);
        GPA=view.findViewById(R.id.InpGPA);


        /// making sure the information of the user are accurate and good to be stored
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String first = FirstName.getText().toString();
                String last = LastName.getText().toString();
                int id = Integer.parseInt(StudentID.getText().toString());
                float Gpa = Float.parseFloat(GPA.getText().toString());
                String created=" created";

                if (first.isEmpty()||last.isEmpty()){
                    Toast.makeText(getActivity(), "Fields cannot be empty!", Toast.LENGTH_LONG).show();
                }
                if ( id < 10000000 || id > 99999999) {
                    Toast.makeText(getActivity(), "WRONG ID INPUT, must between 100000 and 9999999 and 8 integer", Toast.LENGTH_SHORT).show();
                }

                else if (Gpa < 0 || Gpa > 4.3) {
                    Toast.makeText(getActivity(), "WRONG GPA INPUT, most be between 0 and 4.3 ", Toast.LENGTH_SHORT).show();
                }
                else {
                    // storing student information and the access type of created
                    sql= new MySQL(getActivity());
                    Access accesProfile= new Access(id,created, sql.getTime());
                    Profile student = new Profile(id, first, last, Gpa, sql.getTime());
                    sql.insertStudentAccess(accesProfile);
                    sql.insertStudent(student);
                    Toast.makeText(getActivity(), "New Student Added!", Toast.LENGTH_LONG).show();
                    ((MainActivity) getActivity()).loadTextView();
                    ((MainActivity) getActivity()).updateTotal();
                    dismiss();
                }}
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return view;
    }



}

