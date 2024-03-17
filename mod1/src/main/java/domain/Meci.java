package domain;

import java.time.LocalDate;

public class Meci extends Entity<Integer> {
    private String nume;
    private double pretBilet;
    private int capacitate;
    private LocalDate data;

    public Meci(String nume, double pretBilet, int capacitate, LocalDate data) {
        this.nume = nume;
        this.pretBilet = pretBilet;
        this.capacitate = capacitate;
        this.data = data;
    }

    public String getNume() {
        return nume;
    }

    public double getPretBilet() {
        return pretBilet;
    }

    public int getCapacitate() {
        return capacitate;
    }

    public LocalDate getData() {
        return data;
    }
}
