package documents;

public class Magazine{
    private int magazine_id;
    private String magazine_title;
    private Periodicity periodicity;
    private static int nb_magazines;

    public Magazine(String magazine_title, Periodicity periodicity) {
        this.magazine_id = nb_magazines;
        this.magazine_title = magazine_title;
        this.periodicity = periodicity;
        nb_magazines++;
    }

    public Magazine(int magazine_id, String magazine_title, Periodicity periodicity) {
        this.magazine_id = magazine_id;
        this.magazine_title = magazine_title;
        this.periodicity = periodicity;
    }

    @Override
    public String toString(){
        return "\"" + this.magazine_title + "\" of periodicity: " + Periodicity.convertPeriodicityToString(this.periodicity);
    }

    public int getMagazine_id() {
        return this.magazine_id;
    }

    public String getMagazine_title() {
        return this.magazine_title;
    }

    public Periodicity getPeriodicity() {
        return this.periodicity;
    }
    
    public void setMagazine_id(int magazine_id) {
        this.magazine_id = magazine_id;
    }

    public void setMagazine_title(String magazine_title) {
        this.magazine_title = magazine_title;
    }

    public void setPeriodicity(Periodicity periodicity) {
        this.periodicity = periodicity;
    }

}
