package io.github.antalpeti.main.ui;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class ConfigurationWindow {

  private static ConfigurationWindow instance = null;

  private Shell shell;


  private ConfigurationWindow() {
    shell = new Shell(Display.getCurrent());
    shell.setText("Configuration");
    GridLayout gridLayout = new GridLayout();
    gridLayout.numColumns = 1;
    shell.setLayout(gridLayout);
  }

  public static ConfigurationWindow getInstance() {
    if (instance == null) {
      instance = new ConfigurationWindow();
    }
    return instance;
  }

  public void open() {
    shell.open();
  }

  public void close() {
    shell.setVisible(false);
  }
}
