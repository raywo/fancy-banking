package de.raywo.banking.textui.ui;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.input.KeyStroke;
import de.raywo.banking.textui.logic.Account;
import de.raywo.banking.textui.operations.Operation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.NotImplementedException;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class MainMenuWindow extends ObservableBasicWindow {

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  private static class MenuItem {
    private char hotKey;
    private String label;
    private Runnable action;
  }

  @NoArgsConstructor
  private static class MenuSeparator extends MenuItem {
    private static final String MESSAGE = "This is method is not used in a MenuSeparator.";


    @Override
    public char getHotKey() {
      throw new NotImplementedException(MESSAGE);
    }


    @Override
    public String getLabel() {
      throw new NotImplementedException(MESSAGE);
    }


    @Override
    public Runnable getAction() {
      throw new NotImplementedException(MESSAGE);
    }
  }


  private final List<MenuItem> menuEntries = new ArrayList<>();

  private Label accountCountLabel;
  private Label customerCountLabel;
  private Label totalBalanceLabel;


  public MainMenuWindow(String title) {
    super(title);

    menuEntries.add(new MenuItem('1', "1. Kunden anlegen", this::createCustomer));
    menuEntries.add(new MenuItem('2', "2. Konto anlegen", this::createAccount));
    menuEntries.add(new MenuItem('3', "3. alle Kunden anzeigen", this::showAllCustomers));
    menuEntries.add(new MenuItem('4', "4. alle Konten anzeigen", this::showAllAccounts));
    menuEntries.add(new MenuItem('5', "5. auf Konto einzahlen", this::depositOnAccount));
    menuEntries.add(new MenuItem('6', "6. von Konto abheben", this::withdrawFromAccount));
    menuEntries.add(new MenuSeparator());
    menuEntries.add(new MenuItem('x', "x. Beenden", this::close));

    initWindow();
  }


  private void initWindow() {
    int columns = 2;
    Panel mainPanel = Panels.grid(columns);

    LayoutData horizontalFill = GridLayout.createHorizontallyFilledLayoutData(1);
    LayoutData horizontalTopFill = GridLayout.createLayoutData(
        GridLayout.Alignment.FILL,
        GridLayout.Alignment.BEGINNING,
        true, false,
        1, 5);

    Panel buttonPanel = getButtonPanel();
    Panel infoPanel = getInfoPanel();

    mainPanel.addComponent(buttonPanel, horizontalFill);
    mainPanel.addComponent(infoPanel.withBorder(Borders.singleLineBevel()), horizontalTopFill);

    updateState();
    setComponent(mainPanel);
  }


  @Override
  public void updateState() {
    int accountCount = Coordinator.instance().getAccountRepository().count();
    int customerCount = Coordinator.instance().getCustomerRepository().count();
    BigDecimal totalBalance = getTotalBalance();

    NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
    String totalBalanceString = numberFormat.format(totalBalance.doubleValue());

    accountCountLabel.setText(accountCount + "");
    customerCountLabel.setText(customerCount + "");
    totalBalanceLabel.setText(totalBalanceString);
  }


  @Override
  public void onUnhandledInput(Window window, KeyStroke keyStroke, AtomicBoolean atomicBoolean) {
    Character character = keyStroke.getCharacter();

    if (character != null) {
      menuEntries.stream()
          .filter(menuItem -> !(menuItem instanceof MenuSeparator) && menuItem.getHotKey() == character)
          .forEach(menuItem -> menuItem.getAction().run());
    }
  }


  private Panel getInfoPanel() {
    int columns = 2;
    LayoutData horizontalFill = GridLayout.createHorizontallyFilledLayoutData(columns);

    Panel infoPanel = Panels.grid(2);
    infoPanel.addComponent(new Label("Info"), horizontalFill);
    infoPanel.addComponent(new Separator(Direction.HORIZONTAL), horizontalFill);

    infoPanel.addComponent(new Label("Anzahl Konten:"));
    accountCountLabel = new Label("0");
    infoPanel.addComponent(accountCountLabel);

    infoPanel.addComponent(new Label("Anzahl Kunden:"));
    customerCountLabel = new Label("0");
    infoPanel.addComponent(customerCountLabel);

    infoPanel.addComponent(new Label("Gesamtguthaben:"));
    totalBalanceLabel = new Label("0,00€");
    infoPanel.addComponent(totalBalanceLabel);

    return infoPanel;
  }


  private BigDecimal getTotalBalance() {
    return Coordinator.instance().getAccountRepository().allAccounts()
        .values()
        .stream()
        .map(Account::getBalance)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }


  private Panel getButtonPanel() {
    Panel buttonPanel = new Panel();

    final EmptySpace emptyLine = new EmptySpace(new TerminalSize(1, 1));
    buttonPanel.addComponent(emptyLine);

    menuEntries.forEach(menuItem -> {
      if (menuItem instanceof MenuSeparator) {
        final Separator separator = new Separator(Direction.HORIZONTAL);
        separator.setPreferredSize(TerminalSize.ONE.withColumns(17));
        buttonPanel.addComponent(separator);
      } else {
        buttonPanel.addComponent(new Button(menuItem.getLabel(), menuItem.getAction()));
      }
    });

    buttonPanel.addComponent(emptyLine);

    return buttonPanel;
  }


  private void createCustomer() {
    Operation inputCustomer = Coordinator.instance().getInputCustomerDataOperation(this);
    this.setOperation(inputCustomer);
  }


  private void createAccount() {
    Operation inputAccount = Coordinator.instance().getInputAccountDataOperation(this);
    this.setOperation(inputAccount);
  }


  private void showAllCustomers() {
    Operation showAllCustomers = Coordinator.instance().getShowAllCustomersOperation(this);
    this.setOperation(showAllCustomers);
  }


  private void showAllAccounts() {
    Operation showAllAccounts = Coordinator.instance().getShowAllAccountsOperation(this);
    this.setOperation(showAllAccounts);
  }


  private void depositOnAccount() {
    Operation deposit = Coordinator.instance().getInputDepositDataOperation(this);
    this.setOperation(deposit);
  }


  private void withdrawFromAccount() {
    Operation deposit = Coordinator.instance().getInputWithdrawDataOperation(this);
    this.setOperation(deposit);
  }
}
