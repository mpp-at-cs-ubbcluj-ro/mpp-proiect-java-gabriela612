package org.example.domain;

import java.io.Serializable;

public class Bilet extends Entity<Integer> implements Serializable {
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
