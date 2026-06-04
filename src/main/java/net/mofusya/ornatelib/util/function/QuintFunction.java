package net.mofusya.ornatelib.util.function;

@FunctionalInterface
public interface QuintFunction<PARAM1, PARAM2, PARAM3, PARAM4, PARAM5, VALUE> {
    VALUE apply(PARAM1 param1, PARAM2 param2, PARAM3 param3, PARAM4 param4, PARAM5 param5);
}
