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

import io.github.antalpeti.constant.ConfigConstant;
import io.github.antalpeti.constant.PlatformConstant;
import io.github.antalpeti.file.WordData;
import io.github.antalpeti.util.ControlUtil;
import io.github.antalpeti.util.WordUtil;

public class MainWindow {

  private static MainWindow instance = null;

  private Shell shell;
  private Button export;
  private Text log;
  private Text console;

  private ControlUtil controlUtil;

  private String directoryPath;

  private String[] fileNames;


  private MainWindow() {}

  public static MainWindow getInstance() {
    if (instance == null) {
      instance = new MainWindow();
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
    initConfigurationWindow();
    initConfigButton();
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
    String platform = SWT.getPlatform();
    loadFilesButtonProperties(fileDialog, platform);
    fileDialog.open();
    directoryPath = fileDialog.getFilterPath();
    fileNames = fileDialog.getFileNames();
    storeFilesButtonProperties(platform);
  }

  private void loadFilesButtonProperties(FileDialog fileDialog, String platform) {
    String[] filterNames = new String[] {"Subtitle Files", "All Files (*)"};
    String[] filterExtensions = new String[] {"*.srt", "*"};
    String lastSelectedDirectory =
        controlUtil.loadConfigProperties(ConfigConstant.FILES_BUTTON_LAST_SELECTED_DIRECTORY);
    String filterPath =
        lastSelectedDirectory == null ? PlatformConstant.ROOT_DIRECTORY.getValue() : lastSelectedDirectory;
    if (platform.equals(PlatformConstant.WIN32.getValue())) {
      filterNames = new String[] {"Subtitle Files", "All Files (*.*)"};
      filterExtensions = new String[] {"*.srt", "*.*"};
      lastSelectedDirectory =
          controlUtil.loadConfigProperties(ConfigConstant.FILES_BUTTON_LAST_SELECTED_DIRECTORY_WIN32);
      filterPath =
          lastSelectedDirectory == null ? PlatformConstant.ROOT_DIRECTORY_WIN32.getValue() : lastSelectedDirectory;
    }
    fileDialog.setFilterNames(filterNames);
    fileDialog.setFilterExtensions(filterExtensions);
    fileDialog.setFilterPath(filterPath);
  }

  private void storeFilesButtonProperties(String platform) {
    if (platform.equals(PlatformConstant.WIN32.getValue())) {
      ControlUtil.getInstance().storeConfigProperties(ConfigConstant.FILES_BUTTON_LAST_SELECTED_DIRECTORY_WIN32,
          directoryPath);
    } else {
      ControlUtil.getInstance().storeConfigProperties(ConfigConstant.FILES_BUTTON_LAST_SELECTED_DIRECTORY,
          directoryPath);
    }
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
    String platform = SWT.getPlatform();
    loadDirectoryButtonLastSelectedDirectory(platform, directoryDialog);

    String directoryPath = directoryDialog.open();
    fileNames = controlUtil.collectSubtitleFileNamesRecursively(directoryPath);
    storeDirectoryButtonLastSelectedDirectory(platform, directoryPath);
  }

  private void loadDirectoryButtonLastSelectedDirectory(String platform, DirectoryDialog directoryDialog) {
    String lastSelectedDirectory =
        ControlUtil.getInstance().loadConfigProperties(ConfigConstant.DIRECTORY_BUTTON_LAST_SELECTED_DIRECTORY);
    String filterPath =
        lastSelectedDirectory == null ? PlatformConstant.ROOT_DIRECTORY.getValue() : lastSelectedDirectory;
    if (platform.equals(PlatformConstant.WIN32.getValue())) {
      lastSelectedDirectory =
          ControlUtil.getInstance().loadConfigProperties(ConfigConstant.DIRECTORY_BUTTON_LAST_SELECTED_DIRECTORY_WIN32);
      filterPath =
          lastSelectedDirectory == null ? PlatformConstant.ROOT_DIRECTORY_WIN32.getValue() : lastSelectedDirectory;
    }
    directoryDialog.setFilterPath(filterPath);
  }

  private void storeDirectoryButtonLastSelectedDirectory(String platform, String directoryPath) {
    if (platform.equals(PlatformConstant.WIN32.getValue())) {
      ControlUtil.getInstance().storeConfigProperties(ConfigConstant.DIRECTORY_BUTTON_LAST_SELECTED_DIRECTORY_WIN32,
          directoryPath);
    } else {
      ControlUtil.getInstance().storeConfigProperties(ConfigConstant.DIRECTORY_BUTTON_LAST_SELECTED_DIRECTORY,
          directoryPath);
    }
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
    String platform = SWT.getPlatform();
    loadExportButtonProperties(fileDialog, platform);
    String fileName = fileDialog.open();

    if (fileName == null) {
      return;
    }
    directoryPath = fileDialog.getFilterPath();
    controlUtil.writeContentToFile(fileName, console, log);
    storeExportButtonProperties(platform);
  }

  private void loadExportButtonProperties(FileDialog fileDialog, String platform) {
    String[] filterNames = new String[] {"Comma Separated Values"};
    String[] filterExtensions = new String[] {"*.csv"};
    String lastSelectedDirectory =
        controlUtil.loadConfigProperties(ConfigConstant.EXPORT_BUTTON_LAST_SELECTED_DIRECTORY);
    String filterPath =
        lastSelectedDirectory == null ? PlatformConstant.ROOT_DIRECTORY.getValue() : lastSelectedDirectory;
    if (platform.equals(PlatformConstant.WIN32.getValue())) {
      filterNames = new String[] {"Comma Separated Values"};
      filterExtensions = new String[] {"*.csv"};
      lastSelectedDirectory =
          controlUtil.loadConfigProperties(ConfigConstant.EXPORT_BUTTON_LAST_SELECTED_DIRECTORY_WIN32);
      filterPath =
          lastSelectedDirectory == null ? PlatformConstant.ROOT_DIRECTORY_WIN32.getValue() : lastSelectedDirectory;
    }
    fileDialog.setFilterNames(filterNames);
    fileDialog.setFilterExtensions(filterExtensions);
    fileDialog.setFilterPath(filterPath);
    fileDialog.setFileName("export");
  }

  private void storeExportButtonProperties(String platform) {
    if (platform.equals(PlatformConstant.WIN32.getValue())) {
      ControlUtil.getInstance().storeConfigProperties(ConfigConstant.EXPORT_BUTTON_LAST_SELECTED_DIRECTORY_WIN32,
          directoryPath);
    } else {
      ControlUtil.getInstance().storeConfigProperties(ConfigConstant.EXPORT_BUTTON_LAST_SELECTED_DIRECTORY,
          directoryPath);
    }
  }

  ConfigurationWindow configShell;

  /**
   * Initalize Configuration window.
   */
  private void initConfigurationWindow() {
    configShell = ConfigurationWindow.getInstance();
  }

  /**
   * Initialize Configuration button.
   */
  private void initConfigButton() {
    Button configuration = new Button(shell, SWT.PUSH);
    configuration.setText("Configuration");
    configuration.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

    configuration.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        configShell.open();
        // openFileDialog();
        // WordData wordData = processSelectedFiles();
        // setExportButtonEnabledState(wordData);
        // addFilesButtonLogMessage(wordData);
      }
    });
    controlUtil.setFontSize(configuration, 20);
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
    gridData.horizontalSpan = 4;
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
    gridData.horizontalSpan = 4;
    log.setLayoutData(gridData);
    controlUtil.setFontSize(log, 20);
  }
}

