package net.mofusya.ornatelib.util.function;

@FunctionalInterface
public interface QuadFunction<PARAM1, PARAM2, PARAM3, PARAM4, VALUE> {
    VALUE apply(PARAM1 param1, PARAM2 param2, PARAM3 param3, PARAM4 param4);
}
