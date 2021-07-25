package io.rv.restdemo.app.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class CalculationsHelperTest {

    private CalculationsHelper calculationsHelper = new CalculationsHelper();

    @Test
    public void shouldReturnNullForZeroFollowers() {
        var actResult = calculationsHelper.calculateFrom(0, 5);

        assertNull(actResult);
    }

    @ParameterizedTest
    @CsvSource({
            "1, 0, 12.0d",
            "1, 3, 30.0d",
            "2, 5, 21.0d",
            "3878, 8, 0.01547189273d"
    })
    public void shouldCalculate(final int followers, final int repos, final double expectedResult) {
        var actualResult = calculationsHelper.calculateFrom(followers, repos);

        assertEquals(expectedResult, actualResult, 1e-10d);
    }

}
