package com.example.myapplication.test.java.com.example.myapplication;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * File created by tcarau18
 **/
public class MathTest {
    @Test
    public void addTwoNumbers() {
        double resultAdd = Math.addExact(1, 1);
        assertThat(resultAdd, equalTo(2d));
    }

    @Test
    public void addTwoNumbersNegative() {
        double resultAdd = Math.addExact(-1, 2);
        assertThat(resultAdd, equalTo(1d));
    }

    @Test
    public void subTwoNumbers() {
        double resultSub = Math.subtractExact(1, 1);
        assertThat(resultSub, equalTo(0d));
    }

    @Test
    public void subWorksWithNegativeResult() {
        double resultSub = Math.subtractExact(1, 17);
        assertThat(resultSub, equalTo(-16d));
    }

    @Test
    public void mulTwoNumbers() {
        double resultMul = Math.multiplyExact(32, 2);
        assertThat(resultMul, equalTo(64d));
    }

    @Test
    public void divTwoNumbers() {
        double resultDiv = Math.floorDiv(32, 2);
        assertThat(resultDiv, equalTo(16d));
    }

}
