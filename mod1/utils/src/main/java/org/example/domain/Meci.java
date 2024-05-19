package org.example.domain;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.io.Serializable;
import java.time.LocalDate;

@jakarta.persistence.Entity
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Meci extends Entity<Integer> implements Serializable {
    @Id // Marchează câmpul ca fiind cheie primară
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Specifică modul de generare a valorii ID
    private Integer id;
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

    public Meci() {

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

    public void setNume(String nume) {
        this.nume = nume;
    }

    public void setPretBilet(double pretBilet) {
        this.pretBilet = pretBilet;
    }

    public void setCapacitate(int capacitate) {
        this.capacitate = capacitate;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }
}
