package de.raywo.banking.textui.logic;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Objects;


public abstract class Account {
  @NonNull
  @Getter
  private final String iban;
  @Getter
  private BigDecimal balance;
  @Getter
  @Setter
  private Customer owner;

  @Getter
  protected AccountType accountType = null;


  protected Account(String iban, Customer owner) {
    this.iban = iban;
    this.owner = owner;
    this.balance = BigDecimal.ZERO;
  }


  public void deposit(BigDecimal amount) throws IllegalAmountException {
    validateAmount(amount);
    this.balance = this.balance.add(amount);
  }


  public void withdraw(BigDecimal amount) throws IllegalAmountException, InsufficientBalanceException {
    validateAmount(amount);

    if (!sufficientBalanceFor(amount)) {
      final String message = "Der verfügbare Betrag reicht für diese Abhebung nicht aus.";
      throw new InsufficientBalanceException(message);
    }
    this.balance = this.balance.subtract(amount);
  }

  public String formattedBalance() {
    return formattedValue(this.getBalance());
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Account account = (Account) o;
    return iban.equals(account.iban);
  }


  @Override
  public int hashCode() {
    return Objects.hash(iban);
  }


  @Override
  public String toString() {
    final String typeName = accountType != null ? accountType.typeName : "";

    return String.format("%s: %s€ (%s%s); Inhaber: %s",
        iban, formattedValue(balance), typeName, specificInformation(), owner);
  }


  public String specificInformation() {
    return "";
  }


  protected boolean sufficientBalanceFor(BigDecimal amount) {
    return this.balance.compareTo(amount) >= 0;
  }


  protected String formattedValue(BigDecimal value) {
    DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");

    return decimalFormat.format(value);
  }


  private void validateAmount(BigDecimal amount) throws IllegalAmountException {
    if (amount.compareTo(BigDecimal.ZERO) < 0) {
      throw new IllegalAmountException("Der Betrag muss positiv sein!");
    }
  }
}
