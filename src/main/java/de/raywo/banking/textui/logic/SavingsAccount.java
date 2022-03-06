package de.raywo.shellui.logic;

import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("java:S2160")
public class SavingsAccount extends Account {
  @Getter
  @Setter
  private double interestRate;

  public SavingsAccount(String iban, Customer owner) {
    super(iban, owner);
    this.interestRate = 0;
    this.accountType = AccountType.SavingsAccount;
  }
}
