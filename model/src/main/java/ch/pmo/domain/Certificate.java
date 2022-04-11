package ch.pmo.domain;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Certificate {
    DateRange period;
    float incapacity = 1.0f;

    public Certificate(LocalDate start, LocalDate end, float incapacity) {
        this.period = DateRange.from(start).to(end);
        this.incapacity = incapacity;
    }
}
