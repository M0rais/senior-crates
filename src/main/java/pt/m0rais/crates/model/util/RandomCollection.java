package pt.m0rais.crates.model.util;

import java.util.NavigableMap;
import java.util.Random;
import java.util.TreeMap;

/**
 * Class from https://stackoverflow.com/questions/6409652/random-weighted-selection-in-java/6409791#6409791
 */
public class RandomCollection<E> {
    private final NavigableMap<Double, E> map = new TreeMap<>();
    private final Random random;
    private double total = 0;

    public RandomCollection() {
        this.random = new Random();
    }

    public RandomCollection<E> add(double weight, E result) {
        if (weight <= 0) return this;
        total += weight;
        map.put(total, result);
        return this;
    }

    public E next() {
        double value = random.nextDouble() * total;
        return map.higherEntry(value).getValue();
    }

    public boolean isEmpty() {
        return map.isEmpty();
    }

}