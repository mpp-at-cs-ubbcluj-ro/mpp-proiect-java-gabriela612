package org.example.observer;

import org.example.domain.Meci;

public interface IObserver {
    void schimbareMeciuri(Iterable<Meci> meciuri);
}
