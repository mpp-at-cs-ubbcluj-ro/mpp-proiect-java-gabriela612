package pachet.service;

import pachet.domain.Angajat;
import pachet.domain.Bilet;
import pachet.domain.Meci;
import pachet.repository.IAngajatRepository;
import pachet.repository.IBiletRepository;
import pachet.repository.IMeciRepository;

public class Service {
    IAngajatRepository angajatRepository;
    IMeciRepository meciRepository;
    IBiletRepository biletRepository;

    public Service(IAngajatRepository angajatRepository, IMeciRepository meciRepository, IBiletRepository biletRepository) {
        this.angajatRepository = angajatRepository;
        this.meciRepository = meciRepository;
        this.biletRepository = biletRepository;
    }

    public Integer Login(String username, String parola) {
        Angajat angajat = angajatRepository.findByUsername(username);
        if (angajat.getId() == null)
            return null;
        if (angajat.equals(new Angajat(parola, username)))
            return angajat.getId();
        return null;
    }

    public Bilet cumparaBilet(Meci meci, String numeClient, int nrLocuri) {
        if (nrLocuri > this.nrLocuriDisponibileMeci(meci))
            return null;
        Bilet bilet = new Bilet(meci, numeClient, nrLocuri);
        bilet = biletRepository.save(bilet);
        if (bilet.getId() == null)
            return null;
        return bilet;
    }

    public int nrLocuriDisponibileMeci(Meci meci) {
        return meci.getCapacitate() -  biletRepository.nrLocuriOcupateMeci(meci.getId());
    }

    public Iterable<Meci> getMeciuri() {
        return meciRepository.findAll();
    }

    public Iterable<Meci> getMeciuriLibere() {
        return meciRepository.findMeciuriDisponibile();
    }
}
