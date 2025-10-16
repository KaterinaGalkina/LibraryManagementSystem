package com.library.people;

import java.time.LocalDate;

public abstract class Person {
    private int id;
    private String first_name;
    private String last_name;
    private LocalDate birth_date;
    static private int next_id = 0;

    Person(String first_name, String last_name, LocalDate birth_date){
        this.first_name = first_name;
        this.last_name = last_name;
        this.birth_date = birth_date;
        this.id = next_id; 
        next_id ++;
    }

    // When we are retriving the information from the database, we want to set the same id, without incrementing number of people
    Person(int id, String first_name, String last_name, LocalDate birth_date){
        this.id = id; 
        this.first_name = first_name;
        this.last_name = last_name;
        this.birth_date = birth_date;
        if (next_id < id){ // We are taking the largest one, so that there are no duplicates
            next_id = id + 1;
        } 
    }

    @Override
    public String toString() {
        return this.first_name + " " + this.last_name + " " + this.birth_date.toString();
    }

    public int getId() {
        return this.id;
    }

    public String getFirst_name() {
        return this.first_name;
    }

    public String getLast_name() {
        return this.last_name;
    }

    public LocalDate getBirth_date() {
        return this.birth_date;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public void setBirth_date(LocalDate birth_date) {
        this.birth_date = birth_date;
    }
}
