package net.mofusya.ornatelib.lang;

public enum SeptiLongValue {
    ZERO(new SeptiLong()),
    ONE(new SeptiLong(1L)),
    TEN(new SeptiLong(10L)),
    HUNDRED(new SeptiLong(100L)),
    THOUSAND(new SeptiLong(1000L)),
    MILLION(new SeptiLong(1000000L)),
    BILLION(new SeptiLong(1000000000L)),
    TRILLION(new SeptiLong(1000000000000L)),
    QUADRILLION(new SeptiLong(1L, 0)),
    QUINTILLION(new SeptiLong(1000L, 0)),
    SEXTILLION(new SeptiLong(1000000L, 0)),
    SEPTILLION(new SeptiLong(1000000000L, 0)),
    OCTILLION(new SeptiLong(1000000000000L, 0)),
    NONILLION(new SeptiLong(1L, 0, 0)),
    DECILLION(new SeptiLong(1000L, 0, 0)),
    UNDECILLION(new SeptiLong(1000000L, 0, 0)),
    DUODECILLION(new SeptiLong(1000000000L, 0, 0)),
    TREDECILLION(new SeptiLong(1000000000000L, 0, 0)),
    QUATTUORDECILLION(new SeptiLong(1L, 0, 0, 0)),
    QUINDECILLION(new SeptiLong(1000L, 0, 0, 0)),
    SEDECILLION(new SeptiLong(1000000L, 0, 0, 0)),
    SEPTENDECILLION(new SeptiLong(1000000000L, 0, 0, 0)),
    OCTODECILLION(new SeptiLong(1000000000000L, 0, 0, 0)),
    NOVENDECILLION(new SeptiLong(1L, 0, 0, 0, 0)),
    VIGINTILLION(new SeptiLong(1000L, 0, 0, 0, 0)),
    UNVIGINTILLION(new SeptiLong(1000000L, 0, 0, 0, 0)),
    DUOVIGINTILLION(new SeptiLong(1000000000L, 0, 0, 0, 0)),
    TRESVIGINTILLION(new SeptiLong(1000000000000L, 0, 0, 0, 0)),
    QUATTUORVIGINTILLION(new SeptiLong(1L, 0, 0, 0, 0, 0)),
    QUINVIGINTILLION(new SeptiLong(1000L, 0, 0, 0, 0, 0)),
    SESVIGINTILLION(new SeptiLong(1000000L, 0, 0, 0, 0, 0)),
    SEPTEMVIGINTILLION(new SeptiLong(1000000000L, 0, 0, 0, 0, 0)),
    OCTOVIGINTILLION(new SeptiLong(1000000000000L, 0, 0, 0, 0, 0)),
    NOVEMVIGINTILLION(new SeptiLong(1L, 0, 0, 0, 0, 0, 0)),
    TRIGINTILLION(new SeptiLong(1000L, 0, 0, 0, 0, 0, 0)),
    UNTRIGINTILLION(new SeptiLong(1000000L, 0, 0, 0, 0, 0, 0)),
    DUOTRIGINTILLION(new SeptiLong(1000000000L, 0, 0, 0, 0, 0, 0)),
    TRESTRIGINTILLION(new SeptiLong(1000000000000L, 0, 0, 0, 0, 0, 0)),
    QUATTUORTRIGINTILLION(new SeptiLong(1000000000000000L, 0, 0, 0, 0, 0, 0)),
    QUINTRIGINTILLION(new SeptiLong(1000000000000000000L, 0, 0, 0, 0, 0, 0));

    private final SeptiLong septiLong;

    SeptiLongValue(SeptiLong septiLong) {
        this.septiLong = septiLong;
    }

    public SeptiLong get() {
        return septiLong.copy();
    }
}
