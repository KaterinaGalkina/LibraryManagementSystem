package people;

import java.time.LocalDate;

public class Member extends Person {
    private boolean penalty_status;
    private String phone_number;
    private String address;
    private String mail;
    private int number_borrowings;

    Member(String first_name, String last_name, LocalDate birth_date, String phone_number, String address, String mail) {
        super(first_name, last_name, birth_date);
        this.penalty_status = false; // Every person is innocent until proven guilty
        this.phone_number = phone_number;
        this.address = address;
        this.mail = mail;
        this.number_borrowings = 0;
    }

    Member(String first_name, String last_name, LocalDate birth_date, int id, boolean penalty_status, String phone_number, String address, String mail, int number_borrowings) {
        super(id, first_name, last_name, birth_date);
        this.penalty_status = penalty_status; 
        this.phone_number = phone_number;
        this.address = address;
        this.mail = mail;
        this.number_borrowings = number_borrowings;
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

    public int getNumber_borrowings() {
        return this.number_borrowings;
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

    public void setNumber_borrowings(int number_borrowings) {
        this.number_borrowings = number_borrowings;
    }
}
