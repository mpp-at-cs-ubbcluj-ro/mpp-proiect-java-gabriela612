package pachet.repository;

import pachet.domain.Meci;

public interface IMeciRepository extends Repository<Integer, Meci> {
    @Override
    Meci findOne(Integer integer);

    @Override
    Iterable<Meci> findAll();

    Iterable<Meci> findMeciuriDisponibile();
}
