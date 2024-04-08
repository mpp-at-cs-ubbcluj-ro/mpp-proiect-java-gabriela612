package org.example.services;

import org.example.domain.Bilet;
import org.example.domain.Meci;
import org.example.observer.IObserver;

public interface IServices {
    public Integer login(String username, String parola, IObserver client);
    public Bilet cumparaBilet(Meci meci, String numeClient, int nrLocuri);
    public int nrLocuriDisponibileMeci(Meci meci);
    public Iterable<Meci> getMeciuri();
    public Iterable<Meci> getMeciuriLibere();
    public void logout(Integer idAngajat);
}
