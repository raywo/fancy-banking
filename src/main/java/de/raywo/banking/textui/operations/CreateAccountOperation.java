package de.raywo.banking.textui.operations;

import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import de.raywo.banking.textui.logic.Account;
import de.raywo.banking.textui.persistence.AccountRepository;
import de.raywo.banking.textui.ui.ObservableBasicWindow;

public class CreateAccountOperation extends AbstractWindowOperation {
  private final AccountRepository repository;
  private final Account account;


  public CreateAccountOperation(MultiWindowTextGUI gui,
                                ObservableBasicWindow windowToRemove,
                                ObservableBasicWindow windowToShow,
                                AccountRepository repository,
                                Account account) {
    super(gui, windowToRemove, windowToShow);
    this.repository = repository;
    this.account = account;
  }


  @Override
  public void execute() {
    repository.add(account);

    super.execute();
  }
}
