package de.raywo.banking.textui.operations;

import de.raywo.banking.textui.ui.ObservableBasicWindow;

public interface Operation {

  ObservableBasicWindow windowToRemove();

  ObservableBasicWindow windowToShow();

  void execute();
}
