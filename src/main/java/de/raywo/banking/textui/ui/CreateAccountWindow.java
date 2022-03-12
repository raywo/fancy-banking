package de.raywo.banking.textui.ui;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import de.raywo.banking.textui.logic.*;
import de.raywo.banking.textui.operations.Operation;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreateAccountWindow extends ObservableBasicWindow implements RadioBoxList.Listener {
  private final Button createButton;
  private final Label ibanErrorLabel;
  private final Label additionalInfoLabel;
  private final TextBox ibanTextBox;
  private final TextBox additionalInfoTextBox;
  private final RadioBoxList<AccountType> radioBoxList;
  private final Label unitLabel;
  private final Label additionalInfoErrorLabel;

  private static final String IBAN_ERROR_MESSAGE = "IBAN: zwei Buchstaben am Anfang und insgesamt 12 Stellen";
  private static final String LIMIT_ERROR_MESSAGE = "muss größer oder gleich 0€ sein";
  private static final String INTEREST_RATE_ERROR_MESSAGE = "muss größer oder gleich 0% sein";

  private final Collection<Customer> customers;
  private Customer owner;


  public CreateAccountWindow(String title, Collection<Customer> customers) {
    super(title);
    this.customers = customers;

    ibanErrorLabel = new Label("");
    additionalInfoErrorLabel = new Label("");

    radioBoxList = new RadioBoxList<>();

    ibanTextBox = new TextBox(TerminalSize.ONE.withColumns(30));
    additionalInfoLabel = new Label("");
    additionalInfoTextBox = new TextBox(TerminalSize.ONE.withColumns(15));
    unitLabel = new Label("");

    createButton = new Button("Anlegen");

    initWindow();
  }


  @Override
  public void onSelectionChanged(int selectedIndex, int previousSelection) {
    setAdditionalLabel();
    additionalInfoTextBox.setText("");
    validateForm();
  }


  private void initWindow() {
    Panel mainPanel = Panels.grid(2);

    addBlankRow(mainPanel, null);

    addTypeRow(mainPanel);
    addBlankRow(mainPanel, null);

    addCustomerRow(mainPanel);
    addBlankRow(mainPanel, null);

    addIbanRow(mainPanel);
    addBlankRow(mainPanel, ibanErrorLabel);

    addAdditionalInfoRow(mainPanel);
    addBlankRow(mainPanel, additionalInfoErrorLabel);

    addButtonRow(mainPanel);

    validateForm();

    setComponent(mainPanel);
  }


  private void addTypeRow(Panel panel) {
    panel.addComponent(new Label("Kontoart:"));

    Arrays.stream(AccountType.values())
        .forEach(radioBoxList::addItem);
    radioBoxList.setCheckedItemIndex(0);
    radioBoxList.addListener(this);

    setAdditionalLabel();

    panel.addComponent(radioBoxList);
  }


  private void addCustomerRow(Panel panel) {
    ComboBox<Customer> customerComboBox = new ComboBox<>(customers);
    customerComboBox.setSelectedIndex(-1);
    customerComboBox.addListener((selectedIndex, previousIndex, changedByUserInteraction) -> {
      owner = customerComboBox.getSelectedItem();
    });

    panel.addComponent(new Label("Inhaber:"));
    panel.addComponent(customerComboBox);
  }


  private void addIbanRow(Panel panel) {
    ibanTextBox.setTextChangeListener(this::validateIban);

    panel.addComponent(new Label("IBAN:"));
    panel.addComponent(ibanTextBox);
  }


  private void addAdditionalInfoRow(Panel panel) {
    Panel textBoxPanel = Panels.horizontal(additionalInfoTextBox, unitLabel);
    additionalInfoTextBox.setTextChangeListener(this::validateAdditional);

    panel.addComponent(additionalInfoLabel);
    panel.addComponent(textBoxPanel);
  }


  private void addBlankRow(Panel panel, Label errorLabel) {
    panel.addComponent(new EmptySpace(TerminalSize.ONE.withColumns(15)));

    if (errorLabel == null) {
      panel.addComponent(new EmptySpace(TerminalSize.ONE.withColumns(40)));
    } else {
      panel.addComponent(errorLabel.setForegroundColor(TextColor.ANSI.RED));
    }
  }


  private void addButtonRow(Panel panel) {
    createButton.addListener(event -> {
      Operation createCustomer = Coordinator.instance()
          .getCreateAccountOperation(
              this,
              createAccount());
      this.setOperation(createCustomer);
    });
    createButton.setEnabled(formIsValid());
    final Button cancelButton = new Button("Abbrechen", () -> {
      Operation returnToMain = Coordinator.instance()
          .getBackToMainOperation(this);
      this.setOperation(returnToMain);
    });

    panel.addComponent(createButton);
    panel.addComponent(cancelButton);
  }


  private void setAdditionalLabel() {
    AccountType currentType = getAccountType();

    switch (currentType) {
      case CURRENT_ACCOUNT:
        additionalInfoLabel.setText("Dispolimit:");
        unitLabel.setText("€");
        break;
      case SAVINGS_ACCOUNT:
        additionalInfoLabel.setText("Habenzins:");
        unitLabel.setText("%");
        break;
    }
  }


  private boolean formIsValid() {
    final String iban = ibanTextBox.getText();
    final String additionalValue = additionalInfoTextBox.getText();

    return isIbanValid(iban) && isAdditionalValid(getAccountType(), additionalValue);
  }


  private void validateForm() {
    validateIban(ibanTextBox.getText(), false);
    validateAdditional(additionalInfoTextBox.getText(), false);
  }


  private void validateIban(String value, boolean changedByUserInteraction) {
    if (isIbanValid(value)) {
      ibanErrorLabel.setText("");
    } else {
      ibanErrorLabel.setText(IBAN_ERROR_MESSAGE);
    }

    createButton.setEnabled(formIsValid());
  }


  private void validateAdditional(String value, boolean changedByUserInteraction) {
    AccountType accountType = getAccountType();

    if (isAdditionalValid(accountType, value)) {
      additionalInfoErrorLabel.setText("");
    } else {
      switch (accountType) {
        case CURRENT_ACCOUNT -> additionalInfoErrorLabel.setText(LIMIT_ERROR_MESSAGE);
        case SAVINGS_ACCOUNT -> additionalInfoErrorLabel.setText(INTEREST_RATE_ERROR_MESSAGE);
      }
    }

    createButton.setEnabled(formIsValid());
  }


  private boolean isIbanValid(String currentIban) {
    Pattern pattern = Pattern.compile("^[A-Z]{2}[0-9]{10}");
    final Matcher matcher = pattern.matcher(currentIban);

    return currentIban.length() == 12 && matcher.matches();
  }


  private boolean isAdditionalValid(AccountType accountType, String value) {
    try {
      return value.isBlank() || Double.parseDouble(value) >= 0;
    } catch (NumberFormatException e) {
      return false;
    }
  }


  private AccountType getAccountType() {
    return radioBoxList.getCheckedItem();
  }


  private Account createAccount() {
    Account result = null;
    final String iban = ibanTextBox.getText();
    final String additionalValue = additionalInfoTextBox.getText();

    switch (getAccountType()) {
      case CURRENT_ACCOUNT -> {
        double limitValue = Double.parseDouble(additionalValue);
        BigDecimal limit = BigDecimal.valueOf(limitValue);

        CurrentAccount account = new CurrentAccount(iban, owner);
        account.setLimit(limit);

        result = account;
      }

      case SAVINGS_ACCOUNT -> {
        double interestRate = Double.parseDouble(additionalValue);

        SavingsAccount account = new SavingsAccount(iban, owner);
        account.setInterestRate(interestRate);

        result = account;
      }
    }

    return result;
  }
}
