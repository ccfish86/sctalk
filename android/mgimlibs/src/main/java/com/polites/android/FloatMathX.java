package com.polites.android;

import android.util.FloatMath;

/**
 * Created by Yuangui on 2017-05-04.
 */

public class FloatMathX  {

    /** Prevents instantiation. */
    private FloatMathX() {}

    /**
     * Returns the float conversion of the most positive (i.e. closest to
     * positive infinity) integer value which is less than the argument.
     *
     * @param value to be converted
     * @return the floor of value
     * @removed
     */
    public static float floor(float value) {
        return (float) Math.floor(value);
    }

    /**
     * Returns the float conversion of the most negative (i.e. closest to
     * negative infinity) integer value which is greater than the argument.
     *
     * @param value to be converted
     * @return the ceiling of value
     * @removed
     */
    public static float ceil(float value) {
        return (float) Math.ceil(value);
    }

    /**
     * Returns the closest float approximation of the sine of the argument.
     *
     * @param angle to compute the cosine of, in radians
     * @return the sine of angle
     * @removed
     */
    public static float sin(float angle) {
        return (float) Math.sin(angle);
    }

    /**
     * Returns the closest float approximation of the cosine of the argument.
     *
     * @param angle to compute the cosine of, in radians
     * @return the cosine of angle
     * @removed
     */
    public static float cos(float angle) {
        return (float) Math.cos(angle);
    }

    /**
     * Returns the closest float approximation of the square root of the
     * argument.
     *
     * @param value to compute sqrt of
     * @return the square root of value
     * @removed
     */
    public static float sqrt(float value) {
        return (float) Math.sqrt(value);
    }

    /**
     * Returns the closest float approximation of the raising "e" to the power
     * of the argument.
     *
     * @param value to compute the exponential of
     * @return the exponential of value
     * @removed
     */
    public static float exp(float value) {
        return (float) Math.exp(value);
    }

    /**
     * Returns the closest float approximation of the result of raising {@code
     * x} to the power of {@code y}.
     *
     * @param x the base of the operation.
     * @param y the exponent of the operation.
     * @return {@code x} to the power of {@code y}.
     * @removed
     */
    public static float pow(float x, float y) {
        return (float) Math.pow(x, y);
    }

    /**
     * Returns {@code sqrt(}<i>{@code x}</i><sup>{@code 2}</sup>{@code +} <i>
     * {@code y}</i><sup>{@code 2}</sup>{@code )}.
     *
     * @param x a float number
     * @param y a float number
     * @return the hypotenuse
     * @removed
     */
    public static float hypot(float x, float y) {
        return (float) Math.hypot(x, y);
    }
}
