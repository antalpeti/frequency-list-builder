package io.github.antalpeti.main.control;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import io.github.antalpeti.file.FileHandler;
import io.github.antalpeti.file.WordData;

public class ControlElement {
  private static ControlElement instance = null;
  private Shell shell;
  private Text log;
  private Text console;
  private Button files;
  private Button export;

  private ControlElement() {}

  public static ControlElement getInstance() {
    if (instance == null) {
      instance = new ControlElement();
    }
    return instance;
  }

  public void initButtons(Shell shell) {
    this.shell = shell;
    initFilesButton();
    initDirectoryButton();
    initExportButton();
  }

  private void initFilesButton() {
    files = new Button(shell, SWT.PUSH);
    files.setText("File(s)");
    files.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
    files.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        openFileDialogForFiles();
      }
    });
    setFontSize(files, 20);
    shell.setDefaultButton(files);
  }

  private void setFontSize(Control control, int size) {
    FontData[] fontData = control.getFont().getFontData();
    fontData[0].setHeight(size);
    control.setFont(new Font(control.getDisplay(), fontData[0]));
  }

  private void openFileDialogForFiles() {
    FileDialog fileDialog = new FileDialog(shell, SWT.OPEN | SWT.MULTI);
    fileDialog.setText("Open");
    String[] filterNames = new String[] {"Subtitle Files", "All Files (*)"};
    String[] filterExtensions = new String[] {"*.srt", "*"};
    String filterPath = "/";
    String platform = SWT.getPlatform();
    if (platform.equals("win32")) {
      filterNames = new String[] {"Subtitle Files", "All Files (*.*)"};
      filterExtensions = new String[] {"*.srt", "*.*"};
      filterPath = "c:\\";
    }
    fileDialog.setFilterNames(filterNames);
    fileDialog.setFilterExtensions(filterExtensions);
    fileDialog.setFilterPath(filterPath);
    fileDialog.open();
    String directoryPath = fileDialog.getFilterPath();
    String[] fileNames = fileDialog.getFileNames();

    WordData wordData = new WordData();
    wordData = FileHandler.getInstance().processFiles(directoryPath, fileNames, wordData);
    export.setEnabled(wordData.getWordFrequency().size() > 0);

    if (wordData.getContents().toString().isEmpty()) {
      log.setText(log.getText() + "\n" + "No selection or empty file.");
    } else {
      console.setText(wordData.getContents().toString());
      log.setText(log.getText() + "\n" + wordData.getLogText());
    }
  }

  private void initDirectoryButton() {
    Button directory = new Button(shell, SWT.PUSH);
    directory.setText("Directory");
    directory.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
    directory.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        openFileDialogForFiles();
      }
    });
    setFontSize(directory, 20);
  }

  private void openFileDialogForExport() {
    FileDialog fileDialog = new FileDialog(shell, SWT.SAVE);
    fileDialog.setText("Save");
    String[] filterNames = new String[] {"Comma Separated Values"};
    String[] filterExtensions = new String[] {"*.csv"};
    String filterPath = "/";
    String platform = SWT.getPlatform();
    if (platform.equals("win32")) {
      filterNames = new String[] {"Comma Separated Values"};
      filterExtensions = new String[] {"*.csv"};
      filterPath = "c:\\";
    }
    fileDialog.setFilterNames(filterNames);
    fileDialog.setFilterExtensions(filterExtensions);
    fileDialog.setFilterPath(filterPath);
    fileDialog.setFileName("myfile");
    String fileName = fileDialog.open();

    if (fileName == null) {
      return;
    }
    PrintWriter writer;
    try {
      writer = new PrintWriter(fileName, "UTF-8");
      String content = console.getText();
      String[] lines = content.split("\\n");
      for (String line : lines) {
        line = line.replaceAll("\\s", ",");
        writer.println(line);
      }
      writer.close();
      log.setText(log.getText() + "\n" + "Export to " + fileName + ".");
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
  }

  private void initExportButton() {
    export = new Button(shell, SWT.PUSH);
    export.setText("Export");
    export.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
    export.setEnabled(false);
    export.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        openFileDialogForExport();
      }
    });
    setFontSize(export, 20);
  }


  public void initTexts(Shell shell) {
    this.shell = shell;
    initConsole();
    initLog();
  }

  private void initConsole() {
    console = new Text(shell, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
    console.setBackground(console.getDisplay().getSystemColor(SWT.COLOR_BLACK));
    console.setForeground(console.getDisplay().getSystemColor(SWT.COLOR_WHITE));
    GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
    gridData.horizontalSpan = 3;
    console.setLayoutData(gridData);
    setFontSize(console, 20);
  }

  private void initLog() {
    log = new Text(shell, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
    log.setBackground(log.getDisplay().getSystemColor(SWT.COLOR_GRAY));
    log.setForeground(log.getDisplay().getSystemColor(SWT.COLOR_BLUE));
    GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
    gridData.horizontalSpan = 3;
    log.setLayoutData(gridData);
    setFontSize(log, 20);
  }
}
