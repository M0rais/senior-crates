package pt.m0rais.crates.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
public class User {

    private final UUID uuid;
    private int crates = 0;
    private long day = 0, delay = 0;

    public void updateDelay(long time) {
        delay = System.currentTimeMillis() + time;
        crates++;
    }

    public boolean isDelay() {
        return delay > System.currentTimeMillis();
    }

    public boolean canOpen(int maxCrates) {
        if (day <= System.currentTimeMillis()) {
            day = System.currentTimeMillis() + TimeUnit.DAYS.toMillis(1);
            crates = 0;
            return true;
        }
        return crates < maxCrates;
    }

}