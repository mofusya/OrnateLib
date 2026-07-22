package net.mofusya.ornatelib.lang;

import com.mojang.logging.LogUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;

public final class UnLong extends Number implements Comparable<UnLong>, Iterable<Long> {
    private static final Logger LOGGER = LogUtils.getLogger();

    //The max layer size of the UnLong/
    private static final int LAYER_SIZE = 4;
    /* Memo
     * L: 9223372036854775807
     * I: 2147483647
     *
     * L: 1000000000000000000
     * I: 1000000000
     */
    private static final long LAYER_MAX_VALUE = 99;
    private static final long LAYER_MAX_VALUE_PLUS_ONE = LAYER_MAX_VALUE + 1;

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

    public UnLong(@NotNull Integer... values) {
        this(Arrays.stream(values).map(Integer::longValue).toList());
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

            //Add carryValue to carry with the next index.
            carry.setValue(index + 1, carryValue);
        }

        //If carry is not zero, add the carry to this.
        if (!carry.isZero()) {
            return this.add(carry);
        }

        //Return this;
        return this;
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
            if (shiftIndex >= this.getLayerSize() && -shiftIndex >= this.getLayerSize()) return true;

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


    public UnLong div(@NotNull UnLong div) {
        //Represent this with a BigInteger, bigSelf.
        AtomicReference<BigInteger> bigSelf = new AtomicReference<>(BigInteger.valueOf(0));
        //Add all values from this to bigSelf.
        this.forEachI((value, index) -> {
            //If the value in this index is zero, add nothing.
            if (value == 0) return;

            //Get the multiplier for this index.
            BigInteger multiplier = BigInteger.valueOf(UnLong.LAYER_MAX_VALUE_PLUS_ONE).multiply(BigInteger.valueOf(index));
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
            BigInteger multiplier = BigInteger.valueOf(UnLong.LAYER_MAX_VALUE_PLUS_ONE).multiply(BigInteger.valueOf(index));
            //Add this index's value to bigSelf.
            bigDiv.set(bigDiv.get().add(BigInteger.valueOf(value).multiply(multiplier)));
        });

        //Get the divided result in BigInteger.
        BigInteger bigResult = bigSelf.get().divide(bigDiv.get());

        //Initialize the result.
        UnLong result = new UnLong();
        result.forEachI((value, index) -> {
            //Get the multiplier for this next index.
            BigInteger multiplier = BigInteger.valueOf(UnLong.LAYER_MAX_VALUE_PLUS_ONE).multiply(BigInteger.valueOf(index + 1));

            BigInteger bigResultMod = bigResult.mod(multiplier);
        });
    }

    public UnLong div(int div) {
        return this.div((long) div);
    }

    public UnLong div(long div) {
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

        return this;
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

    //Return a new UnLong without inverting the values array (Inverts it twice).
    private static UnLong createWithoutReverse(@NotNull Long... values) {
        //Copy the values for inverting.
        var invertedValues = new ArrayList<>(Arrays.asList(values));
        //Reverse the invertedValues.
        Collections.reverse(invertedValues);
        //Create a UnLong with invertedValues.
        return new UnLong(invertedValues);
    }

    //Returns a new zero UnLong.
    private static UnLong zero() {
        return new UnLong(0L);
    }

    @Override
    public int intValue() {
        return 0;
    }

    @Override
    public long longValue() {
        return 0;
    }

    @Override
    public float floatValue() {
        return 0;
    }

    @Override
    public double doubleValue() {
        return 0;
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
            if (isGreaterThan.get()) return true;

            long value = unLong.getValue(index);
            if (selfValue > value) {
                isGreaterThan.set(true);
            }
            return false;
        });
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

    @Override
    //Returns the values but normalized (Reversed twice).
    public String toString() {
        //Copy the values for inverting.
        var normalizedValues = new ArrayList<>(this.getValues());
        //Reverse the normalizedValues.
        Collections.reverse(normalizedValues);
        //Return normalizedValues as string.
        return normalizedValues.toString();
    }
}
