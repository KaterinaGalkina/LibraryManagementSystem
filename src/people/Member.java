package people;

import java.time.LocalDate;

public class Member extends Person {
    private boolean penalty_status;
    private String phone_number;
    private String address;
    private String mail;

    Member(String first_name, String last_name, LocalDate birth_date, String phone_number, String address, String mail) {
        super(first_name, last_name, birth_date);
        this.penalty_status = false; // Every person is innocent until proven guilty
        this.phone_number = phone_number;
        this.address = address;
        this.mail = mail;
    }

    public boolean getPenalty_status() {
        return penalty_status;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public String getAddress() {
        return address;
    }

    public String getMail() {
        return mail;
    }
    
}
