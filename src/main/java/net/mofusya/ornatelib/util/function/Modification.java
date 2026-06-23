package net.mofusya.ornatelib.util.function;


@FunctionalInterface
public interface Modification<VALUE> {
    VALUE apply(VALUE value);
}
