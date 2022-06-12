package com.example.infinitepocket;

import com.example.infinitepocket.modelobjects.Wallet;

import java.util.ArrayList;
import java.util.List;

public final class Communicator {
    private static Communicator instance;
    private Communicator() {}

    public static Communicator getInstance() {
        if (instance == null)
            instance = new Communicator();
        return instance;
    }
    private Wallet currentWallet = null;
    private List<Observable<Wallet>> observables = new ArrayList<>();

    public void setCurrentWallet(Wallet wallet) {
        if (currentWallet != wallet) {
            currentWallet = wallet;
            fireAll();
        }
    }

    private void fireAll() {
        for (Observable<Wallet> observable : observables)
            observable.fire(currentWallet);
    }
    public void addOnSetCurrentWalletObserver(Observable<Wallet> observable) {
        observables.add(observable);
    }

}
