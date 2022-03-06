package de.raywo.banking.textui.ui;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import de.raywo.banking.textui.operations.Operation;
import de.raywo.banking.textui.operations.ShowMainMenuOperation;

import java.util.List;

public class ShowAllCustomersWindow extends ObservableBasicWindow {

  List<String> customers = List.of("Ray", "Ralf", "Dinko", "David",
      "Ray", "Ralf", "Dinko", "David",
      "Ray", "Ralf", "Dinko", "David",
      "Ray", "Ralf", "Dinko", "David",
      "Ray", "Ralf", "Dinko", "David",
      "Ray", "Ralf", "Dinko", "David");


  public ShowAllCustomersWindow(String title) {
    super(title);

    Panel panel = new Panel();
    panel.setLayoutManager(new GridLayout(2));

    for (int i = 0; i < customers.size(); i++) {
      panel.addComponent(new Label(i + ""));
      panel.addComponent(new Label(customers.get(i)));
    }

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
