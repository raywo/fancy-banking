package de.raywo.banking.textui.ui;

import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import de.raywo.banking.textui.logic.Account;
import de.raywo.banking.textui.operations.*;
import de.raywo.banking.textui.persistence.AccountRepository;
import de.raywo.banking.textui.persistence.CustomerRepository;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Date;

@Log4j2
public class Coordinator implements PropertyChangeListener {

  private static final Coordinator instance = new Coordinator();

  @Getter
  @Setter
  private MultiWindowTextGUI gui;

  @Getter
  private final AccountRepository accountRepository = new AccountRepository();
  @Getter
  private final CustomerRepository customerRepository = new CustomerRepository();


  private Coordinator() {
  }


  public static Coordinator instance() {
    return instance;
  }


  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    if (evt.getPropertyName().equals("operation")) {
      Operation operation = (Operation) evt.getNewValue();
      operation.execute();
    }
  }


  public void start() {
    Operation operation = getBackToMainOperation(null);
    operation.execute();
  }


  public ObservableBasicWindow getMainWindow() {
    ObservableBasicWindow window = new MainMenuWindow("Hauptmenü");
    window.addListener(this);

    return window;
  }


  public ObservableBasicWindow getShowAllCustomersWindow() {
    ObservableBasicWindow window = new ShowAllCustomersWindow("Alle Kunden", customerRepository);
    window.addListener(this);

    return window;
  }


  public ObservableBasicWindow getShowAllAccountsWindow() {
    ObservableBasicWindow window = new ShowAllAccountsWindow("Alle Konten", accountRepository);
    window.addListener(this);

    return window;
  }


  public ObservableBasicWindow getCreateCustomerWindow() {
    ObservableBasicWindow window = new CreateCustomerWindow("Kunden anlegen");
    window.addListener(this);

    return window;
  }


  public ObservableBasicWindow getCreateAccountWindow() {
    ObservableBasicWindow window = new CreateAccountWindow("Konto anlegen",
        customerRepository.allCustomers().values());
    window.addListener(this);

    return window;
  }


  public Operation getBackToMainOperation(ObservableBasicWindow comingFrom) {
    return new ShowMainMenuOperation(this.gui, comingFrom, this.getMainWindow());
  }


  public Operation getShowAllCustomersOperation(ObservableBasicWindow comingFrom) {
    return new ShowAllCustomersOperation(this.gui, comingFrom, this.getShowAllCustomersWindow());
  }


  public Operation getShowAllAccountsOperation(ObservableBasicWindow comingFrom) {
    return new ShowAllAccountsOperation(this.gui, comingFrom, this.getShowAllAccountsWindow());
  }


  public Operation getCreateCustomerOperation(ObservableBasicWindow comingFrom,
                                              String name,
                                              Date dayOfBirth) {
    return new CreateCustomerOperation(
        this.gui,
        comingFrom,
        this.getMainWindow(),
        customerRepository,
        name,
        dayOfBirth);
  }


  public Operation getCreateAccountOperation(ObservableBasicWindow comingFrom,
                                             Account account) {
    return new CreateAccountOperation(this.gui,
        comingFrom,
        this.getCreateAccountWindow(),
        this.accountRepository,
        account);
  }


  public Operation getInputCustomerDataOperation(ObservableBasicWindow comingFrom) {
    return new InputCustomerDataOperation(this.gui, comingFrom, this.getCreateCustomerWindow());
  }


  public Operation getInputAccountDataOperation(ObservableBasicWindow comingFrom) {
    return new InputAccountDataOperation(this.gui, comingFrom, this.getCreateAccountWindow());
  }

}
