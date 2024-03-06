package domain;

public class Bilet extends Entity<Long> {
    private Long idMeci;
    private String numeClient;
    private int nrLocuri;

    public Bilet(Long idMeci, String numeClient, int nrLocuri) {
        this.idMeci = idMeci;
        this.numeClient = numeClient;
        this.nrLocuri = nrLocuri;
    }
}
