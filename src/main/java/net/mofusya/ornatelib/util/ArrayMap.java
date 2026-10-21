package net.mofusya.ornatelib.util;

import org.jetbrains.annotations.NotNull;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;

public class ArrayMap<KEY, VALUE> implements Iterable<SingleMap<KEY, VALUE>> {
    private final ArrayList<KEY> keys;
    private final ArrayList<VALUE> values;

    public ArrayMap() {
        this.keys = new ArrayList<>();
        values = new ArrayList<>();
    }

    @SafeVarargs
    public ArrayMap(SingleMap<KEY, VALUE>... maps) {
        this();
        Arrays.stream(maps).forEach(map -> {
            this.put(map.index(), map.value());
        });
    }

    public ArrayMap(ArrayMap<KEY, VALUE> arrayMap) {
        this();
        this.putAll(arrayMap);
    }

    public void put(KEY key, VALUE value) {
        if (this.keys.contains(key)) {
            this.values.set(this.keys.indexOf(key), value);
        } else {
            this.keys.add(key);
            this.values.add(value);
        }
    }

    public void putAll(ArrayMap<KEY, VALUE> arrayMap) {
        arrayMap.forEach(this::put);
    }

    public void remove(int index) {
        this.keys.remove(index);
        this.values.remove(index);
    }

    public void removeAll(int... indexes) {
        for (int index : indexes) {
            this.remove(index);
        }
    }

    public void remove(KEY key) {
        int index = this.getKeyIndex(key);
        if (index < 0) throw new InvalidParameterException();

        this.remove(index);
    }

    public void removeAll(List<KEY> keys){
        for (KEY key : keys) {
            this.remove(key);
        }
    }

    public void clear() {
        this.keys.clear();
        this.values.clear();
    }

    public VALUE get(KEY key) {
        int index = this.keys.indexOf(key);
        if (index < 0) return null;
        return this.values.get(index);
    }

    public final List<KEY> getKeys(VALUE pValue) {
        ArrayList<KEY> toReturn = new ArrayList<>();
        for (KEY key : this.keys) {
            if (this.get(key).equals(pValue)) {
                toReturn.add(key);
            }
        }
        return toReturn;
    }

    public ArrayList<KEY> getKeys() {
        return new ArrayList<>(this.keys);
    }

    public ArrayList<VALUE> getValues() {
        return new ArrayList<>(this.values);
    }

    public int getKeyIndex(KEY key) {
        return this.getKeys().indexOf(key);
    }

    public boolean containsKey(KEY key) {
        return this.getKeys().contains(key);
    }

    public boolean containsValue(VALUE value) {
        return this.getValues().contains(value);
    }

    public int size() {
        return this.getKeys().size();
    }

    public boolean hasContent() {
        return this.size() > 0;
    }

    public boolean matches(ArrayMap<KEY, VALUE> arrayMap) {
        AtomicBoolean toReturn = new AtomicBoolean(true);

        this.forEach((key, value) -> {
            if (arrayMap.getKeys().contains(key)) {
                if (!arrayMap.get(key).equals(value)) toReturn.set(false);
            } else {
                toReturn.set(false);
            }
        });

        arrayMap.forEach((key, value) -> {
            if (this.getKeys().contains(key)) {
                if (!this.get(key).equals(value)) toReturn.set(false);
            } else {
                toReturn.set(false);
            }
        });

        return toReturn.get();
    }

    public void forEach(BiConsumer<KEY, VALUE> func) {
        for (KEY key : this.getKeys()) {
            VALUE value = this.get(key);
            func.accept(key, value);
        }
    }

    @NotNull
    @Override
    public Iterator<SingleMap<KEY, VALUE>> iterator() {
        ArrayList<SingleMap<KEY, VALUE>> singleMaps = new ArrayList<>();

        this.forEach((key, value) -> {
            singleMaps.add(new SingleMap<>(key, value));
        });

        return singleMaps.iterator();
    }
}
