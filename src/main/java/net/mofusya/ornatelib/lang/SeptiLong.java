package net.mofusya.ornatelib.lang;

import com.electronwill.nightconfig.core.conversion.InvalidValueException;
import org.jetbrains.annotations.NotNull;

public final class SeptiLong {

    private static final long V0_MAX_VALUE = Long.MAX_VALUE;
    private static final long V1_V6_MAX_VALUE = 999999999999999L;
    private static final long MIN_VALUE = 0L;

    public static final SeptiLong MAX_VALUE = new SeptiLong(V0_MAX_VALUE, V1_V6_MAX_VALUE, V1_V6_MAX_VALUE, V1_V6_MAX_VALUE, V1_V6_MAX_VALUE, V1_V6_MAX_VALUE, V1_V6_MAX_VALUE);
    public static final int LAYER_SIZE = 7;

    private final long[] value;

    public SeptiLong() {
        this(MIN_VALUE, MIN_VALUE, MIN_VALUE, MIN_VALUE, MIN_VALUE, MIN_VALUE, MIN_VALUE);
    }

    public SeptiLong(long value6) {
        this(MIN_VALUE, MIN_VALUE, MIN_VALUE, MIN_VALUE, MIN_VALUE, MIN_VALUE, value6);
    }

    public SeptiLong(long value5, long value6) {
        this(MIN_VALUE, MIN_VALUE, MIN_VALUE, MIN_VALUE, MIN_VALUE, value5, value6);
    }

    public SeptiLong(long value4, long value5, long value6) {
        this(MIN_VALUE, MIN_VALUE, MIN_VALUE, MIN_VALUE, value4, value5, value6);
    }

    public SeptiLong(long value3, long value4, long value5, long value6) {
        this(MIN_VALUE, MIN_VALUE, MIN_VALUE, value3, value4, value5, value6);
    }

    public SeptiLong(long value2, long value3, long value4, long value5, long value6) {
        this(MIN_VALUE, MIN_VALUE, value2, value3, value4, value5, value6);
    }

    public SeptiLong(long value1, long value2, long value3, long value4, long value5, long value6) {
        this(MIN_VALUE, value1, value2, value3, value4, value5, value6);
    }

    //1,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000
    //value0                    value1              value2              value3              value4              value5              value6
    public SeptiLong(long value0, long value1, long value2, long value3, long value4, long value5, long value6) {
        if (value0 < 0) {
            throw new InvalidValueException("value" + 0 + " has to be in between " + MIN_VALUE + " and " + V0_MAX_VALUE + ". That value is invalid.");
        }
        if (value1 > V1_V6_MAX_VALUE || value1 < 0) {
            throw new InvalidValueException("value" + 1 + " has to be in between " + MIN_VALUE + " and " + V1_V6_MAX_VALUE + ". That value is invalid.");
        }
        if (value2 > V1_V6_MAX_VALUE || value2 < 0) {
            throw new InvalidValueException("value" + 2 + " has to be in between " + MIN_VALUE + " and " + V1_V6_MAX_VALUE + ". That value is invalid.");
        }
        if (value3 > V1_V6_MAX_VALUE || value3 < 0) {
            throw new InvalidValueException("value" + 3 + " has to be in between " + MIN_VALUE + " and " + V1_V6_MAX_VALUE + ". That value is invalid.");
        }
        if (value4 > V1_V6_MAX_VALUE || value4 < 0) {
            throw new InvalidValueException("value" + 4 + " has to be in between " + MIN_VALUE + " and " + V1_V6_MAX_VALUE + ". That value is invalid.");
        }
        if (value5 > V1_V6_MAX_VALUE || value5 < 0) {
            throw new InvalidValueException("value" + 5 + " has to be in between " + MIN_VALUE + " and " + V1_V6_MAX_VALUE + ". That value is invalid.");
        }
        if (value6 > V1_V6_MAX_VALUE || value6 < 0) {
            throw new InvalidValueException("value" + 6 + " has to be in between " + MIN_VALUE + " and " + V1_V6_MAX_VALUE + ". That value is invalid.");
        }

        this.value = new long[]{value0, value1, value2, value3, value4, value5, value6};
    }

