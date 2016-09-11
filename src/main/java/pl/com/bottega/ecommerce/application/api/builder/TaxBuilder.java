package pl.com.bottega.ecommerce.application.api.builder;

import java.math.BigDecimal;

import pl.com.bottega.ecommerce.sales.domain.invoicing.Tax;
import pl.com.bottega.ecommerce.sharedkernel.Money;

public class TaxBuilder {
    private Money money = new Money(new BigDecimal("100"), "USD");
    private String description = "Description";

    public TaxBuilder() {
    }

    public TaxBuilder withAmount(Money money) {
        this.money = money;
        return this;
    }

    public TaxBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public Tax build() {
        return new Tax(money, description);
    }
}