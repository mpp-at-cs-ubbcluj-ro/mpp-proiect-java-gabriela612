package org.example.observer;

import org.example.domain.Meci;
import org.example.domain.MeciL;

public interface IObserver {
    void schimbareMeciuri(Iterable<MeciL> meciuri);
}
