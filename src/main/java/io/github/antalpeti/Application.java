package io.github.antalpeti;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

public class Application {

  private static Display display;
  private static Shell shell;

  public static void main(String[] args) {
    init();
    Application application = new Application();

    GridLayout layout = new GridLayout(2, false);
    shell.setLayout(layout);

    initButtons(application);

    render();

    dispose();
  }

  private static void initButtons(Application application) {
    initDirectoryButton(application);
  }

  private static void initDirectoryButton(Application application) {
    Button selectFiles = new Button(shell, SWT.PUSH);
    selectFiles.setText("File(s)");
    selectFiles.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        application.openFileDialogForDirectory(shell);
      }
    });
    shell.setDefaultButton(selectFiles);
  }

  private static void render() {
    shell.pack();
    shell.open();
  }

  private static void init() {
    display = new Display();
    shell = new Shell(display);
  }

  private static void dispose() {
    while (!shell.isDisposed()) {
      if (!display.readAndDispatch())
        display.sleep();
    }
    display.dispose();
  }

  private void openFileDialogForDirectory(Shell shell) {
    FileDialog dialog = new FileDialog(shell, SWT.SAVE);
    String[] filterNames = new String[] {"Subtitle Files", "All Files (*)"};
    String[] filterExtensions = new String[] {"*.srt;*.sub", "*"};
    String filterPath = "/";
    String platform = SWT.getPlatform();
    if (platform.equals("win32")) {
      filterNames = new String[] {"Subtitle Files", "All Files (*.*)"};
      filterExtensions = new String[] {"*.srt;*.sub", "*.*"};
      filterPath = "c:\\";
    }
    dialog.setFilterNames(filterNames);
    dialog.setFilterExtensions(filterExtensions);
    dialog.setFilterPath(filterPath);
    dialog.setFileName("myfile");
    System.out.println("Save to: " + dialog.open());
  }
}
