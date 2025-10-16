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
        if (genre.equals("Fiction")){
            return Genre.FICTION;
        } else if (genre.equals("Classics")){
            return Genre.CLASSICS;
        } else if (genre.equals("Romance")){
            return Genre.ROMANCE;
        } else if (genre.equals("Mystery")){
            return Genre.MYSTERY;
        } else if (genre.equals("Thriller")){
            return Genre.THRILLER;
        } else if (genre.equals("Detective")){
            return Genre.DETECTIVE;
        } else if (genre.equals("Adventure")){
            return Genre.ADVENTURE;
        } else if (genre.equals("Fantasy")){
            return Genre.FANTASY;
        } else if (genre.equals("Science_Fiction")){
            return Genre.SCIENCE_FICTION;
        } else if (genre.equals("Historical_Fiction")){
            return Genre.HISTORICAL_FICTION;
        } else if (genre.equals("Drama")){
            return Genre.DRAMA;
        } else if (genre.equals("Poetry")){
            return Genre.POETRY;
        } else if (genre.equals("Philosophy")){
            return Genre.PHILOSOPHY;
        } else if (genre.equals("Psychology")){
            return Genre.PSYCHOLOGY;
        } else if (genre.equals("Sociology")){
            return Genre.SOCIOLOGY;
        } else if (genre.equals("Politics")){
            return Genre.POLITICS;
        } else if (genre.equals("History")){
            return Genre.HISTORY;
        } else if (genre.equals("Education")){
            return Genre.EDUCATION;
        } else if (genre.equals("Law")){
            return Genre.LAW;
        } else if (genre.equals("Religion")){
            return Genre.RELIGION;
        } else if (genre.equals("Anthropology")){
            return Genre.ANTHROPOLOGY;
        } else if (genre.equals("Economy")){
            return Genre.ECONOMY;
        } else if (genre.equals("Finance")){
            return Genre.FINANCE;
        } else if (genre.equals("Management")){
            return Genre.MANAGEMENT;
        } else if (genre.equals("Marketing")){
            return Genre.MARKETING;
        } else if (genre.equals("Entrepreneurship")){
            return Genre.ENTREPRENEURSHIP;
        } else if (genre.equals("Accounting")){
            return Genre.ACCOUNTING;
        } else if (genre.equals("Business_Strategy")){
            return Genre.BUSINESS_STRATEGY;
        } else if (genre.equals("Science")){
            return Genre.SCIENCE;
        } else if (genre.equals("Mathematics")){
            return Genre.MATHEMATICS;
        } else if (genre.equals("Physics")){
            return Genre.PHYSICS;
        } else if (genre.equals("Chemistry")){
            return Genre.CHEMISTRY;
        } else if (genre.equals("Biology")){
            return Genre.BIOLOGY;
        } else if (genre.equals("Medicine")){
            return Genre.MEDICINE;
        } else if (genre.equals("Computer_Science")){
            return Genre.COMPUTER_SCIENCE;
        } else if (genre.equals("Engineering")){
            return Genre.ENGINEERING;
        } else if (genre.equals("Environment")){
            return Genre.ENVIRONMENT;
        } else if (genre.equals("Astronomy")){
            return Genre.ASTRONOMY;
        } else if (genre.equals("Art")){
            return Genre.ART;
        } else if (genre.equals("Architecture")){
            return Genre.ARCHITECTURE;
        } else if (genre.equals("Design")){
            return Genre.DESIGN;
        } else if (genre.equals("Photography")){
            return Genre.PHOTOGRAPHY;
        } else if (genre.equals("Cinema")){
            return Genre.CINEMA;
        } else if (genre.equals("Music")){
            return Genre.MUSIC;
        } else if (genre.equals("Literature")){
            return Genre.LITERATURE;
        } else if (genre.equals("Culture")){
            return Genre.CULTURE;
        } else if (genre.equals("Travel")){
            return Genre.TRAVEL;
        } else if (genre.equals("Cooking")){
            return Genre.COOKING;
        } else if (genre.equals("Fashion")){
            return Genre.FASHION;
        } else if (genre.equals("Beauty")){
            return Genre.BEAUTY;
        } else if (genre.equals("Home")){
            return Genre.HOME;
        } else if (genre.equals("Gardening")){
            return Genre.GARDENING;
        } else if (genre.equals("Sports")){
            return Genre.SPORTS;
        } else if (genre.equals("Well_Being")){
            return Genre.WELL_BEING;
        } else if (genre.equals("Family")){
            return Genre.FAMILY;
        } else if (genre.equals("Lifestyle")){
            return Genre.LIFESTYLE;
        } else if (genre.equals("Children")){
            return Genre.CHILDREN;
        } else if (genre.equals("Teen")){
            return Genre.TEEN;
        } else if (genre.equals("Comics")){
            return Genre.COMICS;
        } else if (genre.equals("Games")){
            return Genre.GAMES;
        } else {
            System.out.println("Genre of document not found !!!!");
            return null;
        }


    }
    public static String convertGenreToString(Genre genre){
        switch (genre) {
            case FICTION:
                return "Fiction";
            case CLASSICS:
                return "Classics";
            case ROMANCE:
                return "Romance";
            case MYSTERY:
                return "Mystery";
            case THRILLER:
                return "Thriller";
            case DETECTIVE:
                return "Detective";
            case ADVENTURE:
                return "Adventure";
            case FANTASY:
                return "Fantasy";
            case SCIENCE_FICTION:
                return "Science_Fiction";
            case HISTORICAL_FICTION:
                return "Historical_Fiction";
            case DRAMA:
                return "Drama";
            case POETRY:
                return "Poetry";
            case PHILOSOPHY:
                return "Philosophy";
            case PSYCHOLOGY:
                return "Psychology";
            case SOCIOLOGY:
                return "Sociology";
            case POLITICS:
                return "Politics";
            case HISTORY:
                return "History";
            case EDUCATION:
                return "Education";
            case LAW:
                return "Law";
            case RELIGION:
                return "Religion";
            case ANTHROPOLOGY:
                return "Anthropology";
            case ECONOMY:
                return "Economy";
            case FINANCE:
                return "Finance";
            case MANAGEMENT:
                return "Management";
            case MARKETING:
                return "Marketing";
            case ENTREPRENEURSHIP:
                return "Entrepreneurship";
            case ACCOUNTING:
                return "Accounting";
            case BUSINESS_STRATEGY:
                return "Business_Strategy";
            case SCIENCE:
                return "Science";
            case MATHEMATICS:
                return "Mathematics";
            case PHYSICS:
                return "Physics";
            case CHEMISTRY:
                return "Chemistry";
            case BIOLOGY:
                return "Biology";
            case MEDICINE:
                return "Medicine";
            case COMPUTER_SCIENCE:
                return "Computer_Science";
            case ENGINEERING:
                return "Engineering";
            case ENVIRONMENT:
                return "Environment";
            case ASTRONOMY:
                return "Astronomy";
            case ART:
                return "Art";
            case ARCHITECTURE:
                return "Architecture";
            case DESIGN:
                return "Design";
            case PHOTOGRAPHY:
                return "Photography";  
            case CINEMA:
                return "Cinema";
            case MUSIC:
                return "Music"; 
            case LITERATURE:
                return "Literature";
            case CULTURE:
                return "Culture";
            case TRAVEL:
                return "Travel";
            case COOKING:
                return "Cooking";  
            case FASHION:
                return "Fashion";
            case BEAUTY:
                return "Beauty";
            case HOME:
                return "Home";
            case GARDENING:
                return "Gardening";
            case SPORTS:
                return "Sports";
            case WELL_BEING:
                return "Well_Being";
            case FAMILY:
                return "Family";   
            case LIFESTYLE:
                return "Lifestyle";
            case CHILDREN:
                return "Children";
            case TEEN:
                return "Teen";
            case COMICS:
                return "Comics";
            case GAMES:
                return "Games";
            default:
                return "Unknown";  
        }
    }
}
