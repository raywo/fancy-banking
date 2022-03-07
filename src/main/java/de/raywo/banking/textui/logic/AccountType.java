package de.raywo.banking.textui.logic;

public enum AccountType {
  CURRENT_ACCOUNT("Girokonto"),
  SAVINGS_ACCOUNT("Sparkonto");

  public final String typeName;


  AccountType(String typeName) {
    this.typeName = typeName;
  }
}
