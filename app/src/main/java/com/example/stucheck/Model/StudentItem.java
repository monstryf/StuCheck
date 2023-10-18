package com.example.stucheck.Model;

public class StudentItem {


    private long sid;
    private int roll;
    private String studentName;
    private String status;
    public StudentItem(long sid, int roll, String studentName) {
        this.sid = sid;
        this.studentName = studentName;
        this.roll = roll;
        status = "";
    }
    public long getSid() {
        return sid;
    }

    public void setSid(long sid) {
        this.sid = sid;
    }
    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public int getRoll() {
        return roll;
    }

    public void setRoll(int roll) {
        this.roll = roll;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
