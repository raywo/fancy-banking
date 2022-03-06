package de.raywo.banking.textui.ui;

import com.googlecode.lanterna.gui2.BasicWindow;
import de.raywo.banking.textui.operations.Operation;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;

import static com.googlecode.lanterna.gui2.Window.Hint.EXPANDED;
import static com.googlecode.lanterna.gui2.Window.Hint.NO_POST_RENDERING;

public class ObservableBasicWindow extends BasicWindow {
  private Operation operation;
  private final PropertyChangeSupport changeSupport;


  public ObservableBasicWindow(String title) {
    super(title);

    changeSupport = new PropertyChangeSupport(this);

    setHints(List.of(
        EXPANDED,
        NO_POST_RENDERING)
    );
  }


  public void addListener(PropertyChangeListener listener) {
    changeSupport.addPropertyChangeListener(listener);
  }


  public void removeListener(PropertyChangeListener listener) {
    changeSupport.removePropertyChangeListener(listener);
  }


  protected void setOperation(Operation value) {
    changeSupport.firePropertyChange("operation", this.operation, value);
    this.operation = value;
  }


  public Operation getOperation() {
    return operation;
  }
}
