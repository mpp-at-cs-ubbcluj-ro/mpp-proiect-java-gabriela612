package org.example.repository;

import org.example.domain.Angajat;
import org.example.domain.Meci;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class AngajatHibernateRepository implements IAngajatRepository {

    static SessionFactory sessionFactory;

    public AngajatHibernateRepository(SessionFactory sF) {
        sessionFactory = sF;
    }

    @Override
    public Angajat findByUsername(String username) {
        Session session = sessionFactory.openSession();

        try {
            session.beginTransaction();

            Angajat angajat = (Angajat) session.createQuery("FROM Angajat WHERE username = :username")
                    .setParameter("username", username)
                    .uniqueResult();

            session.getTransaction().commit();

            if (angajat != null) {
                System.out.println("Angajatul cu username-ul '" + username + "' a fost găsit.");
                System.out.println("ID: " + angajat.getId());
                System.out.println("Parola: " + angajat.getParola());
            } else {
                System.out.println("Nu s-a găsit niciun angajat cu username-ul '" + username + "'.");
            }
            return angajat;
        } catch (Exception e) {
            e.printStackTrace();
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return null;
    }

    @Override
    public Meci findOne(Integer integer) {
        return null;
    }

    @Override
    public Iterable<Meci> findAll() {
        return null;
    }

    @Override
    public Angajat save(Angajat entity) {
        return null;
    }

    @Override
    public Angajat delete(Integer integer) {
        return null;
    }

    @Override
    public Angajat update(Angajat entity) {
        return null;
    }

    @Override
    public int size() {
        return 0;
    }
}
