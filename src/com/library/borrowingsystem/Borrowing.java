package com.library.borrowingsystem;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import com.library.documents.Document;
import com.library.people.Member;

public class Borrowing {
    private int id_borrowing;
    private Document document;
    private Member member;
    private LocalDate borrowing_date;
    private LocalDate return_date;
    private LocalDate real_return_date;
    private boolean fine_paid;
    private static int next_id = 0;

    public Borrowing(Document document, Member member, LocalDate borrowing_date, LocalDate return_date){
        this.document = document;
        this.member = member;
        this.borrowing_date = borrowing_date;
        this.return_date = return_date;
        this.real_return_date = null;
        this.id_borrowing = next_id;
        this.fine_paid = false;
        next_id++;
    }

    // return date by default
    public Borrowing(Document document, Member member, LocalDate borrowing_date){
        this.document = document;
        this.member = member;
        this.borrowing_date = borrowing_date;
        this.return_date =  borrowing_date.plusWeeks(3);
        this.real_return_date = null;
        this.id_borrowing = next_id;
        this.fine_paid = false;
        next_id++;
    }

    // We already have all the information, because it was in the database
    public Borrowing(int id_borrowing, Document document, Member member, LocalDate borrowing_date, LocalDate return_date, LocalDate real_return_date, boolean fine_paid){
        this.id_borrowing = id_borrowing;
        this.document = document;
        this.member = member;
        this.borrowing_date = borrowing_date;
        this.return_date = return_date;
        this.fine_paid = fine_paid;
        this.real_return_date = real_return_date;
        if (next_id <= id_borrowing){
            next_id = id_borrowing + 1;
        }
    }

    @Override
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

    public boolean getFine_paid() {
        return this.fine_paid;
    }

    public double getPenalty() {
        if (fine_paid) {
            return 0;
        }
        LocalDate today = LocalDate.now();
        // If the borrowing is fine
        if (real_return_date != null) {
            if (!real_return_date.isAfter(return_date)) {
                return 0.;
            }
        } else {
            if (!today.isAfter(return_date)) { 
                return 0.;
            }
        }
        long daysLate;
        if (this.real_return_date == null){
            daysLate = ChronoUnit.DAYS.between(this.return_date, today);
        } else {
            daysLate = ChronoUnit.DAYS.between(this.return_date, this.real_return_date);
        }
        return daysLate * 0.5;
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

    public void setFine_paid(boolean fine_paid) {
        if (this.real_return_date == null){
            System.out.println("Impossible to pay a fine of an active overdue");
            return;
        }
        this.fine_paid = fine_paid;
    }
}
