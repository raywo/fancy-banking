package de.raywo.banking.textui.ui;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import de.raywo.banking.textui.logic.Customer;
import de.raywo.banking.textui.operations.Operation;
import de.raywo.banking.textui.operations.ShowMainMenuOperation;
import de.raywo.banking.textui.persistence.CustomerRepository;

import java.util.Comparator;

public class ShowAllCustomersWindow extends ObservableBasicWindow {

  CustomerRepository repository;


  public ShowAllCustomersWindow(String title, CustomerRepository repository) {
    super(title);

    this.repository = repository;

    initWindow();
  }


  private void initWindow() {
    Panel panel = new Panel();
    panel.setLayoutManager(new GridLayout(2));

    repository.allCustomers()
        .values()
        .stream()
        .sorted(Comparator.comparingLong(Customer::getCustomerID))
        .forEach(customer -> {
          panel.addComponent(new Label(customer.getCustomerID().toString()));
          panel.addComponent(new Label(customer.getName()));
        });

    panel.addComponent(new EmptySpace(new TerminalSize(1, 1)));
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
