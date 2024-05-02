package org.example.domain;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Objects;

@jakarta.persistence.Entity
@Table(name = "angajati")
public class Angajat extends Entity<Integer> implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String parola;

    @Column
    private String username;

    public Angajat() {
        // Constructor implicit necesar pentru Hibernate
    }

    public Angajat(String parola, String username) {
        this.parola = parola;
        this.username = username;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getParola() {
        return parola;
    }

    public void setParola(String parola) {
        this.parola = parola;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Angajat angajat)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(parola, angajat.parola) &&
                Objects.equals(username, angajat.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), parola, username);
    }
}
