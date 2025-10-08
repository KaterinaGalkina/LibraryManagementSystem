package people;

import java.time.LocalDate;

public abstract class Person {
    private int id;
    private String first_name;
    private String last_name;
    private LocalDate birth_date;
    static private int nb_people = 0;

    Person(String first_name, String last_name, LocalDate birth_date){
        this.first_name = first_name;
        this.last_name = last_name;
        this.birth_date = birth_date;
        this.id = nb_people; 
        nb_people ++;
    }

    // When we are retriving the information from the database, we want to set the same id, without incrementing number of people
    Person(int id, String first_name, String last_name, LocalDate birth_date){
        this.id = id; 
        this.first_name = first_name;
        this.last_name = last_name;
        this.birth_date = birth_date;
    }

    public int getId() {
        return id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public LocalDate getBirth_date() {
        return birth_date;
    }
}
