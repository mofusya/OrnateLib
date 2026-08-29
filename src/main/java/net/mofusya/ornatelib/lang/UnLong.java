package net.mofusya.ornatelib.lang;

import com.mojang.logging.LogUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;

public final class UnLong extends Number implements Comparable<UnLong>, Iterable<Long> {
    private static final Logger LOGGER = LogUtils.getLogger();

    //The max layer size of the UnLong/
    private static final int LAYER_SIZE = 14;
    /* Memo
     * L: 9223372036854775807
     * I: 2147483647
     *
     * L: 1000000000000000000
     * I: 1000000000
     */
    private static final long LAYER_MAX_VALUE = 999_999_999;
    private static final long LAYER_MAX_VALUE_PLUS_ONE = LAYER_MAX_VALUE + 1;
    private static final int LAYER_MAX_CHARACTER = 9;

    private static final long MILLION = 1_000_000;
    private static final long THOUSAND = 1_000;

    //The main values list/
    private final List<Long> values;
    //The max layer size of this UnLong/
    private final int layerSize;

    private UnLong() {
        //Set this layerSize to the max layer size of the UnLong.
        this.layerSize = LAYER_SIZE;
        //Create the ArrayList for values with the max size of layerSize.
        this.values = new ArrayList<>(this.layerSize);
        //Initialise values to 0 for all index.
        for (int i = 0; i < this.layerSize; i++) {
            this.values.add(0L);
        }
    }

    public UnLong(@NotNull Number... values) {
        this(Arrays.stream(values).map(Number::longValue).toList());
    }

    public UnLong(@NotNull Long... values) {
        this(Arrays.asList(values));
    }

    public UnLong(@NotNull List<Long> values) {
        //Initialize all.
        this();

        //Copy the values for inverting.
        List<Long> invertedValues = new ArrayList<>(values);
        //Reverse the invertedValues.
        Collections.reverse(invertedValues);
        //Add missing layers to the invertedValues.
        int size = invertedValues.size();
        while (size < this.getLayerSize()) {
            invertedValues.add(0L);
            size = invertedValues.size();
        }

        //Import this values from invertedValues.
        for (int i = 0; i < this.getLayerSize(); i++) {
            this.setValue(i, invertedValues.get(i));
        }
    }


    public UnLong(@NotNull UnLong multiplier, @NotNull Number... values) {
        this(multiplier, Arrays.stream(values).map(Number::longValue).toList());
    }

    public UnLong(@NotNull UnLong multiplier, @NotNull Long... values) {
        this(multiplier, Arrays.asList(values));
    }

    public UnLong(@NotNull UnLong multiplier, @NotNull List<Long> values) {
        this(values);
        //Multiply this with the multiplier.
        this.multi(multiplier);
    }

    public void setTo(@NotNull Long l) {
        this.setTo(new UnLong(l));
    }


    public void setTo(@NotNull UnLong unLong) {
        //Replace this values to unLong values.
        this.forEachI((value, index) -> {
            this.setValue(index, unLong.getValue(index));
        });
    }

    //Returns the carry
    public long addToIndex(int index, long value) {
        //Check if value is valid. If so, cancel if it's zero.
        value = checkValue(value);
        if (value <= 0) return 0;

        //Set the added number.
        long added = this.getValue(index) + value;
        //Check if added has an carry (If added is over the layers max value).
        if (added > LAYER_MAX_VALUE) {
            //If the index of the value is that last index, overflow (Set the value to the max layer value).
            if (this.isLastIndex(index)) {
                this.setValue(index, LAYER_MAX_VALUE);
                //Set the carry to zero (Return zero).
                return 0;
            }

            //Calculate the carry.
            long carry = added / LAYER_MAX_VALUE_PLUS_ONE;
            //Remove the carries from added.
            added %= LAYER_MAX_VALUE_PLUS_ONE;
            //Set self value to added.
            this.setValue(index, added);
            //Return the carry.
            return carry;
        }

        //Set self value to added.
        this.setValue(index, added);
        //Set the carry to zero (Return zero).
        return 0;
    }

    //Return the added this;
    public UnLong add(@NotNull Number add) {
        return this.add(new UnLong(add.longValue()));
    }

