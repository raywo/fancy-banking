package de.raywo.banking.textui;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.DefaultWindowManager;
import com.googlecode.lanterna.gui2.EmptySpace;
import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import de.raywo.banking.textui.ui.Coordinator;

import java.io.IOException;

public class Main {


  public static void main(String[] args) throws IOException {
    try (Terminal terminal = new DefaultTerminalFactory().createTerminal()) {
      // Setup terminal and screen layers
      Screen screen = new TerminalScreen(terminal);
      screen.startScreen();

      MultiWindowTextGUI gui = new MultiWindowTextGUI(screen,
          new DefaultWindowManager(),
          new EmptySpace(TextColor.ANSI.BLUE));

      Coordinator coordinator = Coordinator.instance();
      coordinator.setGui(gui);

      coordinator.start();

      screen.stopScreen();
    }
  }
}
