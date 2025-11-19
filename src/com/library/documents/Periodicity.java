package com.library.documents;

public enum Periodicity {
    DAILY,
    WEEKLY,
    MONTHLY,
    QUARTERLY,
    ANNUALLY;

    public static Periodicity convertStringToPeriodicity(String periodicity){
        if (periodicity == null) {
            return null;
        }

        String normalized = periodicity.trim()
            .toUpperCase();

        try {
            return Periodicity.valueOf(normalized);
        } catch (IllegalArgumentException e) {
            System.out.println("Periodicity not found: " + periodicity);
            return null;
        }
    }

    @Override
    public String toString(){
        String formatted = this.name().toLowerCase();
        return formatted.substring(0, 1).toUpperCase() + formatted.substring(1);
    }
}