    //Return the added this;
    public UnLong add(@NotNull UnLong add) {
        //Cancel if add is zero/
        if (add.isZero()) return this;

        //Initialize the carry/
        UnLong carry = new UnLong();
        //Add each add value to this values/
        for (int index = 0; index < this.getLayerSize(); index++) {
            //Add the add value to this value of index and get the carry.
            long carryValue = this.addToIndex(index, add.getValue(index));
            //If index is the last index this UnLong, continue and go to the next index.
            if (this.isLastIndex(index)) continue;

            //If the carryValue is greater or same than one, add carryValue to carry with the next index.
            if (carryValue >= 1) carry.setValue(index + 1, carryValue);
        }

        //If carry is not zero, add the carry to this.
        if (!carry.isZero()) {
            return this.add(carry);
        }

        //Return this;
        return this;
    }

    //Return multiple added this.
    public UnLong add(@NotNull Number... adds) {
        return this.add(Arrays.stream(adds).map(Number::longValue).map(UnLong::new).toArray(UnLong[]::new));
    }

    //Return multiple added this.
    public UnLong add(@NotNull UnLong... adds) {
        //Add from each adds.
        for (UnLong add : adds) {
            //Add.
            this.add(add);
        }
        //Return this.
        return this;
    }

    //Return if succeeded.
    public boolean minFromIndex(int index, long value) {
        return this.minFromIndex(index, value, false);
    }

    //Return if succeeded.
    public boolean minFromIndex(int index, long value, boolean simulate) {
        //Check if value is valid. If so, cancel if it's zero.
        value = checkValue(value);
        if (value <= 0) return true;
        //Set mined number.
        long mined = this.getValue(index) - value;
        //If mined is a minus value, borrow one from the next index
        if (mined < 0) {
            //If index is the last index of this UnLong, the method will return "failed". If not, borrow one from the next index.
            if (this.isLastIndex(index)) {
                //Return "failed".
                return false;
            }
            //Calculate the borrow.
            long borrow = mined / -LAYER_MAX_VALUE_PLUS_ONE + (mined % -LAYER_MAX_VALUE_PLUS_ONE == 0 ? 0 : 1);
            //Borrow (minus) from next index. If failed, deny borrowing and return "failed".
            if (this.minFromIndex(index + 1, borrow)) {
                //Add the borrowed amount to mined.
                mined += (borrow * LAYER_MAX_VALUE_PLUS_ONE);
            } else {
                //Return "failed".
                return false;
            }
        }
        //Set this value to mined if not simulate.
        if (!simulate) this.setValue(index, mined);
        //Return "success".
        return true;
    }

    //Returns min this;
    public UnLong min(@NotNull Number min) {
        return this.min(new UnLong(min.longValue()));
    }

    //Returns min this;
    public UnLong min(@NotNull UnLong min) {
        //Cancel if add is zero
        if (min.isZero()) return this;
        //Set this to zero and cancel if min is smaller or same as this.
        if (this.isSmallerOrSameAs(min)) {
            this.setTo(UnLong.zero());
            return this;
        }
        //Min each values from this values.
        this.forEachI((value, index) -> {
            this.minFromIndex(index, min.getValue(index));
        });
        //Return this.
        return this;
    }

    //Returns multiple mined this.
    public UnLong min(@NotNull Number... mins) {
        return this.min(Arrays.stream(mins).map(Number::longValue).map(UnLong::new).toArray(UnLong[]::new));
    }

    //Returns multiple mined this.
    public UnLong min(@NotNull UnLong... mins) {
        //Min from each mins.
        for (UnLong min : mins) {
            //Min.
            this.min(min);
        }
        //Return this.
        return this;
    }

    //Returns the carry
    private static long multIndex(int index, long value, long multiplier, @NotNull UnLong layerAnswer) {
        //Check if the multiplier is valid. If not make it so.
        multiplier = checkValue(multiplier);
        //If the multiplier is 1 (multiplying by 1), return.
        if (multiplier == 1) {
            //Set layerAnswer's value of index to value.
            layerAnswer.setValue(index, value);
            //Return zero carries.
            return 0;
        }

        //Set multiplied value.
        long multi = value * multiplier;
        //Check if multi has a carry.
        if (multi > LAYER_MAX_VALUE) {
            //If index is the last layer of layerAnswer, set this layer to the max value and return zero carries.
            if (layerAnswer.isLastIndex(index)) {
                //Set this value to the max value.
                layerAnswer.setValue(index, LAYER_MAX_VALUE);
                //Return zero carries.
                return 0;
            }

            //Get the carry amount.
            long carry = multi / LAYER_MAX_VALUE_PLUS_ONE;
            //Subtract the carry from multi.
            multi %= LAYER_MAX_VALUE_PLUS_ONE;
            //Set layerAnswer's value to multi.
            layerAnswer.setValue(index, multi);
            //Return the carry.
            return carry;
        }

        //Set this value to multi.
        layerAnswer.setValue(index, multi);
        //Return zero carries.
        return 0;
    }

