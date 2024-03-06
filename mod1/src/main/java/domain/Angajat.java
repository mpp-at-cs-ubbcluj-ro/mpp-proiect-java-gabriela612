package domain;

import java.util.Objects;

public class Angajat extends Entity<Long> {
    private String parola;

    public Angajat(String parola) {
        this.parola = parola;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Angajat angajat)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(parola, angajat.parola);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), parola);
    }
}
