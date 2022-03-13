package de.raywo.banking.textui.ui;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import de.raywo.banking.textui.logic.Account;
import de.raywo.banking.textui.logic.IllegalAmountException;
import de.raywo.banking.textui.logic.InsufficientBalanceException;
import de.raywo.banking.textui.operations.Operation;

import java.math.BigDecimal;
import java.util.Collection;

public class DepositWithdrawWindow extends ObservableBasicWindow {

  public enum BookingType {
    DEPOSIT("auf Konto einzahlen", "Einzahlen"),
    WITHDRAW("von Konto abheben", "Abheben");

    private final String description;
    private final String buttonLabel;


    BookingType(String description, String buttonLabel) {
      this.description = description;
      this.buttonLabel = buttonLabel;
    }


    @Override
    public String toString() {
      return description;
    }
  }

  private static final String ACCOUNT_ERROR_MESSAGE = "Es muss ein Konto ausgewählt werden.";
  private static final String AMOUNT_NEGATIVE_ERROR_MESSAGE = "Der Betrag muss positiv sein.";
  private static final String AMOUNT_NOT_A_NUMBER_ERROR_MESSAGE = "Der Betrag muss eine Zahl sein.";
  private static final String AMOUNT_INSUFFICIENT_ERROR_MESSAGE = "Der Betrag ist zu hoch.";

  private final Collection<Account> accounts;
  private final BookingType type;

  private final Button actionButton;
  private final Label accountErrorLabel;
  private final Label amountErrorLabel;

  private Account selectedAccount;
  private BigDecimal amount;


  public DepositWithdrawWindow(Collection<Account> accounts, BookingType type) {
    super(type.description);

    this.accounts = accounts;
    this.type = type;

    accountErrorLabel = new Label("");
    amountErrorLabel = new Label("");

    actionButton = new Button(type.buttonLabel);

    initWindow();
    validateForm();
  }


  private void initWindow() {
    Panel panel = Panels.grid(2);

    addBlankRow(panel, null);

    addAccountRow(panel);
    addBlankRow(panel, accountErrorLabel);

    addAmountRow(panel);
    addBlankRow(panel, amountErrorLabel);

    addButtonRow(panel);

    setComponent(panel);
  }


  private void addAccountRow(Panel panel) {
    ComboBox<Account> accountComboBox = new ComboBox<>(accounts);
    accountComboBox.setReadOnly(false);
    accountComboBox.setSelectedIndex(-1);
    accountComboBox.setPreferredSize(TerminalSize.ONE.withColumns(60));
    accountComboBox.addListener((index, previous, byUserInteraction) -> {
      selectedAccount = accountComboBox.getSelectedItem();
      validateAccount();
    });

    panel.addComponent(new Label("Konto: "));
    panel.addComponent(accountComboBox);
  }


  private void addAmountRow(Panel panel) {
    TextBox amountTextBox = new TextBox(TerminalSize.ONE.withColumns(10));
    amountTextBox.setTextChangeListener((value, byUserInteraction) -> extractAmount(value));
    Panel amountPanel = Panels.horizontal(amountTextBox, new Label("€"));

    panel.addComponent(new Label("Betrag:"));
    panel.addComponent(amountPanel);
  }


  @SuppressWarnings("java:S1301")
  private void addButtonRow(Panel panel) {
    actionButton.addListener(event -> {
      try {
        switch (type) {
          case DEPOSIT -> selectedAccount.deposit(amount);
          case WITHDRAW -> selectedAccount.withdraw(amount);
        }

        Operation createCustomer = Coordinator.instance()
            .getBackToMainOperation(this);
        this.setOperation(createCustomer);

      } catch (IllegalAmountException e) {
        amountErrorLabel.setText(AMOUNT_NEGATIVE_ERROR_MESSAGE);
      } catch (InsufficientBalanceException e) {
        amountErrorLabel.setText(AMOUNT_INSUFFICIENT_ERROR_MESSAGE);
      }
    });

    final Button cancelButton = new Button("Abbrechen", () -> {
      Operation returnToMain = Coordinator.instance()
          .getBackToMainOperation(this);
      this.setOperation(returnToMain);
    });

    panel.addComponent(actionButton);
    panel.addComponent(cancelButton);
  }


  private void addBlankRow(Panel panel, Label errorLabel) {
    panel.addComponent(new EmptySpace(TerminalSize.ONE.withColumns(15)));

    if (errorLabel == null) {
      panel.addComponent(new EmptySpace(TerminalSize.ONE.withColumns(40)));
    } else {
      panel.addComponent(errorLabel.setForegroundColor(TextColor.ANSI.RED));
    }
  }


  private void validateForm() {
    validateAccount();
  }


  private void validateAccount() {
    if (accountIsValid(selectedAccount)) {
      accountErrorLabel.setText("");
    } else {
      accountErrorLabel.setText(ACCOUNT_ERROR_MESSAGE);
    }
  }


  private boolean accountIsValid(Account selectedAccount) {
    return selectedAccount != null;
  }


  private void extractAmount(String value) {
    try {
      if (value == null || value.isBlank()) {
        amount = BigDecimal.ZERO;
      } else {
        double amountDouble = Double.parseDouble(value);
        amount = BigDecimal.valueOf(amountDouble);
      }

      amountErrorLabel.setText("");
      actionButton.setEnabled(true);
    } catch (NumberFormatException e) {
      amountErrorLabel.setText(AMOUNT_NOT_A_NUMBER_ERROR_MESSAGE);
      actionButton.setEnabled(false);
    }
  }
}
