package org.example.service;

import org.example.observer.IObserver;
import org.example.repository.*;
import org.example.domain.*;
import org.example.services.IServices;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Service implements IServices {
    IAngajatRepository angajatRepository;
    IMeciRepository meciRepository;
    IBiletRepository biletRepository;
    private Map<Integer, IObserver> loggedClients;
    private final int defaultThreadsNo=5;

    public Service(IAngajatRepository angajatRepository, IMeciRepository meciRepository, IBiletRepository biletRepository) {
        this.angajatRepository = angajatRepository;
        this.meciRepository = meciRepository;
        this.biletRepository = biletRepository;
        this.loggedClients = new HashMap<>();
    }

    @Override
    public synchronized Integer login(String username, String parola, IObserver client) {
        Angajat angajat = angajatRepository.findByUsername(username);
        if (angajat.getId() == null)
            return null;
        if (loggedClients.get(angajat.getId()) != null)
            return null;
        if (angajat.equals(new Angajat(parola, username)))
        {
            this.loggedClients.put(angajat.getId(), client);
            return angajat.getId();
        }
        return null;
    }

    public synchronized void logout(Integer idAngajat) {
        IObserver localClient = loggedClients.remove(idAngajat);
//        if (localClient==null)
//            throw new Exception("User "+idAngajat+" is not logged in.");
    }

    private void notifyCumparareBilet(Iterable<MeciL> meciuri) {
        System.out.println("All: "+meciuri);

        ExecutorService executor= Executors.newFixedThreadPool(defaultThreadsNo);
        for(IObserver client : loggedClients.values()){
            if (client!=null)
                executor.execute(() -> {
                    try {
                        System.out.println("Notifying " + meciuri);
                        client.schimbareMeciuri(meciuri);
                    } catch (Exception e) {
                        System.err.println("Error notifying client " + e);
                    }
                });
        }

        executor.shutdown();
    }


    @Override
    public synchronized Bilet cumparaBilet(Meci meci, String numeClient, int nrLocuri) {
        if (nrLocuri > this.nrLocuriDisponibileMeci(meci))
            return null;
        Bilet bilet = new Bilet(meci, numeClient, nrLocuri);
        bilet = biletRepository.save(bilet);
        if (bilet.getId() == null)
            return null;
        notifyCumparareBilet(this.getMeciuri());
        return bilet;
    }

    @Override
    public synchronized int nrLocuriDisponibileMeci(Meci meci) {
        return meci.getCapacitate() -  biletRepository.nrLocuriOcupateMeci(meci.getId());
    }

    @Override
    public synchronized Iterable<MeciL> getMeciuri() {
        List<Meci> meciuri = (List<Meci>) meciRepository.findAll();
        List<MeciL> meciList = new ArrayList<>();
        meciuri.forEach(m -> {
            MeciL ml = new MeciL(m.getNume(), m.getPretBilet(), m.getCapacitate(), m.getData(), this.nrLocuriDisponibileMeci(m));
            ml.setId(m.getId());
            meciList.add(ml);
        });
        return meciList;
    }

    @Override
    public synchronized Iterable<MeciL> getMeciuriLibere() {
        List<Meci> meciuri = (List<Meci>) meciRepository.findMeciuriDisponibile();
        List<MeciL> meciList = new ArrayList<>();
        meciuri.forEach(m -> {
            MeciL ml = new MeciL(m.getNume(), m.getPretBilet(), m.getCapacitate(), m.getData(), this.nrLocuriDisponibileMeci(m));
            ml.setId(m.getId());
            meciList.add(ml);
        });
        return meciList;
    }
}