    //Returns the carry
    private static float multIndex(int index, long value, float multiplier, @NotNull UnLong layerAnswer) {
        //Check if the multiplier is valid. If not make it so.
        multiplier = checkValue(multiplier);
        //If the multiplier is 1 (multiplying by 1), return.
        if (multiplier == 1f) {
            //Set layerAnswer's value of index to value.
            layerAnswer.setValue(index, value);
            //Return zero carries.
            return 0f;
        }

        //Set multiplied value.
        float multi = value * multiplier;
        //Check if multi has a carry.
        if (multi > LAYER_MAX_VALUE) {
            //If index is the last layer of layerAnswer, set this layer to the max value and return zero carries.
            if (layerAnswer.isLastIndex(index)) {
                //Set this value to the max value.
                layerAnswer.setValue(index, LAYER_MAX_VALUE);
                //Return zero carries.
                return 0f;
            }

            //Get the carry amount.
            float carry = multi / LAYER_MAX_VALUE_PLUS_ONE;
            //Subtract the carry from multi.
            multi %= LAYER_MAX_VALUE_PLUS_ONE;
            //Set layerAnswer's value to multi.
            layerAnswer.setValue(index, (long) multi);
            //Return the carry.
            return carry;
        }

        //Set this value to multi.
        layerAnswer.setValue(index, (long) multi);
        //Return zero carries.
        return 0f;
    }

    //Returns the multiplied this.
    public UnLong multi(@NotNull Number multi) {
        return this.multi(new UnLong(multi.longValue()));
    }

    //Returns the multiplied this.
    public UnLong multi(@NotNull UnLong multi) {
        //Cancel if multi is one (multiplying by one).
        if (multi.isSameAs(1)) return this;

        //If this is zero, return.
        if (this.isZero()) return this;

        //If multi is zero, set this to zero and return this.
        if (multi.isZero()) {
            //Set this to zero.
            this.setTo(UnLong.zero());
            //Return this.
            return this;
        }

        //Set the layerAnswers.
        ArrayList<UnLong> layerAnswers = new ArrayList<>(this.getLayerSize());
        //For all the multi's values.
        multi.forEachI((pValue, pIndex) -> {
            //Sets the layers answer.
            UnLong layerAnswer = new UnLong();
            //Sets the carry.
            UnLong carry = new UnLong();
            //Multiply this values my the multi's value.
            this.forEachI((value, index) -> {
                //Multiply and get the carry.
                long carryValue = multIndex(index, value, pValue, layerAnswer);
                //If the carryValue is zero or invalid, continue.
                if (carryValue <= 0) return false;
                //Add the carry value to the next index of carry.
                carry.setValue(index + 1, carryValue);
                //Return "do not break".
                return false;
            });
            //Add layerAnswer the carry.
            layerAnswer.add(carry);
            //Shift the layerAnswer pIndex times.
            layerAnswer.shift(pIndex);
            //Add the layerAnswer to the layerAnswers.
            layerAnswers.add(layerAnswer);
        });

        //Set this to zero.
        this.setTo(UnLong.zero());
        //Adds all the layersAnswers to this.
        this.add(layerAnswers.toArray(new UnLong[this.getLayerSize()]));
        //Return this.
        return this;
    }

    //Returns multiple multiplied this.
    public UnLong multi(@NotNull Number... multi) {
        return this.multi(Arrays.stream(multi).map(Number::longValue).map(UnLong::new).toArray(UnLong[]::new));
    }

    //Returns multiple multiplied this.
    public UnLong multi(@NotNull UnLong... multi) {
        //Multi from each multi.
        for (UnLong mult : multi) {
            //Multi.
            this.multi(mult);
        }
        //Return this.
        return this;
    }

    //Returns slided this.
    public UnLong shift(int count) {
        //If count is zero, return.
        if (count == 0) return this;
        //Set the slided.
        UnLong slided = new UnLong();
        //Slide for each values.
        this.forEachI((value, index) -> {
            //If index will go over the max layer size, break.
            int shiftIndex = index + count;
            if (shiftIndex >= this.getLayerSize() || -shiftIndex >= this.getLayerSize()) return true;

            //Set the slided value of shiftIndex to value.
            slided.setValue(shiftIndex, value);
            //Return "do not break".
            return false;
        });
        //Set this to slided.
        this.setTo(slided);
        //Return this.
        return this;
    }

