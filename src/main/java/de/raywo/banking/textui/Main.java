package de.raywo.banking.textui;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.gui2.DefaultWindowManager;
import com.googlecode.lanterna.gui2.EmptySpace;
import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import de.raywo.banking.textui.logic.CurrentAccount;
import de.raywo.banking.textui.logic.Customer;
import de.raywo.banking.textui.logic.SavingsAccount;
import de.raywo.banking.textui.ui.Coordinator;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Main {


  public static void main(String[] args) throws IOException {
    try (Terminal terminal = new DefaultTerminalFactory().createTerminal()) {
      // Setup terminal and screen layers
      Screen screen = new TerminalScreen(terminal);
      screen.startScreen();

      MultiWindowTextGUI gui = new MultiWindowTextGUI(screen,
          new DefaultWindowManager(),
          new EmptySpace(TextColor.ANSI.BLUE));

//      String[] splash = {
//          "    ______                          ____              __   _",
//          "   / ____/___ _____  _______  __   / __ )____ _____  / /__(_)___  ____ _",
//          "  / /_  / __ `/ __ \\/ ___/ / / /  / __  / __ `/ __ \\/ //_/ / __ \\/ __ `/",
//          " / __/ / /_/ / / / / /__/ /_/ /  / /_/ / /_/ / / / / ,< / / / / / /_/ /",
//          "/_/    \\__,_/_/ /_/\\___/\\__, /  /_____/\\__,_/_/ /_/_/|_/_/_/ /_/\\__, /",
//          "                       /____/                                  /____/"
//      };

      String[] splash = {
          "$$$$$$$$\\                                                                        ",
          "$$  _____|                                                                        ",
          "$$ |   $$$$$$\\  $$$$$$$\\   $$$$$$$\\ $$\\   $$\\                                ",
          "$$$$$\\ \\____$$\\ $$  __$$\\ $$  _____|$$ |  $$ |                                ",
          "$$  __|$$$$$$$ |$$ |  $$ |$$ /      $$ |  $$ |                                    ",
          "$$ |  $$  __$$ |$$ |  $$ |$$ |      $$ |  $$ |                                    ",
          "$$ |  \\$$$$$$$ |$$ |  $$ |\\$$$$$$$\\ \\$$$$$$$ |                                ",
          "\\__|   \\_______|\\__|  \\__| \\_______| \\____$$ |                              ",
          "                                    $$\\   $$ |                                   ",
          "                                    \\$$$$$$  |                                   ",
          "                                     \\______/                                    ",
          "             $$$$$$$\\                      $$\\       $$\\                       ",
          "             $$  __$$\\                     $$ |      \\__|                       ",
          "             $$ |  $$ | $$$$$$\\  $$$$$$$\\  $$ |  $$\\ $$\\ $$$$$$$\\   $$$$$$\\ ",
          "             $$$$$$$\\ | \\____$$\\ $$  __$$\\ $$ | $$  |$$ |$$  __$$\\ $$  __$$\\",
          "             $$  __$$\\  $$$$$$$ |$$ |  $$ |$$$$$$  / $$ |$$ |  $$ |$$ /  $$ |    ",
          "             $$ |  $$ |$$  __$$ |$$ |  $$ |$$  _$$<  $$ |$$ |  $$ |$$ |  $$ |     ",
          "             $$$$$$$  |\\$$$$$$$ |$$ |  $$ |$$ | \\$$\\ $$ |$$ |  $$ |\\$$$$$$$ | ",
          "             \\_______/  \\_______|\\__|  \\__|\\__|  \\__|\\__|\\__|  \\__| \\____$$ |",
          "                                                                   $$\\   $$ |",
          "                                                                   \\$$$$$$  |",
          "                                                                    \\______/ "
      };

      TextGraphics textGraphics = screen.newTextGraphics();
      textGraphics.setForegroundColor(TextColor.ANSI.YELLOW_BRIGHT);

      for (int i = 0; i < splash.length; i++) {
        textGraphics.putString(1, i + 1, splash[i]);
      }

      screen.refresh();

//      try {
//        Thread.sleep(3000);
//      } catch (InterruptedException e) {
//        Thread.currentThread().interrupt();
//        e.printStackTrace();
//      }

      Coordinator coordinator = Coordinator.instance();

      Customer ray = new Customer(1L, "Ray Wojciechowski");
      CurrentAccount currentAccount = new CurrentAccount("DE1234567890", ray);
      currentAccount.setLimit(BigDecimal.valueOf(250L));
      SavingsAccount savingsAccount = new SavingsAccount("DE2345678901", ray);
      savingsAccount.setInterestRate(0.15);

      coordinator.getCustomerRepository().add(ray);
      coordinator.getAccountRepository().add(currentAccount);
      coordinator.getAccountRepository().add(savingsAccount);

      for (int i = 0; i < 25; i++) {
        final Customer customer = new Customer((long) i, "Kunde " + (i + 1));
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        customer.setDayOfBirth(dateFormat.parse("2000-01-06"));
        coordinator.getCustomerRepository().add(customer);
      }

      coordinator.setGui(gui);

      coordinator.start();

      screen.stopScreen();
    } catch (ParseException e) {
      e.printStackTrace();
    }
  }
}
