package io.github.antalpeti.main;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import io.github.antalpeti.main.ui.MainWindow;

public class FrequencyListBuilder {

  public static void main(String[] args) {
    Display display = new Display();
    Shell shell = new Shell(display);
    shell.setText("Frequency List Builder");
    GridLayout gridLayout = new GridLayout(4, true);

    shell.setLayout(gridLayout);
    MainWindow control = MainWindow.getInstance();
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
