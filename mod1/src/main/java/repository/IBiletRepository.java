package repository;

import domain.Bilet;

public interface IBiletRepository extends Repository<Integer, Bilet> {
    @Override
    Bilet create(Bilet entity);

    @Override
    int size();
}
