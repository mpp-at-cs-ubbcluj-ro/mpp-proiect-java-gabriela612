package domain;

public class Meci extends Entity<Long> {
    private String nume;
    private double pretBilet;
    private int nrLocuriDisponibile;

    public Meci(String nume, double pretBilet, int nrLocuriDisponibile) {
        this.nume = nume;
        this.pretBilet = pretBilet;
        this.nrLocuriDisponibile = nrLocuriDisponibile;
    }

    public String getNume() {
        return nume;
    }

    public double getPretBilet() {
        return pretBilet;
    }

    public int getNrLocuriDisponibile() {
        return nrLocuriDisponibile;
    }

    public void setNrLocuriDisponibile(int nrLocuriDisponibile) {
        this.nrLocuriDisponibile = nrLocuriDisponibile;
    }
}
