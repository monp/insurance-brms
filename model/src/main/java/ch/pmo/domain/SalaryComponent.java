package ch.pmo.domain;

import lombok.NonNull;
import lombok.Value;
import org.javamoney.moneta.Money;

import javax.money.MonetaryAmount;

@Value(staticConstructor = "of")
class SalaryComponent {

    @NonNull SalaryType type;
    @NonNull MonetaryAmount amount;

    MonetaryAmount annual() {
        switch (type) {
            case YEARLY:
                return amount;
            case MONTHLY:
                return amount.multiply(12);
            case HOURLY:
                return amount.multiply(40).multiply(52);
            default:
                return Money.of(0, "CHF");
        }
    }
}
