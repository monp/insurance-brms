package ch.pmo.domain;

import lombok.NonNull;
import lombok.Value;
import org.springframework.util.Assert;

import javax.money.MonetaryAmount;

@Value
public class SalaryRange {

    @NonNull MonetaryAmount low;
    @NonNull MonetaryAmount high;

    private SalaryRange(MonetaryAmount low, MonetaryAmount high) {
        Assert.notNull(low, "Low salary boundary cannot be null.");
        Assert.notNull(high, "High salary boundary cannot be null.");
        if(!isValid(low, high)) {
            throw new IllegalArgumentException("Low boundary cannot be higher than the higher one.");
        }
        this.low = low;
        this.high = high;
    }

    private boolean isValid(MonetaryAmount low, MonetaryAmount high) {
        return high.isGreaterThan(low);
    }

    public static SalaryRange of(MonetaryAmount low, MonetaryAmount high) {
        return new SalaryRange(low, high);
    }

    public boolean includes(MonetaryAmount amount) {
        Assert.notNull(amount, "Amount cannot be null.");
        return !amount.isLessThan(low) && !amount.isGreaterThan(high);
    }
}
