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
    private static int next_id = 0;

    public Borrowing(Document document, Member member, LocalDate borrowing_date, LocalDate return_date){
        this.document = document;
        this.member = member;
        this.borrowing_date = borrowing_date;
        this.return_date = return_date;
        this.real_return_date = null;
        this.id_borrowing = next_id;
        next_id++;
        member.setNumber_borrowings(member.getNumber_borrowings() + 1);
    }

    // return date by default
    public Borrowing(Document document, Member member, LocalDate borrowing_date){
        this.document = document;
        this.member = member;
        this.borrowing_date = borrowing_date;
        this.return_date = LocalDate.now().plusWeeks(3);
        this.real_return_date = null;
        this.id_borrowing = next_id;
        next_id++;
        member.setNumber_borrowings(member.getNumber_borrowings() + 1);
    }

    // We already have all the information, because it was in the database
    public Borrowing(int id_borrowing, Document document, Member member, LocalDate borrowing_date, LocalDate return_date, LocalDate real_return_date){
        this.id_borrowing = id_borrowing;
        this.document = document;
        this.member = member;
        this.borrowing_date = borrowing_date;
        this.return_date = return_date;
        this.real_return_date = real_return_date;
        if (next_id <= id_borrowing){
            next_id = id_borrowing + 1;
        }
    }

    public String toString(){
        if (this.real_return_date != null){
            return this.document.toString() + " from " + this.borrowing_date.toString() + " until " + this.real_return_date.toString() + " - Finished";
        } else {
            return this.document.toString() + " from " + this.borrowing_date.toString() + " (until " + this.return_date.toString() + ") - Not Finished";
        }
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
