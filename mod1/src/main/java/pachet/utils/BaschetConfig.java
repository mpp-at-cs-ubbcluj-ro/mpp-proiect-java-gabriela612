package pachet.utils;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import pachet.repository.*;
import pachet.service.Service;

public class BaschetConfig {
    @Bean(name="baschetService")
    public Service baschetService(){
        ApplicationContext context = new ClassPathXmlApplicationContext("BaschetConfiguration.xml");
        IAngajatRepository angajatRepository =
                context.getBean(AngajatDBRepository.class);
        IMeciRepository meciRepository = context.getBean(MeciDBRepository.class);
        IBiletRepository biletRepository = context.getBean(BiletDBRepository.class);
        return new Service(angajatRepository, meciRepository, biletRepository);
    }
}
