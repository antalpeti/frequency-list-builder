package io.github.antalpeti;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedSet;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import io.github.antalpeti.constant.DirectionOrder;
import io.github.antalpeti.util.CharacterRemover;
import io.github.antalpeti.util.TagRemover;
import io.github.antalpeti.util.Util;

public class Application {

  private static Display display;
  private static Shell shell;
  private static Application application;
  private static Text console;
  private static Text status;

  public static void main(String[] args) {
    application = new Application();
    application.init();

    GridLayout gridLayout = new GridLayout(3, true);

    shell.setLayout(gridLayout);
    application.initButtons();
    application.initConsole();
    application.initStatus();

    application.render();

    application.dispose();
  }

  private void initButtons() {
    initFilesButton();
    initDirectoryButton();
    initExportButton();
  }

  private void initFilesButton() {
    Button selectFiles = new Button(shell, SWT.PUSH);
    selectFiles.setText("File(s)");
    selectFiles.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
    selectFiles.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        application.openFileDialogForFiles();
      }
    });
    shell.setDefaultButton(selectFiles);
  }

  private void initDirectoryButton() {
    Button selectDirectory = new Button(shell, SWT.PUSH);
    selectDirectory.setText("Directory");
    selectDirectory.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
    selectDirectory.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        application.openFileDialogForFiles();
      }
    });
  }

  private void initExportButton() {
    Button selectDirectory = new Button(shell, SWT.PUSH);
    selectDirectory.setText("Export");
    selectDirectory.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
    selectDirectory.setEnabled(exportEnabled);
    selectDirectory.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        application.openFileDialogForExport();
      }
    });
  }

  private void initConsole() {
    console = new Text(shell, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
    console.setBackground(display.getSystemColor(SWT.COLOR_BLACK));
    console.setForeground(display.getSystemColor(SWT.COLOR_WHITE));
    GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
    gridData.horizontalSpan = 3;
    console.setLayoutData(gridData);
  }

  private void initStatus() {
    status = new Text(shell, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
    status.setBackground(display.getSystemColor(SWT.COLOR_GRAY));
    status.setForeground(display.getSystemColor(SWT.COLOR_BLUE));
    GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
    gridData.horizontalSpan = 3;
    status.setLayoutData(gridData);
  }

  private void render() {
    shell.open();
  }

  private void init() {
    display = new Display();
    shell = new Shell(display);
  }

  private void dispose() {
    while (!shell.isDisposed()) {
      if (!display.readAndDispatch())
        display.sleep();
    }
    display.dispose();
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
    wordData = readFiles(directoryPath, fileNames, wordData);

    console.setText(wordData.getContents().toString());
    status.setText(wordData.getStatusText());
  }

  private WordData readFiles(String directoryPath, String[] fileNames, WordData wordData) {
    String separator = File.separator;
    if (fileNames.length == 0) {
      wordData.getContents().append("No file selected!");
    }
    for (String filename : fileNames) {
      String pathname = directoryPath + separator + filename;
      readFile(pathname, wordData);
    }
    sortMap(wordData);
    return wordData;
  }


  private void readFile(String pathname, WordData wordData) {
    File file = null;
    try {
      file = new File(pathname);

      try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF8"))) {
        String line;

        TagRemover tagRemover = TagRemover.getInstance();
        CharacterRemover characterRemover = CharacterRemover.getInstance();

        while ((line = br.readLine()) != null) {
          line = line.trim();

          boolean emptyLine = line.isEmpty();
          boolean srtTimeLine = line.matches("[\\d:,\\s->]*");

          if (!emptyLine && !srtTimeLine) {
            line = line.toLowerCase();
            line = tagRemover.removeBoldItalicFontOpenAndCloseTags(line);

            String[] words = line.split("\\s");
            for (String word : words) {
              word = tagRemover.removeBoldItalicFontOpenAndCloseTags(word);

              word = characterRemover.removeFrontAndBackSpecialCharacters(word);

              word = characterRemover.removeOnlyHypenCharacter(word);

              if (word.isEmpty()) {
                continue;
              }
              Integer occurence = wordData.getWordFrequency().get(word);
              if (occurence == null) {
                occurence = new Integer(0);
              }
              wordData.setProcessedWordNumber(wordData.getProcessedWordNumber() + 1);
              wordData.getWordFrequency().put(word, ++occurence);
            }
          }
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void sortMap(WordData wordData) {
    int individualWordNumber = 0;;
    SortedSet<Entry<String, Integer>> entriesSortedByValues =
        Util.getInstance().entriesSortedByValues(wordData.getWordFrequency(), DirectionOrder.DESCENDING);
    for (Map.Entry<String, Integer> entry : entriesSortedByValues) {
      ++individualWordNumber;
      String key = entry.getKey();
      Integer value = entry.getValue();
      wordData.getContents().append(key + " " + value + "\n");
    }
    exportEnabled = entriesSortedByValues.size() > 0;
    wordData.setIndividualWordNumber(individualWordNumber);
  }

  boolean exportEnabled = false;

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

    PrintWriter writer;
    try {
      writer = new PrintWriter(fileName, "UTF-8");
      writer.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
  }


}