    public long[] getLayer(){
        long[] layers = new long[this.value.length];

        for (int i = 0; i < this.value.length; i++) {
            layers[i] = this.getLayer(i);
        }

        return layers;
    }

    public void set(@NotNull SeptiLong septiLong) {
        System.arraycopy(septiLong.value, 0, this.value, 0, value.length);
    }

    public void set() {
        this.set(new SeptiLong());
    }

    public SeptiLong add(@NotNull SeptiLong septiLong) {
        for (int i = 0; i < this.value.length; i++) {
            this.addLayer(i, septiLong.value[i]);
        }
        return this;
    }

    public SeptiLong add(long l) {
        return this.add(new SeptiLong(l));
    }

    public SeptiLong add() {
        return this.add(new SeptiLong(1));
    }

    public SeptiLong remove(@NotNull SeptiLong septiLong) {
        for (int i = 0; i < this.value.length; i++) {
            this.removeLayer(i, septiLong.value[i]);
        }
        return this;
    }

    public SeptiLong remove(long l) {
        return this.remove(new SeptiLong(l));
    }

    public SeptiLong remove() {
        return this.remove(new SeptiLong(1));
    }

    public SeptiLong divide(@NotNull SeptiLong septiLong) {
        for (int i = this.value.length - 1; i >= 0; i--) {
            this.divideLayer(i, septiLong);
        }
        return this;
    }

    public SeptiLong divide(long l) {
        return this.divide(new SeptiLong(l));
    }

    public float divideAndGetFloat(@NotNull SeptiLong septiLong) {
        for (int i = 0; i < this.value.length; i++) {
            if (septiLong.value[i] != 0) {
                return this.divideLayerAndGetFloat(i, septiLong);
            }
        }

        throw new InvalidValueException("Cannot divide with a zero.");
    }

    public float divideAndGetFloat(long l) {
        return this.divideAndGetFloat(new SeptiLong(l));
    }

    public SeptiLong multiply(float multiplier){
        if (multiplier <= 1f || multiplier > V0_MAX_VALUE) return this;

        for (int i = 0; i < this.value.length; i++) {
            if (this.value[i] > 0){
                this.multiplyLayer(i, multiplier);
            }
        }
        return this;
    }

    public void setLayer(int layer, long set) {
        if (layer > this.value.length - 1 || layer < 0) {
            throw new InvalidValueException("layer has to be in between " + 0 + " and " + (this.value.length - 1) + ". That value is invalid.");
        }

        if (layer != 0 && (set > V1_V6_MAX_VALUE || set < 0)) {
            throw new InvalidValueException("value" + layer + " has to be in between " + MIN_VALUE + " and " + V1_V6_MAX_VALUE + ". That value is invalid.");
        } else {
            if (set < 0) {
                throw new InvalidValueException("value" + layer + " has to be in between " + MIN_VALUE + " and " + V0_MAX_VALUE + ". That value is invalid.");
            }
        }

        this.value[layer] = set;
    }

    public long getLayer(int layer) {
        if (layer > this.value.length - 1 || layer < 0) {
            throw new InvalidValueException("layer has to be in between " + 0 + " and " + (this.value.length - 1) + ". That value is invalid.");
        }
        return this.value[layer];
    }

    public boolean addLayer(int layer, long add) {
        if (layer > this.value.length - 1 || layer < 0) {
            throw new InvalidValueException("layer has to be in between " + 0 + " and " + (this.value.length - 1) + ". That value is invalid.");
        }
        if (layer != 0 && (add > V1_V6_MAX_VALUE || add < 0)) {
            throw new InvalidValueException("value" + layer + " has to be in between " + MIN_VALUE + " and " + V1_V6_MAX_VALUE + ". That value is invalid.");
        } else {
            if (add < 0) {
                throw new InvalidValueException("value" + layer + " has to be in between " + MIN_VALUE + " and " + V0_MAX_VALUE + ". That value is invalid.");
            }
        }

        if (layer == 0) {
            try {
                this.value[layer] += add;
            } catch (Exception e) {
                return false;
            }
        } else {
            long added = this.value[layer] + add;
            if (added > V1_V6_MAX_VALUE) {
                added -= V1_V6_MAX_VALUE + 1;

                if (this.addLayer(layer - 1)) return false;
            }
            this.value[layer] = added;
        }
        return true;
    }

