package io.github.antalpeti;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
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
    init();
    application = new Application();

    GridLayout gridLayout = new GridLayout(2, true);

    shell.setLayout(gridLayout);
    initButtons();
    initConsole();
    initStatus();

    render();

    dispose();
  }

  private static void initButtons() {
    initFilesButton();
    initDirectoryButton();
  }

  private static void initFilesButton() {
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

  private static void initDirectoryButton() {
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

  private static void initConsole() {
    console = new Text(shell, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
    console.setBackground(display.getSystemColor(SWT.COLOR_BLACK));
    console.setForeground(display.getSystemColor(SWT.COLOR_WHITE));
    GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
    gridData.horizontalSpan = 2;
    console.setLayoutData(gridData);
  }

  private static void initStatus() {
    status = new Text(shell, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
    status.setBackground(display.getSystemColor(SWT.COLOR_GRAY));
    status.setForeground(display.getSystemColor(SWT.COLOR_BLUE));
    GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
    gridData.horizontalSpan = 2;
    status.setLayoutData(gridData);

  }

  private static void render() {
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
    wordData.setIndividualWordNumber(individualWordNumber);
  }

}
