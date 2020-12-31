package chapter4.reentrantlock;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class AccountService {
    public boolean transfer(final Account from, final Account to, final int amount) throws InterruptedException {
        final Account[] accounts = new Account[] {from, to};
        Arrays.sort(accounts);
        if(accounts[0].monitor.tryLock(1, TimeUnit.SECONDS)) {
            try {
                if (accounts[1].monitor.tryLock(1, TimeUnit.SECONDS)) {
                    try {
                        if(from.withdraw(amount)) {
                            to.deposit(amount);
                            return true;
                        } else {
                            return false;
                        }
                    } finally {
                        accounts[1].monitor.unlock();
                    }
                }
            } finally {
                accounts[0].monitor.unlock();
            }
        }
        throw new RuntimeException("Unable to acquire locks on the accounts");
    }
}