    public boolean addLayer(int layer) {
        return this.addLayer(layer, 1);
    }

    public boolean removeLayer(int layer, long remove) {
        if (layer > this.value.length - 1 || layer < 0) {
            throw new InvalidValueException("layer has to be in between " + 0 + " and " + (this.value.length - 1) + ". That value is invalid.");
        }
        if (layer != 0) {
            if (remove > V1_V6_MAX_VALUE || remove < 0) {
                throw new InvalidValueException("value" + layer + " has to be in between " + MIN_VALUE + " and " + V1_V6_MAX_VALUE + ". That value is invalid.");
            }
        } else {
            if (remove < 0) {
                throw new InvalidValueException("value" + layer + " has to be in between " + MIN_VALUE + " and " + V0_MAX_VALUE + ". That value is invalid.");
            }
        }

        long removed = this.value[layer] - remove;

        if (layer == 0) {
            if (removed < 0) {
                this.value[layer] = 0;
                return false;
            }
        } else {
            if (removed < 0) {
                if (!this.removeLayer(layer - 1)) {
                    this.value[layer] = 0;
                    return false;
                }
                removed += (V1_V6_MAX_VALUE + 1) / 10;
            }
        }

        this.value[layer] = removed;
        return true;
    }

    public boolean removeLayer(int layer) {
        return this.removeLayer(layer, 1);
    }

    public void divideLayer(int layer, SeptiLong divide) {

        if (divide.is(SeptiLongValue.ZERO.get())) {
            throw new InvalidValueException("Cannot divide with a zero.");
        }

        if (layer > this.value.length - 1 || layer < 0) {
            throw new InvalidValueException("layer has to be in between " + 0 + " and " + (this.value.length - 1) + ". That value is invalid.");
        }

        if (this.value[layer] == 0) return;

        if (layer == this.value.length - 1) {
            this.value[layer] = (long) this.divideAndGetFloat(divide);
        } else {
            float divided = (long) this.divideAndGetFloat(divide);
            if (divided >= 1) {
                this.value[layer] = (long) divided;
            } else {
                long modDivided = (long) (divided * ((float) (V1_V6_MAX_VALUE + 1) / 10));
                this.addLayer(layer + 1, modDivided);
            }
        }
    }

    public void divideLayer(int layer, long divide) {
        this.divideLayer(layer, new SeptiLong(divide));
    }

    public float divideLayerAndGetFloat(int layer, SeptiLong divide) {
        if (divide.is(SeptiLongValue.ZERO.get())) {
            throw new InvalidValueException("Cannot divide with a zero.");
        }

        if (layer > this.value.length - 1 || layer < 0) {
            throw new InvalidValueException("layer has to be in between " + 0 + " and " + (this.value.length - 1) + ". That value is invalid.");
        }

        float value = this.value[layer];

        for (long l : divide.value) {
            if (l == 0) continue;

            value /= l;
        }

        return value;
    }

    public float divideLayerAndGetFloat(int layer, long divide) {
        return this.divideLayerAndGetFloat(layer, new SeptiLong(divide));
    }

    public void multiplyLayer(int layer, float multiplier){
        if (multiplier <= 1f || multiplier > V0_MAX_VALUE) return;

        if (layer > this.value.length - 1 || layer < 0) {
            throw new InvalidValueException("layer has to be in between " + 0 + " and " + (this.value.length - 1) + ". That value is invalid.");
        }

        float value = this.value[layer] * multiplier;

        if (layer == 0){
            if (value > V0_MAX_VALUE) value = V0_MAX_VALUE;
        } else {
            if (value > V1_V6_MAX_VALUE){
                long upperValue = (long) (value / (V1_V6_MAX_VALUE + 1f));
                this.addLayer(layer - 1, upperValue);
                value -= upperValue * (V1_V6_MAX_VALUE + 1f);
            }
        }
        this.value[layer] = (long) value;
    }

    public boolean is(@NotNull SeptiLong septiLong) {
        boolean match = true;
        for (int i = 0; i < value.length; i++) {
            if (this.value[i] != septiLong.value[i]) {
                match = false;
                break;
            }
        }

        return match;
    }

