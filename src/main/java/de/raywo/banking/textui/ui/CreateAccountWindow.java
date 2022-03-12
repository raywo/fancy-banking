package de.raywo.banking.textui.ui;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import de.raywo.banking.textui.logic.*;
import de.raywo.banking.textui.operations.Operation;
import lombok.SneakyThrows;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("java:S1301")
public class CreateAccountWindow extends ObservableBasicWindow {
  private final Button createButton;

  private final TextBox ibanTextBox;
  private final TextBox additionalInfoTextBox;
  private final ComboBox<Customer> ownerComboBox;
  private final RadioBoxList<AccountType> accountTypeBoxList;

  private final Label additionalInfoLabel;
  private final Label unitLabel;

  private final Label ownerErrorLabel;
  private final Label ibanErrorLabel;
  private final Label additionalInfoErrorLabel;

  private static final String OWNER_ERROR_MESSAGE = "muss ausgewählt werden";
  private static final String IBAN_ERROR_MESSAGE = "IBAN: zwei Buchstaben am Anfang und insgesamt 12 Stellen";
  private static final String LIMIT_ERROR_MESSAGE = "muss größer oder gleich 0€ sein";
  private static final String INTEREST_RATE_ERROR_MESSAGE = "muss größer oder gleich 0% sein";

  private Customer owner;


  public CreateAccountWindow(String title, Collection<Customer> customers) {
    super(title);

    ownerErrorLabel = new Label("");
    ibanErrorLabel = new Label("");
    additionalInfoErrorLabel = new Label("");

    ownerComboBox = new ComboBox<>(customers);
    accountTypeBoxList = new RadioBoxList<>();

    ibanTextBox = new TextBox(TerminalSize.ONE.withColumns(30));
    additionalInfoLabel = new Label("");
    additionalInfoTextBox = new TextBox(TerminalSize.ONE.withColumns(15));
    unitLabel = new Label("");

    createButton = new Button("Anlegen");

    initWindow();
  }


  private void initWindow() {
    Panel mainPanel = Panels.grid(2);

    addBlankRow(mainPanel, null);

    addTypeRow(mainPanel);
    addBlankRow(mainPanel, null);

    addOwnerRow(mainPanel);
    addBlankRow(mainPanel, ownerErrorLabel);

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
        .forEach(accountTypeBoxList::addItem);
    accountTypeBoxList.setCheckedItemIndex(0);
    accountTypeBoxList.addListener(this::onTypeChanged);

    setAdditionalLabel(getAccountType());

    panel.addComponent(accountTypeBoxList);
  }


  private void addOwnerRow(Panel panel) {
    ownerComboBox.setSelectedIndex(-1);
    ownerComboBox.addListener((selectedIndex, previousIndex, changedByUserInteraction) -> {
      owner = ownerComboBox.getSelectedItem();
      validateOwner();
    });

    panel.addComponent(new Label("Inhaber:"));
    panel.addComponent(ownerComboBox);
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


  private void onTypeChanged(int selectedIndex, int previousSelection) {
    setAdditionalLabel(getAccountType());
    additionalInfoTextBox.setText("");
    validateForm();
  }


  private void setAdditionalLabel(AccountType currentType) {
    switch (currentType) {
      case CURRENT_ACCOUNT -> {
        additionalInfoLabel.setText("Dispolimit:");
        unitLabel.setText("€");
      }
      case SAVINGS_ACCOUNT -> {
        additionalInfoLabel.setText("Habenzins:");
        unitLabel.setText("%");
      }
    }
  }


  private boolean formIsValid() {
    final String iban = ibanTextBox.getText();
    final String additionalValue = additionalInfoTextBox.getText();

    return isOwnerValid() && isIbanValid(iban) && isAdditionalValid(additionalValue);
  }


  private void validateForm() {
    validateOwner();
    validateIban(ibanTextBox.getText(), false);
    validateAdditional(additionalInfoTextBox.getText(), false);
  }


  private void validateOwner() {
    if (isOwnerValid()) {
      ownerErrorLabel.setText("");
    } else {
      ownerErrorLabel.setText(OWNER_ERROR_MESSAGE);
    }

    createButton.setEnabled(formIsValid());
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

    if (isAdditionalValid(value)) {
      additionalInfoErrorLabel.setText("");
    } else {
      switch (accountType) {
        case CURRENT_ACCOUNT -> additionalInfoErrorLabel.setText(LIMIT_ERROR_MESSAGE);
        case SAVINGS_ACCOUNT -> additionalInfoErrorLabel.setText(INTEREST_RATE_ERROR_MESSAGE);
      }
    }

    createButton.setEnabled(formIsValid());
  }


  private boolean isOwnerValid() {
    return owner != null;
  }


  private boolean isIbanValid(String currentIban) {
    Pattern pattern = Pattern.compile("^[A-Z]{2}[0-9]{10}");
    final Matcher matcher = pattern.matcher(currentIban);

    return currentIban.length() == 12 && matcher.matches();
  }


  private boolean isAdditionalValid(String value) {
    try {
      return value.isBlank() || getNumberFormat().parse(value).doubleValue() >= 0;
    } catch (ParseException e) {
      return false;
    }
  }


  private NumberFormat getNumberFormat() {
    NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.GERMANY);
    numberFormat.setMaximumFractionDigits(2);

    return numberFormat;
  }


  private AccountType getAccountType() {
    return accountTypeBoxList.getCheckedItem();
  }


  @SneakyThrows
  private Account createAccount() {
    Account result = null;
    final String iban = ibanTextBox.getText();
    final String additionalValue = additionalInfoTextBox.getText();
    final Number parsedNumber = getNumberFormat().parse(additionalValue);

    switch (getAccountType()) {
      case CURRENT_ACCOUNT -> {
        CurrentAccount account = new CurrentAccount(iban, owner);

        BigDecimal limit = BigDecimal.valueOf(parsedNumber.doubleValue());
        account.setLimit(limit);

        result = account;
      }

      case SAVINGS_ACCOUNT -> {
        SavingsAccount account = new SavingsAccount(iban, owner);
        account.setInterestRate(parsedNumber.doubleValue());

        result = account;
      }
    }

    return result;
  }
}
