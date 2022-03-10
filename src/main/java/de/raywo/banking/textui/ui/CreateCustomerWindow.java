package de.raywo.banking.textui.ui;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import de.raywo.banking.textui.operations.Operation;

public class CreateCustomerWindow extends ObservableBasicWindow {

  private final Label nameErrorLabel;
  private final Label dayOfBirthErrorLabel;
  private final Button createButton;
  private final TextBox nameTextBox;
  private final TextBox dayOfBirthTextBox;

  private boolean nameIsValid = false;
  private boolean dayOfBirthIsValid = true;

  private static final String NAME_ERROR_MESSAGE = "Muss mindestens zwei Zeichen haben.";


  public CreateCustomerWindow(String title) {
    super(title);

    nameTextBox = new TextBox(TerminalSize.ONE.withColumns(30));
    dayOfBirthTextBox = new TextBox(TerminalSize.ONE.withColumns(30));

    nameErrorLabel = new Label(NAME_ERROR_MESSAGE);
    dayOfBirthErrorLabel = new Label("");
    createButton = new Button("Anlegen");

    initWindow();
  }


  private void initWindow() {
    Panel panel = Panels.grid(2);

    addBlankRow(panel, null);

    addNameRow(panel);
    addBlankRow(panel, nameErrorLabel);

    addDayOfBirthRow(panel);
    addBlankRow(panel, dayOfBirthErrorLabel);

    addButtonRow(panel);

    setComponent(panel);
  }


  private void addButtonRow(Panel panel) {
    createButton.addListener(event -> {
      Operation createCustomer = Coordinator.instance()
          .getCreateCustomerOperation(this, nameTextBox.getText());
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


  private void addNameRow(Panel panel) {
    final Label nameLabel = new Label("Name");
    nameTextBox.setTextChangeListener(this::validateName);

    panel.addComponent(nameLabel);
    panel.addComponent(nameTextBox);
  }


  private void addDayOfBirthRow(Panel panel) {
    final Label dayOfBirthLabel = new Label("Geburtsdatum");
    dayOfBirthTextBox.setTextChangeListener(this::validateDayOfBirth);
    panel.addComponent(dayOfBirthLabel);
    panel.addComponent(dayOfBirthTextBox);
  }


  private boolean formIsValid() {
    return nameIsValid && dayOfBirthIsValid;
  }


  private void validateName(String value, boolean changedByUserInteraction) {
    if (isNameValid(value)) {
      nameErrorLabel.setText("");
      nameIsValid = true;
    } else {
      nameErrorLabel.setText(NAME_ERROR_MESSAGE);
      nameIsValid = false;
    }

    createButton.setEnabled(formIsValid());
  }


  private void validateDayOfBirth(String value, boolean changedByUserInteraction) {
    if (isDayOfBirthValid(value)) {
      dayOfBirthErrorLabel.setText("");
      dayOfBirthIsValid = true;
    } else {
      dayOfBirthErrorLabel.setText("Das Geburtsdatum muss in der Vergangenheit liegen.");
      dayOfBirthIsValid = false;
    }

    createButton.setEnabled(formIsValid());
  }


  private boolean isDayOfBirthValid(String value) {
    return value.isBlank();
  }


  private boolean isNameValid(String value) {
    return !value.isBlank() && value.length() >= 2;
  }


  private void addBlankRow(Panel panel, Label errorLabel) {
    panel.addComponent(new EmptySpace(TerminalSize.ONE.withColumns(20)));

    if (errorLabel == null) {
      panel.addComponent(new EmptySpace(TerminalSize.ONE.withColumns(30)));
    } else {
      panel.addComponent(errorLabel.setForegroundColor(TextColor.ANSI.RED));
    }
  }
}