    public UnLong div(Number pDiv) {
        //Cast pDiv into a long.
        long div = pDiv.longValue();

        //Throw an error if trying to divide by zero.
        if (div == 0) throw new IllegalArgumentException("Cannot divide with zero");
        //If dividing by one, cancel.
        if (div == 1) return this;

        //Initialize the result.
        UnLong result = new UnLong();
        //Initialize the carry.
        AtomicLong carry = new AtomicLong(0);
        //For all values from the last index to the first index,
        this.forEachI((initValue, index) -> {
            //Set value to initValue plus the previous index's carry, if the carry has anything.
            long value = initValue;
            if (carry.get() != 0) {
                value += (carry.get() * LAYER_MAX_VALUE_PLUS_ONE);
            }

            //Set the resultValue for this index.
            long resultValue = value / div;
            //Set the carry for the next index.
            carry.set(value % div);

            //Set the result's index's value to the resultValue.
            result.setValue(index, resultValue);
        }, true);

        //Set this to the result.
        this.setTo(result);
        //Return this,
        return this;
    }

    public UnLong div(@NotNull UnLong div) {
        //Represent this with a BigInteger, bigSelf.
        AtomicReference<BigInteger> bigSelf = new AtomicReference<>(BigInteger.valueOf(0));
        //Add all values from this to bigSelf.
        this.forEachI((value, index) -> {
            //If the value in this index is zero, add nothing.
            if (value == 0) return;

            //Get the multiplier for this index.
            BigInteger multiplier = getMultiplierBigIntFromIndex(index);
            //Add this index's value to bigSelf.
            bigSelf.set(bigSelf.get().add(BigInteger.valueOf(value).multiply(multiplier)));
        });

        //Represent div with a BigInteger, bigDiv.
        AtomicReference<BigInteger> bigDiv = new AtomicReference<>(BigInteger.valueOf(0));
        //Add all values from div to bigDiv.
        div.forEachI((value, index) -> {
            //If the value in this index is zero, add nothing.
            if (value == 0) return;

            //Get the multiplier for this index.
            BigInteger multiplier = getMultiplierBigIntFromIndex(index);
            //Add this index's value to bigSelf.
            bigDiv.set(bigDiv.get().add(BigInteger.valueOf(value).multiply(multiplier)));
        });

        //Get the divided result in BigInteger.
        BigInteger bigResult = bigSelf.get().divide(bigDiv.get());

        //Set the result in UnLong.
        UnLong result = getDivResultByUnLong(bigResult);

        //Set this to the result.
        this.setTo(result);
        //Return this.
        return this;
    }

    public Float simulateDivAndGetFloat(@NotNull UnLong div) {
        //Represent this with a Float, fSelf.
        AtomicReference<Float> fSelf = new AtomicReference<>(0f);
        //Add all values from this to fSelf.
        this.forEachI((value, index) -> {
            //If the value in this index is zero, add nothing.
            if (value == 0) return;

            //Get the multiplier for this index.
            float multiplier = getMultiplierFloatFromIndex(index);
            //Add this index's value to fSelf.
            fSelf.set(fSelf.get() + (value * multiplier));
        });

        //Represent div with a Float, fDiv.
        AtomicReference<Float> fDiv = new AtomicReference<>(0f);
        //Add all values from div to fDiv.
        div.forEachI((value, index) -> {
            //If the value in this index is zero, add nothing.
            if (value == 0) return;

            //Get the multiplier for this index.
            float multiplier = getMultiplierFloatFromIndex(index);
            //Add this index's value to bigSelf.
            fDiv.set(fDiv.get() + (value * multiplier));
        });

        //Get the divided result in Float and return the result.
        return fSelf.get() / fDiv.get();
    }

    private static @NotNull UnLong getDivResultByUnLong(BigInteger bigResult) {
        //Initialize the result.
        UnLong result = new UnLong();
        //For all values,
        result.forEachI((value, index) -> {
            //Get the multiplier for this and the next index.
            BigInteger p1multiplier = getMultiplierBigIntFromIndex(index + 1);
            BigInteger multiplier = getMultiplierBigIntFromIndex(index);

            //Get the value that's for this index
            long valueResult = (multiplier.compareTo(BigInteger.ZERO) == 0 ? bigResult.mod(p1multiplier) : bigResult.mod(p1multiplier).divide(multiplier)).longValue();
            //Set the values if result in index, index.
            result.setValue(index, valueResult);
        });
        //Return the result.
        return result;
    }

