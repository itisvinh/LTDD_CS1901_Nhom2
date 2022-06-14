package com.example.infinitepocket;

import android.graphics.Color;

import com.example.infinitepocket.interfaces.Notifiable;
import com.example.infinitepocket.interfaces.Observable;
import com.example.infinitepocket.modelobjects.Transaction;
import com.example.infinitepocket.modelobjects.Wallet;

import java.util.ArrayList;
import java.util.List;


// Mediator class
// this class handles all the communication between every other classes
public final class Communicator{
    private static Communicator instance;
    private Communicator() {}

    public static Communicator getInstance() {
        if (instance == null)
            instance = new Communicator();
        return instance;
    }

    private Wallet currentWallet = null;
    private Transaction lastTransaction = null;
    private List<Observable<Wallet>> wallet_creation_observables = new ArrayList<>();
    private List<Observable<Wallet>> wallet_change_observables = new ArrayList<>();
    private List<Observable<Transaction>> transaction_creation_observables = new ArrayList<>();
    private CreateWalletMode createWalletMode = CreateWalletMode.MODE_CREATE;

    public CreateWalletMode getCreateWalletMode() {
        return createWalletMode;
    }

    public void setCreateWalletMode(CreateWalletMode createWalletMode) {
        if (createWalletMode == CreateWalletMode.MODE_EDIT && currentWallet == null)
            throw new IllegalArgumentException("Communicator.currentWallet is not editable: null?");
        this.createWalletMode = createWalletMode;
    }

    public Transaction getLastTransaction() {
        return lastTransaction;
    }

    public void setLastTransaction(Transaction lastTransaction) {
        if (this.lastTransaction != lastTransaction) {
            this.lastTransaction = lastTransaction;
            currentWallet.addTransaction(lastTransaction);
            fireAllTransactionCreationObservables();
        }
    }

    public void setCurrentWallet(Wallet wallet) {
        if (currentWallet != wallet) {
            currentWallet = wallet;
            fireAllWalletCreationObservables();
        }
    }

    public Wallet getCurrentWallet() {
        return currentWallet;
    }

    private void fireAllWalletCreationObservables() {
        for (Observable<Wallet> observable : wallet_creation_observables)
            observable.fire(currentWallet);
    }

    private void fireAllTransactionCreationObservables() {
        for (Observable<Transaction> observable : transaction_creation_observables)
            observable.fire(lastTransaction);
    }

    public void addOnCreatedTransactionObservables(Observable<Transaction> observable) {
        transaction_creation_observables.add(observable);
    }

    public void addOnSetCurrentWalletObserver(Observable<Wallet> observable) {
        wallet_creation_observables.add(observable);
    }

    public void addOnChangedCurrentWalletObserver(Observable<Wallet> observable) {
        wallet_change_observables.add(observable);
    }

    public void getNotifiedFromWalletChange(Wallet wallet) {
        for (Observable<Wallet> observables : wallet_change_observables)
            observables.fire(wallet);
    }
}
