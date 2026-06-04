package net.mofusya.ornatelib.util;

public record SingleMap<KEY, VALUE>(KEY index, VALUE value) {
    public static <KEY, VALUE> SingleMap<KEY, VALUE> of(KEY index, VALUE value){
        return new SingleMap<>(index, value);
    }
}