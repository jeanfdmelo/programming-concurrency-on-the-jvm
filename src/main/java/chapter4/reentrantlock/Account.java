package chapter4.reentrantlock;


import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Account implements Comparable<Account> {
    private int balance;
    public final Lock monitor = new ReentrantLock();

    public Account(final int initialBalance) { balance = initialBalance; }

    public int compareTo(final Account other) {
        return Integer.compare(hashCode(), other.hashCode());
    }

    public void deposit(final int amount) {
        monitor.lock();
        try {
            if (amount > 0) balance += amount;
        } finally { //In case there was an Exception we're covered
            monitor.unlock();
        }
    }

    public boolean withdraw(final int amount) {
        try {
            monitor.lock();
            if(amount > 0 && balance >= amount)
            {
                balance -= amount;
                return true;
            }
            return false;
        } finally {
            monitor.unlock();
        }
    }
}