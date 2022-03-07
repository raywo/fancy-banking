package de.raywo.banking.textui.operations;

import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import de.raywo.banking.textui.logic.Customer;
import de.raywo.banking.textui.persistence.CustomerRepository;
import de.raywo.banking.textui.ui.ObservableBasicWindow;

public class CreateCustomerOperation extends AbstractWindowOperation {
  private final CustomerRepository repository;
  private final String name;


  public CreateCustomerOperation(MultiWindowTextGUI gui,
                                 ObservableBasicWindow windowToRemove,
                                 ObservableBasicWindow windowToShow,
                                 CustomerRepository customerRepository,
                                 String name) {
    super(gui, windowToRemove, windowToShow);

    this.repository = customerRepository;
    this.name = name;
  }


  @Override
  public void execute() {
    Long newId = repository.nextId();
    Customer customer = new Customer(newId, name);
    repository.add(customer);

    super.execute();
  }
}
