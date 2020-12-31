package chapter5.fixbadcode;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class EnergySourceGood {
    private final long MAXLEVEL = 100;
    private final AtomicLong level = new AtomicLong(MAXLEVEL);
    private static final ScheduledExecutorService replenishTimer = Executors.newScheduledThreadPool(10);
    private ScheduledFuture<?> replenishTask;

    private EnergySourceGood() {}

    private void init() {
        replenishTask = replenishTimer.scheduleAtFixedRate(new Runnable() {
            public void run() { replenish(); }
        }, 0, 1, TimeUnit.SECONDS);
    }

    public static EnergySourceGood create() {
        final EnergySourceGood energySource = new EnergySourceGood();
        energySource.init();
        return energySource;
    }

    public long getUnitsAvailable() { return level.get(); }

    public boolean useEnergy(final long units) {
        final long currentLevel = level.get();
        if (units > 0 && currentLevel >= units) {
            return level.compareAndSet(currentLevel, currentLevel - units);
        }
        return false;
    }

    public synchronized void stopEnergySource() {
        replenishTask.cancel(false);
    }

    private void replenish() {
        if (level.get() < MAXLEVEL) level.incrementAndGet();
    }
}