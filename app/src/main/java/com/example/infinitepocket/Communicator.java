package com.example.infinitepocket;

import com.example.infinitepocket.interfaces.Observable;
import com.example.infinitepocket.modelobjects.Transaction;
import com.example.infinitepocket.modelobjects.Wallet;

import java.util.ArrayList;
import java.util.List;

public final class Communicator implements Observable{
    private static Communicator instance;
    private Communicator() {}

    public static Communicator getInstance() {
        if (instance == null)
            instance = new Communicator();
        return instance;
    }

    private Wallet currentWallet = null;
    private List<Observable<Wallet>> wallet_observables = new ArrayList<>();
    private List<Observable<Transaction>> transaction_observables = new ArrayList<>();
    private CreateWalletMode createWalletMode = CreateWalletMode.MODE_CREATE;


    public CreateWalletMode getCreateWalletMode() {
        return createWalletMode;
    }

    public void setCreateWalletMode(CreateWalletMode createWalletMode) {
        if (createWalletMode == CreateWalletMode.MODE_EDIT && currentWallet == null)
            throw new IllegalArgumentException("Communicator.currentWallet is not editable: null?");
        this.createWalletMode = createWalletMode;
    }

    public void setCurrentWallet(Wallet wallet) {
        if (currentWallet != wallet) {
            currentWallet = wallet;
            fireAll();
        }
    }

    public Wallet getCurrentWallet() {
        return currentWallet;
    }

    private void fireAll() {
        for (Observable<Wallet> observable : wallet_observables)
            observable.fire(currentWallet);
    }
    public void addOnSetCurrentWalletObserver(Observable<Wallet> observable) {
        wallet_observables.add(observable);
    }

    @Override
    public void fire(Object source) {

    }
}
