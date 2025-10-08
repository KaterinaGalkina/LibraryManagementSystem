package documents;

import java.util.Set;

import people.Author;

import java.time.LocalDate;

public class Magazine extends Document{
    private Periodicity periodicity;
    private int number;
    private LocalDate date; // = LocalDate.of(1986, 4, 26);

    Magazine(Set<Type> type, String title, int nb_copies, Set<Author> authors, Periodicity periodicity, int number, LocalDate date) {
        super(type, title, nb_copies, authors);
        this.periodicity = periodicity;
        this.number = number;
        this.date = date;
    }

    public Periodicity getPeriodicity() {
        return periodicity;
    }

    public int getNumber() {
        return number;
    }

    public LocalDate getDate() {
        return date;
    }
    
}
