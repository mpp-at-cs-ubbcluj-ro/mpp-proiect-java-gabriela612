package pachet.domain;

import java.util.Objects;

public class Angajat extends Entity<Integer> {
    private String parola;
    private String username;

    public Angajat(String parola, String username) {
        this.parola = parola;
        this.username = username;
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
