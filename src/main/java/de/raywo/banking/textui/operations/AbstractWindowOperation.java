package de.raywo.banking.textui.operations;

import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import de.raywo.banking.textui.ui.ObservableBasicWindow;
import lombok.Getter;

public abstract class AbstractWindowOperation implements Operation {

  @Getter
  protected final MultiWindowTextGUI gui;

  private final ObservableBasicWindow windowToRemove;
  private final ObservableBasicWindow windowToShow;


  protected AbstractWindowOperation(MultiWindowTextGUI gui,
                                    ObservableBasicWindow windowToRemove,
                                    ObservableBasicWindow windowToShow) {
    this.gui = gui;
    this.windowToRemove = windowToRemove;
    this.windowToShow = windowToShow;
  }


  @Override
  public void execute() {
    if (this.windowToRemove() != null) {
      this.gui.removeWindow(this.windowToRemove());
    }

    gui.addWindowAndWait(windowToShow());
  }


  @Override
  public ObservableBasicWindow windowToRemove() {
    return windowToRemove;
  }


  @Override
  public ObservableBasicWindow windowToShow() {
    return windowToShow;
  }
}
