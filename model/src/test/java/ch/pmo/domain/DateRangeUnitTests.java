package ch.pmo.domain;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Period;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.junit.MatcherAssert.assertThat;

class DateRangeUnitTests {

    @Test
    void preventsNullStart() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> DateRange.from(null));
    }

    @Test
    void allowsNullEnd() {
        DateRange dateRange = DateRange.from(LocalDate.now()).to(null);
        assertThat(dateRange.getEnd(), Matchers.nullValue());
    }

    @Test
    void preventsStartToBeGreaterThanEnd() {
        LocalDate now = LocalDate.now();
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> DateRange.of(now.plusDays(1), now));
    }

    @Test
    void instancesWithSameStartAndEndAreEqual() {
        LocalDate start = LocalDate.now();
        LocalDate end = start.plusDays(2);

        DateRange first = DateRange.from(start).to(end);
        DateRange second = DateRange.from(start).to(end);

        assertThat(first, Matchers.is(first));
        assertThat(first, Matchers.is(second));
        assertThat(second, Matchers.is(first));
    }

    @Test
    void instancesWithDifferentEndsAreNotEqual() {
        LocalDate start = LocalDate.now();

        DateRange first = DateRange.from(start).to(start.plusDays(1));
        DateRange second = DateRange.from(start).to(start.plusDays(2));

        assertThat(first).isNotEqualTo(second);
        assertThat(second).isNotEqualTo(first);
    }

    @Test
    void instancesWithDifferentStartsAreNotEqual() {
        LocalDate reference = LocalDate.now();

        DateRange first = DateRange.from(reference.minusDays(1)).to(reference);
        DateRange second = DateRange.from(reference.minusDays(2)).to(reference);

        assertThat(first).isNotEqualTo(second);
        assertThat(second).isNotEqualTo(first);
    }

    @Test
    void instancesWithDifferentStartsAndEndsAreNotEqual() {
        LocalDate reference = LocalDate.now();

        DateRange first = DateRange.from(reference.minusDays(1)).to(reference);
        DateRange second = DateRange.from(reference.plusDays(1)).to(reference.plusDays(2));

        assertThat(first).isNotEqualTo(second);
        assertThat(second).isNotEqualTo(first);
    }

    @Test
    void detectsContainedDateTimes() {
        LocalDate now = LocalDate.now();
        LocalDate nowTomorrow = now.plusDays(1);
        LocalDate nowYesterday = now.minusDays(1);

        DateRange interval = DateRange.from(nowYesterday).to(nowTomorrow);

        assertThat(interval.contains(now), is(true));
        assertThat(interval.contains(nowTomorrow), is(true));
        assertThat(interval.contains(nowYesterday), is(true));

        assertThat(interval.contains(nowYesterday.minusDays(1)), is(false));
        assertThat(interval.contains(nowTomorrow.plusDays(1)), is(false));
    }

    @Test
    void rejectsNullForContainsReference() {
        LocalDate now = LocalDate.now();
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> DateRange.from(now).to(now.plusDays(1)).contains(null));
    }

    @Test
    void detectsOverlaps() {
        LocalDate now = LocalDate.now();

        DateRange first = DateRange.from(now.minusDays(1)).to(now.plusDays(2));
        DateRange second = DateRange.from(now.minusDays(2)).to(now.plusDays(1));
        DateRange third = DateRange.from(now).to(now.plusDays(1));
        DateRange fourth = DateRange.from(now.minusDays(2)).to(now.minusDays(1));

        // Partial
        assertThat(first.overlaps(second), is(true));
        assertThat(second.overlaps(first), is(true));

        // Containment
        assertThat(third.overlaps(first), is(true));
        assertThat(first.overlaps(third), is(true));

        // No overlap
        assertThat(first.overlaps(fourth), is(false));
        assertThat(fourth.overlaps(first), is(false));
    }

    @Test
    void rejectsNullForOverlapReference() {
        LocalDate now = LocalDate.now();
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> DateRange.from(now).to(now.plusDays(1)).overlaps(null));
    }

    @Test
    void exposesDateRangeAsPeriod() {
        LocalDate now = LocalDate.now();
        assertThat(DateRange.from(now).to(now.plusDays(1)).period(), is(Period.ofDays(1)));
    }

    @Test
    void exposesDateRangeAsPeriodAgain() {
        LocalDate now = LocalDate.now();
        assertThat(DateRange.from(now).withLength(Period.ofDays(1)).period(), is(Period.ofDays(1)));
    }

    @Test
    void exposesDateRangeAsDays() {
        LocalDate now = LocalDate.now();
        assertThat(DateRange.from(now).to(now.plusDays(1)).days(), is(2L));
    }

}
