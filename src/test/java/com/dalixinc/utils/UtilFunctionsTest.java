package com.dalixinc.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UtilFunctionsTest {

    UtilFunctions utilFunctions = new UtilFunctions();
    @Test
    void bubbleArray() {
        Object[] arr = {5, null, 8, null, 2};
        Object[] expected = {5, 8, 2, null, null};
        utilFunctions.bubbleArray(arr);
        assertArrayEquals(expected, arr);
    }
}