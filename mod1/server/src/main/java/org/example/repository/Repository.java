package org.example.repository;

import org.example.domain.*;

public interface Repository<ID, E extends Entity<ID>> {
    public Meci findOne(ID id);
    public Iterable<Meci> findAll();
    public E save(E entity);
    public E delete(ID id);
    public E update(E entity);
    public int size();
}
