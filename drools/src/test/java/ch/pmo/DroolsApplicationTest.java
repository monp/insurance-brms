package ch.pmo;

import ch.pmo.domain.*;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kie.api.runtime.KieContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.money.Monetary;
import javax.money.RoundingQueryBuilder;
import java.time.LocalDate;

import static java.math.RoundingMode.HALF_EVEN;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.junit.MatcherAssert.assertThat;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = DroolsApplication.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class DroolsApplicationTest {

    @Autowired KieContainer kieContainer;

    @Test
    public void costCalculationIncapacityHighTest() {
        // Given
        val coverage = new Coverage();
        val salary = new Salary(10000, SalaryType.YEARLY);
        val certificate = new Certificate(
                LocalDate.of(2020, 1, 1),
                LocalDate.of(2020, 3, 31),
                1.0f);

        // When
        val allowance = new Allowance();
        val kieSession = kieContainer.newKieSession();
        kieSession.setGlobal("allowance", allowance);
        kieSession.insert(certificate);
        kieSession.insert(coverage);
        kieSession.insert(salary);
        kieSession.fireAllRules();
        kieSession.dispose();

        // Then
        val roundingMode = Monetary.getRounding(RoundingQueryBuilder.of().set(HALF_EVEN).setScale(2).build());
        val amount = allowance.getAmount().with(roundingMode);
        val expected = Money.of(679.45, "CHF");
        assertThat(amount, equalTo(expected));
    }

    @Test
    public void costCalculationIncapacityLowTest() {
        // Given
        val coverage = new Coverage();
        val salary = new Salary(10000, SalaryType.YEARLY);
        val certificate = new Certificate(
                LocalDate.of(2020, 1, 1),
                LocalDate.of(2020, 3, 31),
                0.1f);

        // When
        val allowance = new Allowance();
        val kieSession = kieContainer.newKieSession();
        kieSession.setGlobal("allowance", allowance);
        kieSession.insert(coverage);
        kieSession.insert(salary);
        kieSession.insert(certificate);
        kieSession.fireAllRules();
        kieSession.dispose();

        // Then
        val expected = Money.of(0, "CHF");
        assertThat(allowance.getAmount(), equalTo(expected));
    }

}