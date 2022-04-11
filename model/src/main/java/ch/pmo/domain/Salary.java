package ch.pmo.domain;

import lombok.Data;
import org.javamoney.moneta.Money;

import javax.money.MonetaryAmount;

@Data
public class Salary {

    SalaryComponent basicSalary;
    SalaryComponent familyBenefits = SalaryComponent.of(SalaryType.YEARLY, Money.of(0, "CHF"));
    SalaryComponent holidayCompensation = SalaryComponent.of(SalaryType.YEARLY, Money.of(0, "CHF"));
    SalaryComponent gratuity = SalaryComponent.of(SalaryType.YEARLY, Money.of(0, "CHF"));
    SalaryComponent otherSupplements = SalaryComponent.of(SalaryType.YEARLY, Money.of(0, "CHF"));

    public Salary(Number amount, SalaryType type) {
        basicSalary = SalaryComponent.of(type, Money.of(amount, "CHF"));
    }

    public MonetaryAmount daily() {
        return annual().divide(365);
    }

    public MonetaryAmount annual() {
        return basicSalary.annual()
                .add(familyBenefits.annual())
                .add(holidayCompensation.annual())
                .add(gratuity.annual())
                .add(otherSupplements.annual());
    }
}
