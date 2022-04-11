package ch.pmo.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.javamoney.moneta.Money;

import javax.money.MonetaryAmount;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class Allowance {
    DateRange period;
    MonetaryAmount amount;

    public Allowance(LocalDate from, LocalDate to, Number amount) {
        this.period = DateRange.from(from).to(to);
        this.amount = Money.of(amount, "CHF");
    }
}
