package net.mofusya.ornatelib.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;

public class ArrayMap<KEY, VALUE> {
    private final ArrayList<KEY> keys = new ArrayList<>();
    private final ArrayList<VALUE> values = new ArrayList<>();

    public ArrayMap() {
    }

    @SafeVarargs
    public ArrayMap(SingleMap<KEY, VALUE>... maps) {
        Arrays.stream(maps).forEach(map -> {
            this.put(map.index(), map.value());
        });
    }

    public ArrayMap(ArrayMap<KEY, VALUE> arrayMap) {
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

    public int size() {
        return this.getKeys().size();
    }

    public boolean hasContent() {
        return this.size() > 0;
    }

    public boolean matches(ArrayMap<KEY, VALUE> arrayMap){
        AtomicBoolean toReturn = new AtomicBoolean(true);

        this.forEach((key, value) -> {
            if (arrayMap.getKeys().contains(key)){
                if (!arrayMap.get(key).equals(value)) toReturn.set(false);
            } else {
                toReturn.set(false);
            }
        });

        arrayMap.forEach((key, value) -> {
            if (this.getKeys().contains(key)){
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
}
