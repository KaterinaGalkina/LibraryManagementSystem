package documents;
public enum Genre {
    ACTION,
    DRAMA,
    FANTASY,
    ANIMATION,
    THRILLER,
    HISTORICAL,
    ROMANCE,
    MYSTERY,
    SCIENCE_FICTION,
    FICTION,
    COMEDY;

    public static Genre convertStringToGenre(String genre){
        if (genre.equals("Action")){
            return Genre.ACTION;
        } else if (genre.equals("Drama")){
            return Genre.DRAMA;
        } else if (genre.equals("Fantasy")){
            return Genre.FANTASY;
        } else if (genre.equals("Animation")){
            return Genre.ANIMATION;
        } else if (genre.equals("Thriller")){
            return Genre.THRILLER;
        } else if (genre.equals("Historical")){
            return Genre.HISTORICAL;
        } else if (genre.equals("Romance")){
            return Genre.ROMANCE;
        } else if (genre.equals("Mystery")){
            return Genre.MYSTERY;
        } else if (genre.equals("Science_Fiction")){
            return Genre.SCIENCE_FICTION;
        } else if (genre.equals("Fiction")){
            return Genre.FICTION;
        }else if (genre.equals("Comedy")){
            return Genre.COMEDY;
        } else {
            System.out.println("Genre of document not found !!!!");
            return null;
        }
    }

    public static String convertGenreToString(Genre genre){
        if (genre == Genre.ACTION){
            return "Action";
        } else if (genre == Genre.DRAMA){
            return "Drama";
        } else if (genre == Genre.FANTASY){
            return "Fantasy";
        } else if (genre == Genre.ANIMATION){
            return "Animation";
        } else if (genre == Genre.THRILLER){
            return "Thriller";
        } else if (genre == Genre.HISTORICAL){
            return "Historical";
        } else if (genre == Genre.ROMANCE){
            return "Romance";
        } else if (genre == Genre.MYSTERY){
            return "Mystery";
        } else if (genre == Genre.SCIENCE_FICTION){
            return "Science_Fiction";
        } else if (genre == Genre.FICTION){
            return "Fiction";
        } else if (genre == Genre.COMEDY){
            return "Comedy";
        } else {
            System.out.println("Genre of a document is not found !!!!");
            return null;
        }
    }
}
