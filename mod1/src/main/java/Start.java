import domain.Angajat;
import domain.Bilet;
import domain.Meci;
import repository.*;

import java.io.FileReader;
import java.io.IOException;
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

//        IMeciRepository meciRepository = new MeciDBRepository(props);
//        Meci meci = meciRepository.findOne(1);
//        Bilet bilet = new Bilet(meci, "Ana Maria", 2);
//        IBiletRepository biletRepository = new BiletDBRepository(props);
//        biletRepository.create(bilet);
//        System.out.println(biletRepository.size());

    }
}
