package com.seds519.hw1.model;

public class Course {
    private String day;
    private String name;
    private String timeSlot;
    private String instructor;
    private String classroom;

    public Course(String day, String name, String timeSlot, String instructor, String classroom) {
        this.day = day;
        this.name = name;
        this.timeSlot = timeSlot;
        this.instructor = instructor;
        this.classroom = classroom;
    }

    public String getDay() {
        return day;
    }

    public String getName() {
        return name;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public String getInstructor() {
        return instructor;
    }

    public String getClassroom() {
        return classroom;
    }

}
