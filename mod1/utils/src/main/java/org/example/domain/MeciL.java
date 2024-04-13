package org.example.domain;

import java.time.LocalDate;

public class MeciL extends Meci {
    private int nrLocuriDisponibile;
    public MeciL(String nume, double pretBilet, int capacitate, LocalDate data, int nrLocuriDisponibile) {
        super(nume, pretBilet, capacitate, data);
        this.nrLocuriDisponibile = nrLocuriDisponibile;
    }

    public int getNrLocuriDisponibile() {
        return nrLocuriDisponibile;
    }
}
