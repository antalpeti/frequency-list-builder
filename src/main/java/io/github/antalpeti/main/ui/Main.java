package io.github.antalpeti.main.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import io.github.antalpeti.file.WordData;
import io.github.antalpeti.util.ControlUtil;
import io.github.antalpeti.util.WordUtil;

public class Main {
  private static Main instance = null;

  private Shell shell;
  private Button export;
  private Text log;
  private Text console;

  private ControlUtil controlUtil;

  private String directoryPath;

  private String[] fileNames;


  private Main() {}

  public static Main getInstance() {
    if (instance == null) {
      instance = new Main();
    }
    return instance;
  }

  /**
   * Initialize the buttons.
   * 
   * @param shell the shell
   */
  public void initButtons(Shell shell) {
    this.shell = shell;
    controlUtil = ControlUtil.getInstance();
    initFilesButton();
    initDirectoryButton();
    initExportButton();
  }

  /**
   * Initialize File(s) button.
   */
  private void initFilesButton() {
    Button files = new Button(shell, SWT.PUSH);
    files.setText("File(s)");
    files.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
    files.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        openFileDialog();
        WordData wordData = processSelectedFiles();
        setExportButtonEnabledState(wordData);
        addFilesButtonLogMessage(wordData);
      }
    });
    controlUtil.setFontSize(files, 20);
    shell.setDefaultButton(files);
  }

  /**
   * Open the "Select File(s)" dialog.
   */
  private void openFileDialog() {
    FileDialog fileDialog = new FileDialog(shell, SWT.OPEN | SWT.MULTI);
    fileDialog.setText("Select File(s)");
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
    directoryPath = fileDialog.getFilterPath();
    fileNames = fileDialog.getFileNames();
  }

  /**
   * Process the content of selected files.
   * 
   * @return the related word data
   */
  private WordData processSelectedFiles() {
    WordData wordData = new WordData();
    wordData = WordUtil.getInstance().processFiles(directoryPath, fileNames, wordData, log);
    return wordData;
  }

  /**
   * Enable or disable the export button.
   * 
   * @param wordData the related word data
   */
  private void setExportButtonEnabledState(WordData wordData) {
    export.setEnabled(wordData.getWordFrequency().size() > 0 || !console.getText().isEmpty());
  }

  /**
   * Add log message according to the File(s) button.
   * 
   * @param wordData the related word data
   */
  private void addFilesButtonLogMessage(WordData wordData) {
    if (wordData.getContents().toString().isEmpty()) {
      controlUtil.addLogMessage(log, "No selected file(s) or empty file(s).");
    } else {
      console.setText(wordData.getContents().toString());
      controlUtil.addLogMessage(log, wordData.getAfterProcessMessage());
    }
  }

  /**
   * Initialize Directory button.
   */
  private void initDirectoryButton() {
    Button directory = new Button(shell, SWT.PUSH);
    directory.setText("Directory");
    directory.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
    directory.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        openDirectoryDialog();
        WordData wordData = processSelectedDirectory();
        setExportButtonEnabledState(wordData);
        addFilesButtonLogMessage(wordData);
      }
    });
    controlUtil.setFontSize(directory, 20);
  }

  /**
   * Open the "Select Directory" dialog.
   */
  private void openDirectoryDialog() {
    DirectoryDialog directoryDialog = new DirectoryDialog(shell, SWT.OPEN | SWT.MULTI);
    directoryDialog.setText("Select Directory");
    String filterPath = "/";
    String platform = SWT.getPlatform();
    if (platform.equals("win32")) {
      filterPath = "c:\\";
    }
    directoryDialog.setFilterPath(filterPath);
    String directoryPath = directoryDialog.open();

    fileNames = controlUtil.collectSubtitleFileNamesRecursively(directoryPath);
  }

  /**
   * Process the content of selected directory.
   * 
   * @return the related word data
   */
  private WordData processSelectedDirectory() {
    WordData wordData = new WordData();
    wordData = WordUtil.getInstance().processFiles("", fileNames, wordData, log);
    return wordData;
  }

  /**
   * Initialize Export button.
   */
  private void initExportButton() {
    export = new Button(shell, SWT.PUSH);
    export.setText("Export");
    export.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
    export.setEnabled(false);
    export.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        openExportDialog();
      }
    });
    controlUtil.setFontSize(export, 20);
  }

  /**
   * Open the "Export" dialog.
   */
  private void openExportDialog() {
    FileDialog fileDialog = new FileDialog(shell, SWT.SAVE);
    fileDialog.setText("Export");
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
    controlUtil.writeContentToFile(fileName, console, log);
  }

  /**
   * Initialize the texts views.
   * 
   * @param shell the shell
   */
  public void initTexts(Shell shell) {
    this.shell = shell;
    initConsole();
    initLog();
  }

  /**
   * Initialize the console view.
   */
  private void initConsole() {
    console = new Text(shell, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
    console.setBackground(console.getDisplay().getSystemColor(SWT.COLOR_BLACK));
    console.setForeground(console.getDisplay().getSystemColor(SWT.COLOR_WHITE));
    GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
    gridData.horizontalSpan = 3;
    console.setLayoutData(gridData);
    controlUtil.setFontSize(console, 20);
  }

  /**
   * Initialize the log view.
   */
  private void initLog() {
    log = new Text(shell, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
    log.setBackground(log.getDisplay().getSystemColor(SWT.COLOR_GRAY));
    log.setForeground(log.getDisplay().getSystemColor(SWT.COLOR_BLUE));
    GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
    gridData.horizontalSpan = 3;
    log.setLayoutData(gridData);
    controlUtil.setFontSize(log, 20);
  }
}

