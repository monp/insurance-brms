package ch.pmo.domain;

import lombok.*;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.TemporalAmount;
import java.util.Objects;
import java.util.Optional;

@Value
public class DateRange {

    @NonNull LocalDate start;
    LocalDate end;

    public DateRange(LocalDate start, LocalDate end) {
        Assert.notNull(start, "Begin date cannot be null.");
        if(!isValid(start, end)) {
            throw new IllegalArgumentException("End date must be null or higher than begin date.");
        }
        this.start = start;
        this.end = end;
    }

    private boolean isValid(LocalDate start, LocalDate end) {
        return end == null || start.isBefore(end) || start.isEqual(end);
    }

    public static DateRange of(LocalDate from, LocalDate to) {
        return new DateRange(from, to);
    }

    public boolean contains(LocalDate eventDate) {
        Assert.notNull(eventDate, "Event date cannot be null.");
        if (end != null && eventDate.isAfter(end)) {
            return false;
        }
        return !eventDate.isBefore(start);
    }

    public boolean overlaps(DateRange reference) {
        Assert.notNull(reference, "Reference must not be null!");

        boolean endsAfterOtherStarts = getEnd().isAfter(reference.getStart());
        boolean startsBeforeOtherEnds = getStart().isBefore(reference.getEnd());

        return startsBeforeOtherEnds && endsAfterOtherStarts;
    }

    public DateRange endOn(LocalDate endDate) {
        return new DateRange(start, endDate);
    }

    public Period period() {
        return Period.between(start, Optional.ofNullable(end).orElse(LocalDate.now()));
    }

    public long days() {
        return period().getDays() + 1;
    }

    @Override
    public String toString() {
        return String.format("DateRange from %s to %s", start, end);
    }

    public static DateRangeBuilder from(LocalDate start) {
        Assert.notNull(start, "Begin date cannot be null.");
        return new DateRangeBuilder(start);
    }

    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public static class DateRangeBuilder {

        private final @NonNull LocalDate from;

        public DateRange to(LocalDate end) {
            return new DateRange(from, end);
        }

        public DateRange withLength(TemporalAmount amount) {
            Objects.requireNonNull(amount, "Temporal amount must not be null!");
            return new DateRange(from, from.plus(amount));
        }
    }
}
