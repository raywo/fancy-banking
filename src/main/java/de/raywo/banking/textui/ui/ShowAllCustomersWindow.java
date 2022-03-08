package de.raywo.banking.textui.ui;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.EmptySpace;
import com.googlecode.lanterna.gui2.GridLayout;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.table.Table;
import com.googlecode.lanterna.gui2.table.TableModel;
import de.raywo.banking.textui.logic.Customer;
import de.raywo.banking.textui.operations.Operation;
import de.raywo.banking.textui.operations.ShowMainMenuOperation;
import de.raywo.banking.textui.persistence.CustomerRepository;

import java.text.DateFormat;
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
    panel.setLayoutManager(new GridLayout(1));

    panel.addComponent(new EmptySpace(new TerminalSize(1, 1)));

    Table<String> table = new Table<>("Ku-Nr.", "Name", "Geburtsdatum");
    TableModel<String> model = table.getTableModel();
    table.setPreferredSize(new TerminalSize(80, 15));

    repository.allCustomers()
        .values()
        .stream()
        .sorted(Comparator.comparingLong(Customer::getCustomerID))
        .forEach(customer -> {
          DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.LONG);
          String dayOfBirth = "- keine Angabe –";

          if (customer.getDayOfBirth() != null) {
            dayOfBirth = dateFormat.format(customer.getDayOfBirth());
          }

          model.addRow(
              customer.getCustomerID().toString(),
              customer.getName(),
              dayOfBirth
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
