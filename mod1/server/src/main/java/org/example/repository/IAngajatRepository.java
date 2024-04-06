package org.example.repository;

import org.example.domain.Angajat;
import org.example.domain.*;

public interface IAngajatRepository extends Repository<Integer, Angajat> {
    public Angajat findByUsername(String username);
}
