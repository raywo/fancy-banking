package de.raywo.shellui.logic;

public enum AccountType {
  CurrentAccount("Girokonto"),
  SavingsAccount("Sparkonto");

  public final String typeName;


  AccountType(String typeName) {
    this.typeName = typeName;
  }
}
