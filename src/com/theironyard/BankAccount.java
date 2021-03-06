package com.theironyard;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BankAccount {

    private double balance;
    private String accountNumber;

    private Lock lock;

    public BankAccount(String accountNumber, double initialBalance) {
        this.accountNumber = accountNumber;
        this.balance = initialBalance;
        this.lock = new ReentrantLock();
    }

    //one way to make thread safe

//    public synchronized void deposit(double amount) {
//        balance += amount;
//    }
//
//    public synchronized void withdraw(double amount) {
//        balance -= amount;
//    }

    //better way to make thread safe
    //you do not need to synchronize anything else
    //over-synchronization can have a noticeably negative impact on performance

    public boolean deposit(double amount) {

        boolean status = false;
        try {
            if(lock.tryLock(1000, TimeUnit.MILLISECONDS)) {
                try {
                    balance += amount;
                    status = true;
                } finally {
                    lock.unlock();
                }
            }else {
                System.out.println("Could not get the lock");
            }

        } catch(InterruptedException e) {

        }

        System.out.println("Transaction status = " + status);

        return status;
    }

    public void withdraw(double amount) {
        boolean status = false;
        try {
            if (lock.tryLock(1000, TimeUnit.MILLISECONDS)) {

                try {
                    balance -= amount;
                    status = true;
                } finally {
                    lock.unlock();
                }

            } else {
                System.out.println("Could not get the lock");
            }

        } catch(InterruptedException e) {

        }
        System.out.println("Transaction status = " + status);
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void printAccountNumber() {
        System.out.println("Account number = " + accountNumber);
    }

}
