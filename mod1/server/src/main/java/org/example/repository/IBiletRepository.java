package org.example.repository;

import org.example.domain.Bilet;

public interface IBiletRepository extends Repository<Integer, Bilet> {
    @Override
    Bilet save(Bilet entity);

    @Override
    int size();

    int nrLocuriOcupateMeci(Integer idMeci);
}
