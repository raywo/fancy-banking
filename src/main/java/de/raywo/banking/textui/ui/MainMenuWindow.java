package de.raywo.banking.textui.ui;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import de.raywo.banking.textui.logic.Account;
import de.raywo.banking.textui.operations.Operation;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class MainMenuWindow extends ObservableBasicWindow {


  private Label accountCountLabel;
  private Label customerCountLabel;
  private Label totalBalanceLabel;


  public MainMenuWindow(String title) {
    super(title);

    initWindow();
  }


  private void initWindow() {
    Label result = new Label("");
    Panel mainPanel = new Panel();
    int columns = 2;
    LayoutData horizontalFill = GridLayout.createHorizontallyFilledLayoutData(1);
    LayoutData horizontalTopFill = GridLayout.createLayoutData(
        GridLayout.Alignment.FILL,
        GridLayout.Alignment.BEGINNING,
        true, false,
        1, 5);
    mainPanel.setLayoutManager(new GridLayout(columns));

    Panel buttonPanel = getButtonPanel(result, horizontalFill);

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

    DecimalFormat df = new DecimalFormat("#,##0.00");
    String totalBalanceString = df.format(totalBalance);

    accountCountLabel.setText(accountCount + "");
    customerCountLabel.setText(customerCount + "");
    totalBalanceLabel.setText(totalBalanceString + "€");
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


  private Panel getButtonPanel(Label result, LayoutData horizontalFill) {
    Panel buttonPanel = new Panel();

    final EmptySpace emptyLine = new EmptySpace(new TerminalSize(1, 1));
    buttonPanel.addComponent(emptyLine, horizontalFill);

    buttonPanel.addComponent(new Button("1. Kunden anlegen", () -> {
      Operation inputCustomer = Coordinator.instance().getInputCustomerDataOperation(this);
      this.setOperation(inputCustomer);
    }));

    buttonPanel.addComponent(new Button("2. Konto anlegen", () -> {
      result.setText("Konto anlegen");
    }));

    buttonPanel.addComponent(new Button("3. alle Kunden anzeigen", () -> {
      Operation showAllCustomers = Coordinator.instance().getShowAllCustomersOperation(this);
      this.setOperation(showAllCustomers);
    }));

    buttonPanel.addComponent(new Button("4. alle Konten anzeigen", () -> {
      Operation showAllAccounts = Coordinator.instance().getShowAllAccountsOperation(this);
      this.setOperation(showAllAccounts);
    }));

    buttonPanel.addComponent(new Button("5. auf Konto einzahlen", () -> {
      result.setText("auf Konto einzahlen");
    }));

    buttonPanel.addComponent(new Button("6. von Konto abheben", () -> {
      result.setText("von Konto abheben");
    }));

    buttonPanel.addComponent(new Separator(Direction.HORIZONTAL), horizontalFill);

    buttonPanel.addComponent(new Button("x. Beenden", () -> {
      result.setText("Beenden");
      close();
    }));

    buttonPanel.addComponent(emptyLine, horizontalFill);

    buttonPanel.addComponent(result);
    return buttonPanel;
  }
}
