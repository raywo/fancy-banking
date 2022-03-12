package de.raywo.banking.textui.ui;

import com.googlecode.lanterna.gui2.BasePaneListener;
import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.gui2.Window;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import de.raywo.banking.textui.operations.Operation;
import de.raywo.banking.textui.operations.ShowMainMenuOperation;
import lombok.Getter;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.googlecode.lanterna.gui2.Window.Hint.FULL_SCREEN;
import static com.googlecode.lanterna.gui2.Window.Hint.NO_POST_RENDERING;

public abstract class ObservableBasicWindow extends BasicWindow implements BasePaneListener<Window> {
  @Getter
  private Operation operation;
  private final PropertyChangeSupport changeSupport;


  protected ObservableBasicWindow(String title) {
    super(title);

    changeSupport = new PropertyChangeSupport(this);

    setHints(List.of(
        FULL_SCREEN,
        NO_POST_RENDERING)
    );

    addBasePaneListener(this);
  }


  public void updateState() {
    // empty implementation
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


  @Override
  public void onInput(Window window, KeyStroke keyStroke, AtomicBoolean atomicBoolean) {
    // empty implementation
  }


  @Override
  public void onUnhandledInput(Window window, KeyStroke keyStroke, AtomicBoolean atomicBoolean) {
    if (keyStroke.getKeyType().equals(KeyType.Escape)) {
      Coordinator coordinator = Coordinator.instance();
      Operation back = new ShowMainMenuOperation(
          coordinator.getGui(),
          this,
          coordinator.getMainWindow());
      this.setOperation(back);
    }
  }
}
