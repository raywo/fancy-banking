package de.raywo.banking.textui.ui;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import de.raywo.banking.textui.operations.Operation;

public class CreateCustomerWindow extends ObservableBasicWindow {

  public CreateCustomerWindow(String title) {
    super(title);

    initWindow();
  }


  private void initWindow() {
    Panel panel = new Panel();
    panel.setLayoutManager(new GridLayout(2));

    final Label nameLabel = new Label("Name");
    panel.addComponent(nameLabel);
    final TextBox nameTextBox = new TextBox();
    panel.addComponent(nameTextBox);

    panel.addComponent(new EmptySpace(new TerminalSize(1, 1)));
    panel.addComponent(new EmptySpace(new TerminalSize(1, 1)));

    final Button createButton = new Button("Anlegen", () -> {
      Operation createCustomer = Coordinator.instance().getCreateCustomerOperation(this);
      this.setOperation(createCustomer);
    });
    panel.addComponent(createButton);

    setComponent(panel);
  }
}
