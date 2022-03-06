package de.raywo.banking.textui.ui;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import de.raywo.banking.textui.operations.Operation;

public class MainMenuWindow extends ObservableBasicWindow {

  public MainMenuWindow(String title) {
    super(title);

    initWindow();
  }


  private void initWindow() {
    Label result = new Label("");
    Panel panel = new Panel();
    panel.setLayoutManager(new GridLayout(2));

    panel.addComponent(new EmptySpace(new TerminalSize(1, 1)));
    panel.addComponent(new EmptySpace(new TerminalSize(1, 1)));

    panel.addComponent(new Button("1. Kunden anlegen", () -> {
      Operation inputCustomer = Coordinator.instance().getInputCustomerDataOperation(this);
      this.setOperation(inputCustomer);
    }));
    panel.addComponent(new Button("3. alle Kunden anzeigen", () -> {
      Operation showAllCustomers = Coordinator.instance().getShowAllCustomersOperation(this);
      this.setOperation(showAllCustomers);
    }));
    panel.addComponent(new Button("2. Konto anlegen", () -> {
      result.setText("Konto anlegen");
    }));
    panel.addComponent(new Button("4. alle Konten anzeigen", () -> {
      result.setText("alle Konten anzeigen");
    }));

    panel.addComponent(new Button("x. Beenden", () -> {
      result.setText("Beenden");
      close();
    }));

    panel.addComponent(new EmptySpace(new TerminalSize(1, 1)));

    panel.addComponent(result);

    panel.addComponent(new EmptySpace(new TerminalSize(1, 1)));

    setComponent(panel);
  }
}