    public boolean isGreaterThan(@NotNull SeptiLong septiLong) {
        for (int i = 0; i < this.value.length; i++) {
            if (this.value[i] == 0 && septiLong.value[i] == 0) {
                continue;
            }
            return this.value[i] > septiLong.value[i];
        }
        return false;
    }

    public boolean isGreaterThan(long l) {
        return this.isGreaterThan(new SeptiLong(l));
    }

    public boolean isSmallerThan(@NotNull SeptiLong septiLong) {
        for (int i = 0; i < this.value.length; i++) {
            if (this.value[i] == 0 && septiLong.value[i] == 0) {
                continue;
            }
            return this.value[i] < septiLong.value[i];
        }
        return false;
    }

    public boolean isSmallerThan(long l) {
        return this.isSmallerThan(new SeptiLong(l));
    }

    public boolean isGreaterOrSameThan(@NotNull SeptiLong septiLong) {
        return this.is(septiLong) || this.isGreaterThan(septiLong);
    }

    public boolean isGreaterOrSameThan(long l) {
        return this.isGreaterOrSameThan(new SeptiLong(l));
    }

    public boolean isSmallerOrSameThan(@NotNull SeptiLong septiLong) {
        return this.is(septiLong) || this.isSmallerThan(septiLong);
    }

    public boolean isSmallerOrSameThan(long l) {
        return this.isSmallerOrSameThan(new SeptiLong(l));
    }

    public SeptiLong copy() {
        return new SeptiLong(this.value[0], this.value[1], this.value[2], this.value[3], this.value[4], this.value[5], this.value[6]);
    }

    @Override
    public String toString() {
        StringBuilder toReturn = new StringBuilder();
        String[] layers = {"", "", "", "", "", "", ""};

        if (this.value[0] != 0) {
            layers[0] = toString(this.value[0]);
        }

        for (int i = 1; i < this.value.length; i++) {
            if (this.value[i] != 0) {
                if (layers[i - 1].isEmpty()) {
                    layers[i] = toString(this.value[i]);
                } else {
                    layers[i] = addExtraZero(toString(this.value[i]));
                }
            } else {
                if (!layers[i - 1].isEmpty()) {
                    layers[i] = addExtraZero("0");
                }
            }
        }

        for (String layer : layers) {
            toReturn.append(layer);
        }

        String strToReturn = toReturn.toString();

        if (strToReturn.isEmpty()) {
            strToReturn = "0";
        }

        return addComma(strToReturn);
    }

    public Integer toInteger() {
        if (this.isGreaterThan(new SeptiLong(Integer.MAX_VALUE))) return Integer.MAX_VALUE;
        if (this.value[this.value.length -1] > Integer.MAX_VALUE) return Integer.MAX_VALUE;

        return (int) this.value[4];
    }

    public static SeptiLong createFromList(long[] layers) {
        SeptiLong toReturn = new SeptiLong();

        if (layers == null || layers.length > LAYER_SIZE) return toReturn;

        for (int i = 0; i < LAYER_SIZE; i++) {
            toReturn.setLayer(i, layers[i]);
        }
        return toReturn;
    }

    private static String toString(Long l) {
        return l.toString();
    }

    private static String addExtraZero(String string) {
        if (string.isEmpty()) string = "0";

        StringBuilder toReturn = new StringBuilder();

        final int maxCharacter = 15;
        char[] characters = string.toCharArray();

        toReturn.append("0".repeat(Math.max(0, maxCharacter - characters.length)));
        toReturn.append(characters);

        return toReturn.toString();
    }

    public static String addComma(String string) {

        if (string.length() < 4) return string;

        StringBuilder toReturn = new StringBuilder();

        char[] characters = string.toCharArray();
        int types = string.length() % 3;
        int offset = switch (types) {
            case 0 -> 1;
            case 1 -> 3;
            case 2 -> 2;
            default -> throw new IllegalStateException("Unexpected value: " + types);
        };

        for (int i = 0; i < characters.length; i++) {
            toReturn.append(characters[i]);
            if ((i + offset) % 3 == 0 && i != characters.length - 1) {
                toReturn.append(",");
            }
        }

        return toReturn.toString();
    }
}