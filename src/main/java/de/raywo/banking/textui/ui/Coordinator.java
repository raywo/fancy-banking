package de.raywo.banking.textui.ui;

import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import de.raywo.banking.textui.operations.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

@Log4j2
public class Coordinator implements PropertyChangeListener {

  private static Coordinator instance;

  @Getter
  @Setter
  private MultiWindowTextGUI gui;

  private ObservableBasicWindow mainWindow;
  private ObservableBasicWindow showAllCustomersWindow;
  private ObservableBasicWindow createCustomerWindow;


  private Coordinator() {
//    try (Terminal terminal = new DefaultTerminalFactory().createTerminal()) {
//      // Setup terminal and screen layers
//      Screen screen = new TerminalScreen(terminal);
//      screen.startScreen();
//
//      gui = new MultiWindowTextGUI(screen,
//          new DefaultWindowManager(),
//          new EmptySpace(TextColor.ANSI.BLUE));
//
////      screen.stopScreen();
//    } catch (IOException e) {
//      log.error(e.getMessage());
//    }
  }


  public static Coordinator instance() {
    if (instance == null) {
      instance = new Coordinator();
    }

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
    if (mainWindow == null) {
      mainWindow = new MainMenuWindow("Hauptmenü");
      mainWindow.addListener(this);
    }

    return mainWindow;
  }


  public ObservableBasicWindow getShowAllCustomersWindow() {
    if (showAllCustomersWindow == null) {
      showAllCustomersWindow = new ShowAllCustomersWindow("Alle Kunden");
      showAllCustomersWindow.addListener(this);
    }

    return showAllCustomersWindow;
  }


  public ObservableBasicWindow getCreateCustomerWindow() {
    if (createCustomerWindow == null) {
      createCustomerWindow = new CreateCustomerWindow("Kunden anlegen");
      createCustomerWindow.addListener(this);
    }

    return createCustomerWindow;
  }


  public Operation getBackToMainOperation(ObservableBasicWindow comingFrom) {
    return new ShowMainMenuOperation(this.gui, comingFrom, this.getMainWindow());
  }


  public Operation getShowAllCustomersOperation(ObservableBasicWindow comingFrom) {
    return new ShowAllCustomersOperation(this.gui, comingFrom, this.getShowAllCustomersWindow());
  }


  public Operation getCreateCustomerOperation(ObservableBasicWindow comingFrom) {
    return new CreateCustomerOperation(this.gui, comingFrom, this.getMainWindow());
  }


  public Operation getInputCustomerDataOperation(ObservableBasicWindow comingFrom) {
    return new InputCustomerDataOperation(this.gui, comingFrom, this.getCreateCustomerWindow());
  }

}
