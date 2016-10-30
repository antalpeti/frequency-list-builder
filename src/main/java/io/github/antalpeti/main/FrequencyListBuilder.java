package io.github.antalpeti.main;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import io.github.antalpeti.main.ui.MainWindow;

public class FrequencyListBuilder {

  public static void main(String[] args) {
    Display display = new Display();
    Shell shell = new Shell(display, SWT.CLOSE | SWT.TITLE);
    shell.setLayout(new GridLayout(1, true));
    shell.setText("Frequency List Builder");
    MainWindow mainWindow = MainWindow.getInstance();
    mainWindow.init(shell);
    shell.open();

    while (!shell.isDisposed()) {
      if (!display.readAndDispatch())
        display.sleep();
    }
    display.dispose();
  }
}
