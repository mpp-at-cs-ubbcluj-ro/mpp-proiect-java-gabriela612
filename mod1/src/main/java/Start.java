import domain.Angajat;
import domain.Bilet;
import domain.Meci;
import repository.*;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Properties;

public class Start {
    public static void main(String[] args) {
        System.out.println("Hello world!");

        Properties props=new Properties();
        try {
            props.load(new FileReader("bd.config"));
        } catch (IOException e) {
            System.out.println("Cannot find bd.config "+e);
        }
        

        IAngajatRepository angajatRepository = new AngajatDBRepository(props);
        Angajat angajat = angajatRepository.findByUsername("ionescu_ion");
        IMeciRepository meciRepository = new MeciDBRepository(props);
        Meci meci = meciRepository.findOne(1);
        meciRepository.findAll();
        IBiletRepository biletRepository = new BiletDBRepository(props);
        biletRepository.create(new Bilet(meci, "Mihai Eminescu", 2));
        biletRepository.size();
    }
}
