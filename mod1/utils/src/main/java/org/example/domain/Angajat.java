package org.example.domain;

import java.io.Serializable;
import java.util.Objects;

public class Angajat extends Entity<Integer> implements Serializable {
    private String parola;
    private String username;

    public Angajat(String parola, String username) {
        this.parola = parola;
        this.username = username;
    }

    public String getParola() {
        return parola;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Angajat angajat)) return false;
        if (super.equals(o)) return true;
        return Objects.equals(parola, angajat.parola) &&
                Objects.equals(username, angajat.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), parola, username);
    }
}
