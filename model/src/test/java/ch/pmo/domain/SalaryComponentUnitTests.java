package ch.pmo.domain;

import lombok.val;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.junit.MatcherAssert.assertThat;

class SalaryComponentUnitTests {

    @Test
    void preventsNullArguments() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> SalaryComponent.of(null, null));
    }

    @Test
    void annualSalary() {
        val salary = SalaryComponent.of(SalaryType.YEARLY, Money.of(100000, "CHF"));
        assertThat(salary.annual(), is(Money.of(100000, "CHF")));
    }

    @Test
    void annualSalaryFromMonthlyOne() {
        val salary = SalaryComponent.of(SalaryType.MONTHLY, Money.of(10000, "CHF"));
        assertThat(salary.annual(), is(Money.of(120000, "CHF")));
    }

    @Test
    void annualSalaryFromHourlyOne() {
        val salary = SalaryComponent.of(SalaryType.HOURLY, Money.of(100, "CHF"));
        assertThat(salary.annual(), is(Money.of(208000, "CHF")));
    }
}