    private boolean isZero() {
        return this.equals(zero());
    }

    public int getLayerSize() {
        return this.layerSize;
    }

    public List<Long> getValues() {
        return this.values;
    }

    public long getValue(int index) {
        return this.getValues().get(index);
    }

    public void setValue(int index, long value) {
        //Check if value is valid.
        value = checkValue(value);

        this.getValues().set(index, value);
    }

    //If the index is the last index of this UnLong
    private boolean isLastIndex(int index) {
        return index == (this.getLayerSize() - 1);
    }

    public void forEachI(@NotNull BiConsumer<? super Long, Integer> action) {
        this.forEachI(action, false);
    }

    public void forEachI(@NotNull BiConsumer<? super Long, Integer> action, boolean reversed) {
        //If reversed, use the for function reversed. If not, use the for function usually.
        if (reversed) {
            //Reversed for function.
            for (int i = this.getValues().size() - 1; i >= 0; i--) {
                //Execute the action.
                action.accept(this.getValues().get(i), i);
            }
        } else {
            //Normal for function.
            for (int i = 0; i < this.getValues().size(); i++) {
                //Execute the action.
                action.accept(this.getValues().get(i), i);
            }
        }
    }

    public boolean forEachI(@NotNull BiPredicate<? super Long, Integer> action) {
        return this.forEachI(action, false);
    }

    public boolean forEachI(@NotNull BiPredicate<? super Long, Integer> action, boolean reversed) {
        //If reversed, use the for function reversed. If not, use the for function usually.
        if (reversed) {
            //Reversed for function.
            for (int i = this.getValues().size() - 1; i >= 0; i--) {
                //Execute the action and get the return value.
                boolean doBreak = action.test(this.getValues().get(i), i);
                //If doBreak, break the loop.
                if (doBreak) return true;
            }
        } else {
            //Normal for function.
            for (int i = 0; i < this.getValues().size(); i++) {
                //Execute the action and get the return value.
                boolean doBreak = action.test(this.getValues().get(i), i);
                //If doBreak, break the loop.
                if (doBreak) return true;
            }
        }
        return false;
    }

    public boolean isSameAs(@NotNull Number number) {
        return this.longValue() == number.longValue();
    }

    public boolean isSameAs(@NotNull UnLong unLong) {
        return this.compareTo(unLong) == 0;
    }

    public boolean isSmallerOrSameAs(@NotNull Number number) {
        return this.longValue() <= number.longValue();
    }

    public boolean isSmallerOrSameAs(@NotNull UnLong unLong) {
        return this.compareTo(unLong) <= 0;
    }

    public boolean isSmallerThan(@NotNull Number number) {
        return this.longValue() < number.longValue();
    }

    public boolean isSmallerThan(@NotNull UnLong unLong) {
        return this.compareTo(unLong) < 0;
    }

    public boolean isGreaterOrSameAs(@NotNull Number number) {
        return this.longValue() >= number.longValue();
    }

    public boolean isGreaterOrSameAs(@NotNull UnLong unLong) {
        return this.compareTo(unLong) >= 0;
    }

    public boolean isGreaterThan(@NotNull Number number) {
        return this.longValue() > number.longValue();
    }

    public boolean isGreaterThan(@NotNull UnLong unLong) {
        return this.compareTo(unLong) > 0;
    }

    //Returns value or the nearest valid value.
    private static long checkValue(long value) {
        //Check the value if it's greater than 0 and smaller than the layers max value.
        // If not, log an error and cut to the nearest valid value.
        if (value > LAYER_MAX_VALUE) {
            LOGGER.error("The value: " + value + " is greater than the max layer value: " + LAYER_MAX_VALUE + ". The value will be cut to " + LAYER_MAX_VALUE + ".");
            return LAYER_MAX_VALUE;
        } else if (value < 0) {
            LOGGER.error("The value: " + value + " is smaller than the min layer value: 0. The value will be cut to 0.");
            return 0;
        }
        return value;
    }

