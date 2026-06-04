package net.mofusya.ornatelib.util.function;

@FunctionalInterface
public interface TriConsumer<PARAM1, PARAM2, PARAM3> {
    void accept(PARAM1 param1, PARAM2 param2, PARAM3 param3);
}
