package org.example;

import org.example.domain.Meci;
import org.example.repository.IMeciRepository;
import org.example.repository.MeciDBRepository;
import org.example.service.MeciService;
import org.example.utils.JdbcUtils;

import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class StartRest {

    static MeciControllerTest controller;

    public static void testGetAll() {

        try {
            System.out.println("toate meciurile");
            System.out.println(Arrays.toString(controller.getAll()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void testGetById() {

        try {
            System.out.println(controller.getById("1"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void testCreate() {

        try {
            System.out.println(controller.create(new Meci("Nume meci", 50.0, 100, LocalDate.of(2022, 10, 12))));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void testUpdate() {

        try {
            // Obținem ultimul meci salvat
            Meci[] meciuri = controller.getAll();
            Meci ultimulMeci = meciuri[meciuri.length - 1];

            // Facem update la ultimul meci
            ultimulMeci.setNume("Nume meci actualizat");
            ultimulMeci.setPretBilet(75.0);
            ultimulMeci.setCapacitate(200);
            ultimulMeci.setData(LocalDate.now()); // Actualizăm data la data curentă
            controller.update(ultimulMeci);

            testGetAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void testDelete() {

        try {
            // Obținem ultimul meci salvat
            Meci[] meciuri = controller.getAll();
            Meci ultimulMeci = meciuri[meciuri.length - 1];

            // Ștergem ultimul meci
            controller.delete(String.valueOf(ultimulMeci.getId()));

            testGetAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {

//        Properties props=new Properties();
//        try {
//            props.load(new FileReader("../bd.config"));
//        } catch (IOException e) {
//            System.out.println("Cannot find bd.config "+e);
//        }

        controller = new MeciControllerTest();

        // Testarea metodei getAll()
        testGetAll();

        // Testarea metodei getById()
//        testGetById();

        // Testarea metodei create()
//        testCreate();

        // Testarea metodei update()
//        testUpdate();

        // Testarea metodei delete()
//        testDelete();
    }

}