    //Returns value or the nearest valid value.
    private static float checkValue(float value) {
        //Check the value if it's greater than 0 and smaller than the layers max value.
        // If not, log an error and cut to the nearest valid value.
        if (value > (float) LAYER_MAX_VALUE) {
            LOGGER.error("The value: " + value + " is greater than the max layer value: " + LAYER_MAX_VALUE + ". The value will be cut to " + LAYER_MAX_VALUE + ".");
            return (float) LAYER_MAX_VALUE;
        } else if (value < 0f) {
            LOGGER.error("The value: " + value + " is smaller than the min layer value: 0. The value will be cut to 0.");
            return 0f;
        }
        return value;
    }

    private static BigInteger getMultiplierBigIntFromIndex(int index) {
        return BigInteger.valueOf(UnLong.LAYER_MAX_VALUE_PLUS_ONE).pow(index);
    }

    private static float getMultiplierFloatFromIndex(int index) {
        //If index is invalid(zero or less), return zero.
        if (index < 0) return 0f;
        if (index == 0) return 1f;

        //Initialize the multiplier.
        float multiplier = (float) UnLong.LAYER_MAX_VALUE_PLUS_ONE;
        //Power the multiplier for index times.
        for (int i = 0; i < index; i++) {
            //Multiply the multiplier by UnLong's max layer value.
            multiplier *= (float) UnLong.LAYER_MAX_VALUE_PLUS_ONE;
        }
        //Return the multiplier.
        return multiplier;
    }

    //Return a new UnLong without inverting the values array (Inverts it twice).
    public static UnLong createWithoutReverse(@NotNull Long... values) {
        //Copy the values for inverting.
        var invertedValues = new ArrayList<>(Arrays.asList(values));
        //Reverse the invertedValues.
        Collections.reverse(invertedValues);
        //Create a UnLong with invertedValues.
        return new UnLong(invertedValues);
    }

    public static UnLong createWithoutReverse(@NotNull List<Long> values) {
        //Copy the values for inverting.
        var invertedValues = new ArrayList<>(values);
        //Reverse the invertedValues.
        Collections.reverse(invertedValues);
        //Create a UnLong with invertedValues.
        return new UnLong(invertedValues);
    }

    @Override
    public int intValue() {
        if (this.isSmallerThan(Integer.MAX_VALUE)) {
            return (int) this.getValue(0);
        } else {
            return Integer.MAX_VALUE;
        }
    }

    @Override
    public long longValue() {
        AtomicBoolean isOver = new AtomicBoolean(false);
        this.forEachI((value, index) -> {
            if (index == 0) return false;

            if (value > 0) {
                isOver.set(true);
                return true;
            }
            return false;
        });

        if (isOver.get()) {
            return Long.MAX_VALUE;
        } else {
            return this.getValue(0) + this.getValue(1) * UnLong.LAYER_MAX_VALUE_PLUS_ONE;
        }
    }

    @Override
    public float floatValue() {
        return this.longValue();
    }

    @Override
    public double doubleValue() {
        return this.longValue();
    }

    @Override
    public int compareTo(@NotNull UnLong unLong) {
        //Check same as unLong,
        AtomicBoolean isSameAs = new AtomicBoolean(true);
        this.forEachI((selfValue, index) -> {
            if (!isSameAs.get()) return true;

            long value = unLong.getValue(index);
            if (selfValue != value) {
                isSameAs.set(false);
            }
            return false;
        });
        if (isSameAs.get()) return 0;

        //Check greater than unLong,
        AtomicBoolean isGreaterThan = new AtomicBoolean(false);
        this.forEachI((selfValue, index) -> {
            long value = unLong.getValue(index);
            if (value > selfValue) {
                return true;
            } else if (selfValue > value) {
                isGreaterThan.set(true);
                return true;
            }
            return false;
        }, true);
        if (isGreaterThan.get()) return 1;

        //Automatically is smaller than unLong,
        return -1;
    }

