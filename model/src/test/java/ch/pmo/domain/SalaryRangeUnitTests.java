package ch.pmo.domain;

import lombok.val;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.junit.MatcherAssert.assertThat;

class SalaryRangeUnitTests {

    @Test
    void preventsNulls() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> SalaryRange.of(null, null));
    }

    @Test
    void preventsLowSalaryToBeGreaterThanTheHighestOne() {
        val low = Money.of(100000, "CHF");
        val high = Money.of(10000, "CHF");
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> SalaryRange.of(low, high));
    }

    @Test
    void includesAGivenValue() {
        val low = Money.of(0, "CHF");
        val high = Money.of(100000, "CHF");
        val value = Money.of(8000, "CHF");

        val range = SalaryRange.of(low, high);

        assertThat(range.includes(value), is(true));
        assertThat(range.includes(value.negate()), is(false));
    }

}