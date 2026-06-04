package net.mofusya.ornatelib.util;

import net.minecraft.nbt.CompoundTag;
import net.mofusya.ornatelib.lang.SeptiLong;
import net.mofusya.ornatelib.lang.SeptiLongValue;

public class SeptiLongHelper {

    public static CompoundTag serialiseNBT(String name, SeptiLong septiLong) {
        CompoundTag tag = new CompoundTag();
        tag.putLongArray(name, septiLong.getLayer());
        return tag;
    }

    public static SeptiLong deserializeNBT(String name, CompoundTag tag) {
        return SeptiLong.createFromList(tag.getLongArray(name));
    }

    private static final ArrayMap<SeptiLongValue, String> SUFFIX = suffix();

    public static String convertToStringAndAddSuffix(SeptiLong septiLong) {
        if (septiLong.isSmallerOrSameThan(0)) return septiLong.toString();
        for (int i = 0; i < SUFFIX.getKeys().size(); i++) {
            SeptiLongValue key = SUFFIX.getKeys().get(i);
            if (septiLong.copy().divide(10).divide(key.get()).isSmallerThan(10)) continue;
            if (i == 35) return septiLong.toString();

            key = SUFFIX.getKeys().get(i + 1);
            return septiLong.divide(key.get()) + SUFFIX.get(key);
        }
        return septiLong.toString();
    }

    private static ArrayMap<SeptiLongValue, String> suffix() {
        ArrayMap<SeptiLongValue, String> suffix = new ArrayMap<>();
        suffix.put(SeptiLongValue.QUINTRIGINTILLION, "QiTg");
        suffix.put(SeptiLongValue.QUATTUORTRIGINTILLION, "QaTg");
        suffix.put(SeptiLongValue.TRESTRIGINTILLION, "TTg");
        suffix.put(SeptiLongValue.DUOTRIGINTILLION, "DTg");
        suffix.put(SeptiLongValue.UNTRIGINTILLION, "UTg");
        suffix.put(SeptiLongValue.TRIGINTILLION, "Tg");
        suffix.put(SeptiLongValue.NOVEMVIGINTILLION, "NoVg");
        suffix.put(SeptiLongValue.OCTOVIGINTILLION, "OcVg");
        suffix.put(SeptiLongValue.SEPTEMVIGINTILLION, "SpVg");
        suffix.put(SeptiLongValue.SESVIGINTILLION, "SeVg");
        suffix.put(SeptiLongValue.QUINVIGINTILLION, "QiVg");
        suffix.put(SeptiLongValue.QUATTUORVIGINTILLION, "QaVg");
        suffix.put(SeptiLongValue.TRESVIGINTILLION, "TVg");
        suffix.put(SeptiLongValue.DUOVIGINTILLION, "DVg");
        suffix.put(SeptiLongValue.UNVIGINTILLION, "UVg");
        suffix.put(SeptiLongValue.VIGINTILLION, "Vg");
        suffix.put(SeptiLongValue.NOVENDECILLION, "NoDe");
        suffix.put(SeptiLongValue.OCTODECILLION, "OcDe");
        suffix.put(SeptiLongValue.SEPTENDECILLION, "SpDe");
        suffix.put(SeptiLongValue.SEDECILLION, "SeDe");
        suffix.put(SeptiLongValue.QUINDECILLION, "QiDe");
        suffix.put(SeptiLongValue.QUATTUORDECILLION, "QaDe");
        suffix.put(SeptiLongValue.TREDECILLION, "TDe");
        suffix.put(SeptiLongValue.DUODECILLION, "DDe");
        suffix.put(SeptiLongValue.UNDECILLION, "UDe");
        suffix.put(SeptiLongValue.DECILLION, "De");
        suffix.put(SeptiLongValue.NONILLION, "No");
        suffix.put(SeptiLongValue.OCTILLION, "Oc");
        suffix.put(SeptiLongValue.SEPTILLION, "Sp");
        suffix.put(SeptiLongValue.SEXTILLION, "Se");
        suffix.put(SeptiLongValue.QUINTILLION, "Qi");
        suffix.put(SeptiLongValue.QUADRILLION, "Qa");
        suffix.put(SeptiLongValue.TRILLION, "T");
        suffix.put(SeptiLongValue.BILLION, "B");
        suffix.put(SeptiLongValue.MILLION, "M");
        suffix.put(SeptiLongValue.THOUSAND, "k");
        return suffix;
    }
}