    @NotNull
    @Override
    public Iterator<Long> iterator() {
        return new ArrayList<>(this.getValues()).iterator();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof UnLong unLong)) return false;

        return this.compareTo(unLong) == 0;
    }

    @NotNull
    public UnLong copy() {
        return UnLong.createWithoutReverse(this.getValues());
    }

    @Override
    //Returns the String converted this.
    public String toString() {
        if (this.isZero()) return "0";

        StringBuilder builder = new StringBuilder();

        AtomicBoolean hasValue = new AtomicBoolean(false);
        this.forEachI((value, index) -> {
            if (hasValue.get()) {
                builder.append(addExtraZero(value.toString()));
                return false;
            }

            if (value != 0) {
                builder.append(value);
                hasValue.set(true);
            } else {
                if (index == 0) {
                    builder.append("0");
                    return true;
                }
            }
            return false;
        }, true);

        return addComma(builder.toString());
    }

    private static final List<String> SUFFIXES = suffixes();

    //Returns the MutableComponent converted this. (With or without the suffix)
    public MutableComponent toComponent(boolean suffix, boolean shortType) {
        //If this is smaller or same than the Billion, return just the toString().
        if (this.isSmallerOrSameAs(UnLong.billion())) return Component.literal(this.toString());

        AtomicLong resultNum = new AtomicLong();
        AtomicInteger resultSuffix = new AtomicInteger(-1);
        boolean broken = this.forEachI((value, index) -> {
            if (value / 1_000_000 >= 1) {
                resultNum.set(value / 1_000_000);
                if (suffix) resultSuffix.set(index * 3 + 1);
                return true;
            } else if (value / 1_000 >= 1) {
                resultNum.set(value / 1_000);
                if (suffix) resultSuffix.set(index * 3);
                return true;
            } else if (value >= 1) {
                resultNum.set(this.getValue(index - 1) / 1_000_000);
                if (suffix) resultSuffix.set(index * 3 - 1);
                return true;
            }
            return false;
        }, true);
        if (!broken) return Component.literal(this.toString());

        return Component.literal(addComma(resultNum.toString())).append(Component.translatable("number.ornatelib." + SUFFIXES.get(resultSuffix.get()) + (shortType ? ".short" : "")));
    }

    private static ArrayList<String> suffixes() {
        ArrayList<String> suffix = new ArrayList<>();
        suffix.add("");
        suffix.add("thousand");
        suffix.add("million");
        suffix.add("billion");
        suffix.add("trillion");
        suffix.add("quadrillion");
        suffix.add("quintillion");
        suffix.add("sextillion");
        suffix.add("septillion");
        suffix.add("octillion");
        suffix.add("nonillion");

        suffix.add("decillion");
        suffix.add("undecillion");
        suffix.add("duodecillion");
        suffix.add("tredecillion");
        suffix.add("quattuordecillion");
        suffix.add("quindecillion");
        suffix.add("sedecillion");
        suffix.add("septendecillion");
        suffix.add("octodecillion");
        suffix.add("novendecillion");

        suffix.add("vigintillion");
        suffix.add("unvigintillion");
        suffix.add("duovigintillion");
        suffix.add("tresvigintillion");
        suffix.add("quattuorvigintillion");
        suffix.add("quinvigintillion");
        suffix.add("sesvigintillion");
        suffix.add("septemvigintillion");
        suffix.add("octovigintillion");
        suffix.add("novemvigintillion");

        suffix.add("trigintillion");
        suffix.add("untrigintillion");
        suffix.add("duotrigintillion");
        suffix.add("trestrigintillion");
        suffix.add("quattuortrigintillion");
        suffix.add("quintrigintillion");
        suffix.add("sestrigintillion");
        suffix.add("septentrigintillion");
        suffix.add("octotrigintillion");
        suffix.add("noventrigintillion");

        suffix.add("quadragintillion");
        return suffix;
    }

    private static String addExtraZero(String string) {
        if (string.isEmpty()) string = "0";

        StringBuilder toReturn = new StringBuilder();

        char[] characters = string.toCharArray();

        toReturn.append("0".repeat(Math.max(0, UnLong.LAYER_MAX_CHARACTER - characters.length)));
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

    //==Values==

    @NotNull
    public static UnLong zero() {
        return new UnLong(0L);
    }

    @NotNull
    public static UnLong one() {
        return new UnLong(1L);
    }

    @NotNull
    public static UnLong ten() {
        return new UnLong(10L);
    }

    @NotNull
    public static UnLong hundred() {
        return new UnLong(100L);
    }

    @NotNull
    public static UnLong thousand() {
        return new UnLong(1_000L);
    }

    @NotNull
    public static UnLong million() {
        return new UnLong(1_000_000L);
    }


    @NotNull
    public static UnLong billion() {
        return create((byte) 1, 1);
    }

    @NotNull
    public static UnLong trillion() {
        return create((byte) 1, 1_000L);
    }

    @NotNull
    public static UnLong quadrillion() {
        return create((byte) 1, 1_000_000L);
    }


    @NotNull
    public static UnLong quintillion() {
        return create((byte) 2, 1L);
    }

    @NotNull
    public static UnLong sextillion() {
        return create((byte) 2, 1_000L);
    }

    @NotNull
    public static UnLong septillion() {
        return create((byte) 2, 1_000_000L);
    }

    @NotNull
    public static UnLong octillion() {
        return create((byte) 3, 1L);
    }

    @NotNull
    public static UnLong nonillion() {
        return create((byte) 3, 1_000L);
    }

    @NotNull
    public static UnLong decillion() {
        return create((byte) 3, 1_000_000L);
    }


    @NotNull
    public static UnLong undecillion() {
        return create((byte) 4, 1L);
    }

    @NotNull
    public static UnLong duodecillion() {
        return create((byte) 4, 1_000L);
    }

    @NotNull
    public static UnLong tredecillion() {
        return create((byte) 4, 1_000_000L);
    }


    @NotNull
    public static UnLong quattuordecillion() {
        return create((byte) 5, 1L);
    }

    @NotNull
    public static UnLong quindecillion() {
        return create((byte) 5, 1_000);
    }

    @NotNull
    public static UnLong sedecillion() {
        return create((byte) 5, 1_000_000L);
    }


    @NotNull
    public static UnLong septendecillion() {
        return create((byte) 6, 1L);
    }

    @NotNull
    public static UnLong octodecillion() {
        return create((byte) 6, 1_000L);
    }

    @NotNull
    public static UnLong novendecillion() {
        return create((byte) 6, 1_000_000L);
    }


    @NotNull
    public static UnLong vigintillion() {
        return create((byte) 7, 1L);
    }

    @NotNull
    public static UnLong unvigintillion() {
        return create((byte) 7, 1_000L);
    }

    @NotNull
    public static UnLong duovigintillion() {
        return create((byte) 7, 1_000_000L);
    }


    @NotNull
    public static UnLong tresvigintillion() {
        return create((byte) 8, 1L);
    }

    @NotNull
    public static UnLong quattuorvigintillion() {
        return create((byte) 8, 1_000L);
    }

    @NotNull
    public static UnLong quinvigintillion() {
        return create((byte) 8, 1_000_000L);
    }


    @NotNull
    public static UnLong sesvigintillion() {
        return create((byte) 9, 1L);
    }

    @NotNull
    public static UnLong septemvigintillion() {
        return create((byte) 9, 1_000L);
    }

    @NotNull
    public static UnLong octovigintillion() {
        return create((byte) 9, 1_000_000L);
    }


    @NotNull
    public static UnLong novemvigintillion() {
        return create((byte) 10, 1L);
    }

    @NotNull
    public static UnLong trigintillion() {
        return create((byte) 10, 1_000L);
    }

    @NotNull
    public static UnLong untrigintillion() {
        return create((byte) 10, 1_000_000L);
    }


    @NotNull
    public static UnLong duotrigintillion() {
        return create((byte) 11, 1L);
    }

    @NotNull
    public static UnLong trestrigintillion() {
        return create((byte) 11, 1_000L);
    }

    @NotNull
    public static UnLong quattuortrigintillion() {
        return create((byte) 11, 1_000_000L);
    }


    @NotNull
    public static UnLong quintrigintillion() {
        return create((byte) 12, 1L);
    }

    @NotNull
    public static UnLong sestrigintillion() {
        return create((byte) 12, 1_000L);
    }

    @NotNull
    public static UnLong septentrigintillion() {
        return create((byte) 12, 1_000_000L);
    }


    @NotNull
    public static UnLong octotrigintillion() {
        return create((byte) 13, 1L);
    }

    @NotNull
    public static UnLong noventrigintillion() {
        return create((byte) 13, 1_000L);
    }

    @NotNull
    public static UnLong quadragintillion() {
        return create((byte) 13, 1_000_000L);
    }


    @NotNull
    private static UnLong create(byte plus9Digit, @NotNull Number... values) {
        return create(Arrays.stream(values).map(Number::longValue).toList(), plus9Digit);
    }

    @NotNull
    private static UnLong create(byte plus9Digit, @NotNull Long... values) {
        return create(Arrays.asList(values), plus9Digit);
    }

    @NotNull
    private static UnLong create(@NotNull List<Long> values, byte plus9Digit) {
        //Copy the values.
        List<Long> cValues = new ArrayList<>(values);
        //Add plus9Digit new digit's to cValues.
        for (byte i = 0; i < plus9Digit; i++) {
            cValues.add(0L);
        }
        return new UnLong(cValues);
    }
}
