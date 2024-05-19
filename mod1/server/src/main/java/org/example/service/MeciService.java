package org.example.service;

import org.example.domain.Meci;
import org.example.repository.IMeciRepository;
import org.example.repository.MeciDBRepository;
import org.example.repository.MeciDBRepository2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/meciuri")
//@CrossOrigin(origins = "http://localhost:8080")
@CrossOrigin
public class MeciService {

    @Autowired
    private MeciDBRepository2 meciRepository;

    @Autowired
    public void setMeciRepository(MeciDBRepository2 meciRepository) {
        this.meciRepository = meciRepository;
    }

    @RequestMapping("/greeting")
    public String greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        return String.format("Hello, %s!", name);
    }

    @RequestMapping(method = RequestMethod.GET)
    public Meci[] getAll() {
        System.out.println("Get all meciuri ...");
        List<Meci> meciuri = (List<Meci>) meciRepository.findAll();
        meciuri.forEach(System.out::println);
        Meci[] mecis = meciuri.toArray(new Meci[0]);
        return mecis;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getById(@PathVariable String id) {
        System.out.println("Get by id " + id);
        int idm = Integer.parseInt(id);
        Meci meci = meciRepository.findOne(idm);
        if (meci == null) {
            return new ResponseEntity<>("Meci not found", HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(meci, HttpStatus.OK);
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    public Meci create(@RequestBody Meci meci) {
        meciRepository.save(meci);
        return meci;
    }

    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<Meci> update(@RequestBody Meci meciDetails) {
        System.out.println("Updating meci ...");
        Integer id = meciDetails.getId();  // Obține ID-ul din corpul cererii
        Meci meci = meciRepository.findOne(id);
        if (meci == null) {
            return ResponseEntity.notFound().build();
        }

        meci.setNume(meciDetails.getNume());
        meci.setPretBilet(meciDetails.getPretBilet());
        meci.setCapacitate(meciDetails.getCapacitate());
        meci.setData(meciDetails.getData());

        Meci updatedMeci = meciRepository.update(meci);
        return ResponseEntity.ok(updatedMeci);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        System.out.println("Deleting meci ... " + id);
        Meci meci = meciRepository.findOne(id);
        if (meci == null) {
            return new ResponseEntity<>("Meci not found", HttpStatus.NOT_FOUND);
        }

        Meci deletedMeci = meciRepository.delete(id);
        return new ResponseEntity<>(deletedMeci, HttpStatus.NO_CONTENT);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleException(Exception e) {
        return e.getMessage();
    }
}