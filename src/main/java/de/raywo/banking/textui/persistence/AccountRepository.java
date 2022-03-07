package de.raywo.banking.textui.persistence;

import de.raywo.banking.textui.logic.Account;

import java.util.HashMap;
import java.util.Map;

public class AccountRepository {
  private final Map<String, Account> accounts = new HashMap<>();


  public void add(Account account) {
    this.accounts.put(account.getIban(), account);
  }


  public Account get(String iban) {
    return this.accounts.get(iban);
  }


  public Map<String, Account> allAccounts() {
    return accounts;
  }
}
