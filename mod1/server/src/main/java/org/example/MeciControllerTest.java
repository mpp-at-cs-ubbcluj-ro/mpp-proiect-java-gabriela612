package org.example;

import org.example.domain.Meci;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.Callable;

import static org.junit.jupiter.api.Assertions.*;

public class MeciControllerTest {

    public static final String URL = "http://localhost:8080/api/meciuri";

    private final RestTemplate restTemplate = new RestTemplate();

    private <T> T execute(Callable<T> callable) throws Exception {
        try {
            return callable.call();
        } catch (ResourceAccessException | HttpClientErrorException e) { // server down, resource exception
            throw new Exception(e);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public Meci[] getAll() throws Exception {
        return execute(() -> restTemplate.getForObject(URL, Meci[].class));
    }

    public Meci getById(String id) throws Exception {
        return execute(() -> restTemplate.getForObject(String.format("%s/%s", URL, id), Meci.class));
    }

    public Meci create(Meci meci) throws Exception {
        return execute(() -> restTemplate.postForObject(URL, meci, Meci.class));
    }

    public void update(Meci meci) throws Exception {
        execute(() -> {
            restTemplate.put(URL, meci);
            return null;
        });
    }

    public void delete(String id) throws Exception {
        execute(() -> {
            restTemplate.delete(String.format("%s/%s", URL, id));
            return null;
        });
    }
}