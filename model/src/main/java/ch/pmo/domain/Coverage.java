package ch.pmo.domain;

import lombok.Data;
import org.javamoney.moneta.Money;

@Data
public class Coverage {
    float rate = 0.8f;
    float minIncapacityRate = 0.25f;
    int waitingPeriod = 0;
    int coveredPeriod = -1;
    SalaryRange salaryRange = SalaryRange.of(
            Money.of(0, "CHF"),
            Money.of(148000, "CHF"));
}
