package com.library.documents;

public enum Periodicity {
    DAILY,
    WEEKLY,
    MONTHLY,
    QUARTERLY,
    ANNUALLY;

    public static Periodicity convertStringToPeriodicity(String periodicity){
        if (periodicity.equals("Daily")){
            return Periodicity.DAILY;
        } else if (periodicity.equals("Weekly")){
            return Periodicity.WEEKLY;
        } else if (periodicity.equals("Monthly")){
            return Periodicity.MONTHLY;
        } else if (periodicity.equals("Quarterly")){
            return Periodicity.QUARTERLY;
        } else if (periodicity.equals("Annually")){
            return Periodicity.ANNUALLY;
        } else {
            System.out.println("Periodicity of a document is not found !!!!");
            return null;
        }
    }

    public static String convertPeriodicityToString(Periodicity periodicity){
        if (periodicity == Periodicity.DAILY){
            return "Daily";
        } else if (periodicity == Periodicity.WEEKLY){
            return "Weekly";
        } else if (periodicity == Periodicity.MONTHLY){
            return "Monthly";
        } else if (periodicity == Periodicity.QUARTERLY){
            return "Quarterly";
        } else if (periodicity == Periodicity.ANNUALLY){
            return "Annually";
        } else {
            System.out.println("Periodicity of a document is not found !!!!");
            return null;
        }
    }
}
