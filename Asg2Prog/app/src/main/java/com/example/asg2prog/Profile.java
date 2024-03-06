package com.example.asg2prog;

public class Profile {


    private int pid;
    private String first,last,time;
    private float GPA;
    public Profile(int pid,  String first, String last, float gpa, String time){
        this.pid=pid;
        this.first=first;
        this.last=last;
        this.GPA=gpa;
        this.time=time;
    }
    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }
    public float getGPA() {
        return GPA;
    }
public String getTime() {return time;}
    public void setTime(String time){this.time=time;}
    public void setGPA(float gpa) {
        this.GPA = gpa;
    }

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }
    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }

}
