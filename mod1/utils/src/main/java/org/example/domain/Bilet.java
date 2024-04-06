package org.example.domain;

public class Bilet extends Entity<Integer> {
    private Meci meci;
    private String numeClient;
    private int nrLocuri;

    public Bilet(Meci meci, String numeClient, int nrLocuri) {
        this.meci = meci;
        this.numeClient = numeClient;
        this.nrLocuri = nrLocuri;
    }

    public Meci getMeci() {
        return meci;
    }

    public String getNumeClient() {
        return numeClient;
    }

    public int getNrLocuri() {
        return nrLocuri;
    }
}
