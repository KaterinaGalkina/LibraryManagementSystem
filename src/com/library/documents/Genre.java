package com.library.documents;
public enum Genre {
    // Literature & Fiction
    FICTION,
    CLASSICS,
    ROMANCE,
    MYSTERY,
    THRILLER,
    DETECTIVE,
    ADVENTURE,
    FANTASY,
    SCIENCE_FICTION,
    HISTORICAL_FICTION,
    DRAMA,
    POETRY,

    // Humanities & Social Sciences
    PHILOSOPHY,
    PSYCHOLOGY,
    SOCIOLOGY,
    POLITICS,
    HISTORY,
    EDUCATION,
    LAW,
    RELIGION,
    ANTHROPOLOGY,

    // Economics & Business
    ECONOMY,
    FINANCE,
    MANAGEMENT,
    MARKETING,
    ENTREPRENEURSHIP,
    ACCOUNTING,
    BUSINESS_STRATEGY,

    // Science & Technology
    SCIENCE,
    MATHEMATICS,
    PHYSICS,
    CHEMISTRY,
    BIOLOGY,
    MEDICINE,
    COMPUTER_SCIENCE,
    ENGINEERING,
    ENVIRONMENT,
    ASTRONOMY,

    // Arts & Culture
    ART,
    ARCHITECTURE,
    DESIGN,
    PHOTOGRAPHY,
    CINEMA,
    MUSIC,
    LITERATURE,
    CULTURE,

    // Lifestyle & Society
    TRAVEL,
    COOKING,
    FASHION,
    BEAUTY,
    HOME,
    GARDENING,
    SPORTS,
    WELL_BEING,
    FAMILY,
    LIFESTYLE,

    // Youth & Entertainment
    CHILDREN,
    TEEN,
    COMICS,
    GAMES;


    public static Genre convertStringToGenre(String genre){
        if (genre == null) {
            return null;
        }

        String normalized = genre.trim()
            .toUpperCase()
            .replace(" ", "_");

        try {
            return Genre.valueOf(normalized);
        } catch (IllegalArgumentException e) {
            System.out.println("Genre not found: " + genre);
            return null;
        }
    }

    @Override
    public String toString(){
        String formatted = this.name().toLowerCase().replace("_", " ");
        return formatted.substring(0, 1).toUpperCase() + formatted.substring(1);
    }
}
