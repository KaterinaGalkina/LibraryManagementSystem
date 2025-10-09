package documents;

import java.util.Set;

import people.Author;

import java.time.LocalDate;

public class Magazine extends Document{
    private Periodicity periodicity;
    private int issue_number;
    private LocalDate issue_date; // = LocalDate.of(1986, 4, 26);

    Magazine(Set<Type> type, String title, int nb_copies, Set<Author> authors, Periodicity periodicity, int issue_number, LocalDate issue_date) {
        super(type, title, nb_copies, authors);
        this.periodicity = periodicity;
        this.issue_number = issue_number;
        this.issue_date = issue_date;
    }

    Magazine(Set<Type> type, String title, int nb_copies, Set<Author> authors, int id, Periodicity periodicity, int issue_number, LocalDate issue_date) {
        super(type, title, nb_copies, id, authors);
        this.periodicity = periodicity;
        this.issue_number = issue_number;
        this.issue_date = issue_date;
    }

    public Periodicity getPeriodicity() {
        return this.periodicity;
    }

    public int getIssue_number() {
        return this.issue_number;
    }

    public LocalDate getIssue_date() {
        return this.issue_date;
    }
    
    public void setPeriodicity(Periodicity periodicity) {
        this.periodicity = periodicity;
    }

    public void setIssue_number(int issue_number) {
        this.issue_number = issue_number;
    }

    public void setDate(LocalDate issue_date) {
        this.issue_date = issue_date;
    }
}
