package com.library.people;

import java.time.LocalDate;

public class Member extends Person {
    private boolean penalty_status;
    private String phone_number;
    private String address;
    private String mail;
    private boolean is_library_worker;

    public Member(String first_name, String last_name, LocalDate birth_date, String phone_number, String address, String mail) {
        super(first_name, last_name, birth_date);
        this.penalty_status = false; // Every person is innocent until proven guilty
        this.phone_number = phone_number;
        this.address = address;
        this.mail = mail;
        this.is_library_worker = false;
    }

    public Member(String first_name, String last_name, LocalDate birth_date, int id, boolean penalty_status, String phone_number, String address, String mail, boolean is_library_worker) {
        super(id, first_name, last_name, birth_date);
        this.penalty_status = penalty_status; 
        this.phone_number = phone_number;
        this.address = address;
        this.mail = mail;
        this.is_library_worker = is_library_worker;
    }

    @Override
    public String toString() {
        return super.toString() + " penalty_status: " + this.penalty_status + " phone_number: " + this.phone_number;
    }

    public boolean getPenalty_status() {
        return this.penalty_status;
    }

    public String getPhone_number() {
        return this.phone_number;
    }

    public String getAddress() {
        return this.address;
    }

    public String getMail() {
        return this.mail;
    }

    public boolean getIs_library_worker(){
        return this.is_library_worker;
    }

    public void setPenalty_status(boolean penalty_status) {
        this.penalty_status = penalty_status;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setIs_library_worker(boolean is_library_worker) {
        this.is_library_worker = is_library_worker;
    }
}
