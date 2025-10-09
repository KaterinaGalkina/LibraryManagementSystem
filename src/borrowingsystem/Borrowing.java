package borrowingsystem;

import java.time.LocalDate;

import documents.Document;
import people.Member;

public class Borrowing {
    private int id_borrowing;
    private Document document;
    private Member member;
    private LocalDate borrowing_date;
    private LocalDate return_date;
    private LocalDate real_return_date;
    private static int number_borrowings = 0;

    Borrowing(Document document, Member member, LocalDate borrowing_date, LocalDate return_date){
        this.document = document;
        this.member = member;
        this.borrowing_date = borrowing_date;
        this.return_date = return_date;
        this.real_return_date = null;
        this.id_borrowing = number_borrowings;
        number_borrowings++;
        member.setNumber_borrowings(member.getNumber_borrowings() + 1);
    }

    Borrowing(int id_borrowing, Document document, Member member, LocalDate borrowing_date, LocalDate return_date, LocalDate real_return_date){
        this.id_borrowing = id_borrowing;
        this.document = document;
        this.member = member;
        this.borrowing_date = borrowing_date;
        this.return_date = return_date;
        this.real_return_date = real_return_date;
    }

    public int getId_borrowing() {
        return this.id_borrowing;
    }

    public Document getDocument() {
        return this.document;
    }

    public Member getMember() {
        return this.member;
    }

    public LocalDate getBorrowing_date() {
        return this.borrowing_date;
    }

    public LocalDate getReturn_date() {
        return this.return_date;
    }

    public LocalDate getReal_return_date() {
        return this.real_return_date;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public void setBorrowing_date(LocalDate borrowing_date) {
        this.borrowing_date = borrowing_date;
    }

    public void setReturn_date(LocalDate return_date) {
        this.return_date = return_date;
    }

    public void setReal_return_date(LocalDate real_return_date) {
        this.real_return_date = real_return_date;
    }
}
