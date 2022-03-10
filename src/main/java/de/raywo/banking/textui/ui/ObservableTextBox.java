//package de.raywo.banking.textui.ui;
//
//import com.googlecode.lanterna.TerminalSize;
//import com.googlecode.lanterna.gui2.Interactable;
//import com.googlecode.lanterna.gui2.TextBox;
//
//import java.beans.PropertyChangeListener;
//import java.beans.PropertyChangeSupport;
//
//public class ObservableTextBox extends TextBox {
//
//  private final PropertyChangeSupport changeSupport;
//
//
//  public ObservableTextBox() {
//    changeSupport = new PropertyChangeSupport(this);
//  }
//
//
//  public ObservableTextBox(String initialContent) {
//    super(initialContent);
//    changeSupport = new PropertyChangeSupport(this);
//  }
//
//
//  public ObservableTextBox(String initialContent, Style style) {
//    super(initialContent, style);
//    changeSupport = new PropertyChangeSupport(this);
//  }
//
//
//  public ObservableTextBox(TerminalSize preferredSize) {
//    super(preferredSize);
//    changeSupport = new PropertyChangeSupport(this);
//  }
//
//
//  public ObservableTextBox(TerminalSize preferredSize, Style style) {
//    super(preferredSize, style);
//    changeSupport = new PropertyChangeSupport(this);
//  }
//
//
//  public ObservableTextBox(TerminalSize preferredSize, String initialContent) {
//    super(preferredSize, initialContent);
//    changeSupport = new PropertyChangeSupport(this);
//  }
//
//
//  public ObservableTextBox(TerminalSize preferredSize, String initialContent, Style style) {
//    super(preferredSize, initialContent, style);
//    changeSupport = new PropertyChangeSupport(this);
//  }
//
//
//  public void addListener(PropertyChangeListener listener) {
//    changeSupport.addPropertyChangeListener(listener);
//  }
//
//
//  public void removeListener(PropertyChangeListener listener) {
//    changeSupport.removePropertyChangeListener(listener);
//  }
//
//
//  @Override
//  protected void afterLeaveFocus(FocusChangeDirection direction, Interactable nextInFocus) {
//    changeSupport.firePropertyChange("text", this.getText() + "1", this.getText());
//  }
//}
