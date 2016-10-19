package io.github.antalpeti.main;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import io.github.antalpeti.main.control.ControlElement;

public class Application {

  public static void main(String[] args) {
    Display display = new Display();
    Shell shell = new Shell(display);

    GridLayout gridLayout = new GridLayout(3, true);

    shell.setLayout(gridLayout);
    ControlElement control = ControlElement.getInstance();
    control.initButtons(shell);;
    control.initTexts(shell);

    shell.open();

    while (!shell.isDisposed()) {
      if (!display.readAndDispatch())
        display.sleep();
    }
    display.dispose();
  }
}
