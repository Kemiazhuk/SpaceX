package com.spacex.dragons.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ValidationUtilsTest {

    @Test
    void requireNonEmpty_ShouldNotThrow_ForValidString() {
        assertDoesNotThrow(() -> ValidationUtils.requireNonEmpty("RocketX", "Rocket name"));
    }

    @Test
    void requireNonEmpty_ShouldThrow_ForNullString() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                ValidationUtils.requireNonEmpty(null, "Rocket name")
        );
        assertEquals("Rocket name can't be null or empty", ex.getMessage());
    }

    @Test
    void requireNonEmpty_ShouldThrow_ForEmptyString() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                ValidationUtils.requireNonEmpty("  ", "Rocket name")
        );
        assertEquals("Rocket name can't be null or empty", ex.getMessage());
    }

    @Test
    void requireNonNull_ShouldNotThrow_ForNonNull() {
        assertDoesNotThrow(() -> ValidationUtils.requireNonNull("value", "Field"));
    }

    @Test
    void requireNonNull_ShouldThrow_ForNull() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                ValidationUtils.requireNonNull(null, "Field")
        );
        assertEquals("Field can't be null", ex.getMessage());
    }

    @Test
    void requireFound_ShouldNotThrow_WhenObjectFound() {
        Object obj = new Object();
        assertDoesNotThrow(() -> ValidationUtils.requireFound(obj, "Not found"));
    }

    @Test
    void requireFound_ShouldThrow_WhenObjectIsNull() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                ValidationUtils.requireFound(null, "Not found")
        );
        assertEquals("Not found", ex.getMessage());
    }

    @Test
    void requireState_ShouldNotThrow_WhenConditionTrue() {
        assertDoesNotThrow(() -> ValidationUtils.requireState(true, "Should not fail"));
    }

    @Test
    void requireState_ShouldThrow_WhenConditionFalse() {
        IllegalStateException ex = assertThrows(IllegalStateException.class, () ->
                ValidationUtils.requireState(false, "Invalid state")
        );
        assertEquals("Invalid state", ex.getMessage());
    }
}
