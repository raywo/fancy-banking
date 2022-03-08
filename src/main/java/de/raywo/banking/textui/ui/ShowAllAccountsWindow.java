package de.raywo.banking.textui.ui;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.EmptySpace;
import com.googlecode.lanterna.gui2.GridLayout;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.table.Table;
import com.googlecode.lanterna.gui2.table.TableModel;
import de.raywo.banking.textui.logic.Account;
import de.raywo.banking.textui.logic.CurrentAccount;
import de.raywo.banking.textui.logic.SavingsAccount;
import de.raywo.banking.textui.operations.Operation;
import de.raywo.banking.textui.operations.ShowMainMenuOperation;
import de.raywo.banking.textui.persistence.AccountRepository;

import java.text.DecimalFormat;
import java.util.Comparator;

public class ShowAllAccountsWindow extends ObservableBasicWindow {
  private final AccountRepository repository;


  public ShowAllAccountsWindow(String title, AccountRepository repository) {
    super(title);
    this.repository = repository;

    initWindow();
  }


  private void initWindow() {
    Panel panel = new Panel();
    panel.setLayoutManager(new GridLayout(1));

    panel.addComponent(new EmptySpace(new TerminalSize(1, 1)));

    Table<String> table = new Table<>("IBAN", "Art", "Saldo", "Dispolimit", "Habenzins", "Inhaber");
    TableModel<String> model = table.getTableModel();
    table.setPreferredSize(new TerminalSize(80, 15));

    repository.allAccounts()
        .values()
        .stream()
        .sorted(Comparator.comparing(Account::getIban))
        .forEach(account -> {
          DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
          String limitString = "";
          String interestRateString = "";

          switch (account.getAccountType()) {
            case CURRENT_ACCOUNT -> {
              CurrentAccount currentAccount = (CurrentAccount) account;
              limitString = decimalFormat.format(currentAccount.getLimit()) + "€";
            }

            case SAVINGS_ACCOUNT -> {
              SavingsAccount savingsAccount = (SavingsAccount) account;
              interestRateString = decimalFormat.format(savingsAccount.getInterestRate()) + "%";
            }
          }

          model.addRow(
              account.getIban(),
              account.getAccountType().typeName,
              decimalFormat.format(account.getBalance()),
              limitString,
              interestRateString,
              String.format("(%d) %s",
                  account.getOwner().getCustomerID(), account.getOwner().getName())
          );
        });

    panel.addComponent(table);

    panel.addComponent(new EmptySpace(new TerminalSize(1, 1)));

    Coordinator coordinator = Coordinator.instance();
    Operation back = new ShowMainMenuOperation(
        coordinator.getGui(),
        this,
        coordinator.getMainWindow());
    panel.addComponent(new Button("Zurück", () -> this.setOperation(back)));

    setComponent(panel);
  }
}
