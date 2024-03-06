package com.example.asg2prog;

import java.util.Formatter;

public class Access {
private int id,pid;
private String type,time;


    public Access ( int pid, String type, String time){
        this.pid=pid;
        this.type=type;
        this.time=time;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


}