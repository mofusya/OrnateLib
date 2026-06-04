package net.mofusya.ornatelib.util.function;

@FunctionalInterface
public interface TriFunction<PARAM1, PARAM2, PARAM3, VALUE> {
    VALUE apply(PARAM1 param1, PARAM2 param2, PARAM3 param3);
}
