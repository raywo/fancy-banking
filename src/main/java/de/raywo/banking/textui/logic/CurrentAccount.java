package de.raywo.shellui.logic;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@SuppressWarnings("java:S2160")
public class CurrentAccount extends Account {
  @Getter
  @Setter
  private BigDecimal limit;

  public CurrentAccount(String iban, Customer owner) {
    super(iban, owner);
    this.limit = BigDecimal.ZERO;
    this.accountType = AccountType.CurrentAccount;
  }

  public String formattedLimit() {
    return formattedValue(this.getLimit());
  }

  @Override
  public String specificInformation() {
    return String.format(", Dispo: %s€", formattedValue(this.getLimit()));
  }

  @Override
  protected boolean sufficientBalanceFor(BigDecimal amount) {
    BigDecimal availableAmount = this.getBalance().add(this.getLimit());
    return availableAmount.compareTo(amount) >= 0;
  }
}
