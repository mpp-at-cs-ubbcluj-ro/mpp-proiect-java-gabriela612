package domain;

public class Bilet extends Entity<Long> {
    private Meci meci;
    private String numeClient;
    private int nrLocuri;

    public Bilet(Meci meci, String numeClient, int nrLocuri) {
        this.meci = meci;
        this.numeClient = numeClient;
        this.nrLocuri = nrLocuri;
    }
}
