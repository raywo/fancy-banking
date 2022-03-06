package de.raywo.banking.textui.operations;

import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import de.raywo.banking.textui.ui.ObservableBasicWindow;

public class ShowAllCustomersOperation extends AbstractWindowOperation {


  public ShowAllCustomersOperation(MultiWindowTextGUI gui,
                                   ObservableBasicWindow windowToRemove,
                                   ObservableBasicWindow windowToShow) {
    super(gui, windowToRemove, windowToShow);
  }
}
