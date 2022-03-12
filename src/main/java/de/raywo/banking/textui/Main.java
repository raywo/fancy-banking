package de.raywo.banking.textui;

import com.github.javafaker.Faker;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.SimpleTheme;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.graphics.Theme;
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
import java.util.Date;
import java.util.Locale;

import static com.googlecode.lanterna.TextColor.ANSI.*;

public class Main {

  public static void main(String[] args) throws IOException {
    new Main();
  }


  private Main() throws IOException {
    try (Terminal terminal = new DefaultTerminalFactory().createTerminal()) {
      // Setup terminal and screen layers
      Screen screen = new TerminalScreen(terminal);
      screen.startScreen();

      MultiWindowTextGUI gui = new MultiWindowTextGUI(screen,
          new DefaultWindowManager(),
          new EmptySpace(TextColor.ANSI.BLUE));

      setTheme(gui);

      showSplashScreen(screen);
      startGui(gui);
      screen.stopScreen();
    }
  }


  private void startGui(MultiWindowTextGUI gui) {
    Coordinator coordinator = Coordinator.instance();
    coordinator.setGui(gui);

    createFakeData(coordinator);

    coordinator.start();
  }


  private void showSplashScreen(Screen screen) throws IOException {
    String[] splash = getSplash();

    TextGraphics textGraphics = screen.newTextGraphics();
    textGraphics.setForegroundColor(ANSI.YELLOW_BRIGHT);

    for (int i = 0; i < splash.length; i++) {
      textGraphics.putString(1, i + 1, splash[i]);
    }

    screen.refresh();
    Thread.yield();

//      try {
//        Thread.sleep(3000);
//      } catch (InterruptedException e) {
//        Thread.currentThread().interrupt();
//        e.printStackTrace();
//      }
  }


  private String[] getSplash() {
    return new String[]{
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
  }


  private void setTheme(MultiWindowTextGUI gui) {
    Theme theme = SimpleTheme.makeTheme(
        false,
        YELLOW, BLACK,
        YELLOW, BLACK_BRIGHT,
        BLACK, YELLOW,
        BLACK);
    gui.setTheme(theme);
  }


  private void createFakeData(Coordinator coordinator) {
    Faker faker = new Faker(Locale.GERMANY);

    Customer ray = new Customer(1L, "Ray Wojciechowski");
    ray.setDayOfBirth(faker.date().birthday());

    long digits = faker.number().numberBetween(1000000000L, 9999999999L);
    CurrentAccount currentAccount = new CurrentAccount("DE" + digits, ray);
    currentAccount.setLimit(BigDecimal.valueOf(250L));

    digits = faker.number().numberBetween(1000000000L, 9999999999L);
    SavingsAccount savingsAccount = new SavingsAccount("DE" + digits, ray);
    savingsAccount.setInterestRate(0.15);

    coordinator.getCustomerRepository().add(ray);
    coordinator.getAccountRepository().add(currentAccount);
    coordinator.getAccountRepository().add(savingsAccount);


    for (long i = 2; i < 25; i++) {
      String name = faker.name().fullName();
      Date dayOfBirth = faker.date().birthday();

      final Customer customer = new Customer(i, name);
      customer.setDayOfBirth(dayOfBirth);
      coordinator.getCustomerRepository().add(customer);
    }
  }
}
