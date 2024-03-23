package pachet.repository;

import pachet.domain.Angajat;

public interface IAngajatRepository extends Repository<Integer, Angajat> {
    public Angajat findByUsername(String username);
}
