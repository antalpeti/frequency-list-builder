package io.github.antalpeti;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Comparator;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

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

import io.github.antalpeti.constant.TextConstant;

public class Application {

  private static Display display;
  private static Shell shell;
  private static Application application;
  private static Text text;

  public static void main(String[] args) {
    init();
    application = new Application();

    GridLayout gridLayout = new GridLayout(2, true);

    shell.setLayout(gridLayout);
    initButtons();
    initText();

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
    selectFiles.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, true, true, 1, 1));
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
    selectDirectory.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, true, true, 1, 1));
    selectDirectory.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        application.openFileDialogForFiles();
      }
    });
  }

  private static void initText() {
    text = new Text(shell, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
    text.setBackground(display.getSystemColor(SWT.COLOR_BLACK));
    text.setForeground(display.getSystemColor(SWT.COLOR_WHITE));
    GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
    gridData.horizontalSpan = 2;
    text.setLayoutData(gridData);
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
    FileDialog dialog = new FileDialog(shell, SWT.OPEN);
    dialog.setText("Open");
    String[] filterNames = new String[] {"Subtitle Files", "All Files (*)"};
    String[] filterExtensions = new String[] {"*.srt", "*"};
    String filterPath = "/";
    String platform = SWT.getPlatform();
    if (platform.equals("win32")) {
      filterNames = new String[] {"Subtitle Files", "All Files (*.*)"};
      filterExtensions = new String[] {"*.srt", "*.*"};
      filterPath = "c:\\";
    }
    dialog.setFilterNames(filterNames);
    dialog.setFilterExtensions(filterExtensions);
    dialog.setFilterPath(filterPath);
    String pathname = dialog.open();
    String fileContent = readFile(pathname);
    text.setText(fileContent);
  }

  private String readFile(String pathname) {
    StringBuilder contents = new StringBuilder();

    File file = null;
    if (pathname == null) {
      return "No file selected.";
    }

    try {
      file = new File(pathname);

      try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF8"))) {
        String line;
        TreeMap<String, Integer> wordFrequency = new TreeMap<>();

        while ((line = br.readLine()) != null) {
          line = line.trim();

          boolean emptyLine = line.isEmpty();
          boolean srtTime = line.matches("[\\d:,\\s->]*");

          if (!emptyLine && !srtTime) {
            line = line.toLowerCase();
            line = removeBoldItalicTags(line);

            String[] words = line.split("\\s");
            for (String word : words) {
              word = removeBoldItalicTags(word);

              while (word.length() > 0 && word.matches("^[\"\\(\\[\\{]+.*")) {
                word = word.substring(1);
              }
              while (word.length() > 0 && word.matches(".*[\"\\)\\]\\}\\.,\\?!:$]+$")) {
                word = word.substring(0, word.length() - 1);
              }

              Integer occurence = wordFrequency.get(word);
              if (occurence == null) {
                occurence = new Integer(0);
              }
              wordFrequency.put(word, ++occurence);
            }
          }
        }

        int wordNumber = 0;
        for (Map.Entry<String, Integer> entry : entriesSortedByValues(wordFrequency)) {
          String key = entry.getKey();
          Integer value = entry.getValue();
          contents.append(++wordNumber + " " + key + "   " + value + "\n");
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return contents.toString();
  }

  private String removeBoldItalicTags(String text) {
    while (hasBoldItalicOpenTag(text) || hasBoldItalicCloseTag(text)) {
      text = hasBoldItalicOpenTag(text) ? removeBoldItalicOpenTag(text) : text;
      text = hasBoldItalicCloseTag(text) ? removeBoldItalicCloseTag(text) : text;
    }
    return text;
  }

  private boolean hasBoldItalicOpenTag(String text) {
    return text.length() > 3 && text.matches(".*(<i>|<b>)+.*");
  }

  private String removeBoldItalicOpenTag(String text) {
    return text.replaceAll("(<i>|<b>)", TextConstant.EMPTY_STRING);
  }

  private boolean hasBoldItalicCloseTag(String text) {
    return text.length() > 3 && text.matches(".*(<\\/i>|<\\/b>)+.*");
  }

  private String removeBoldItalicCloseTag(String text) {
    return text.replaceAll("(<\\/i>|<\\/b>)", TextConstant.EMPTY_STRING);
  }

  <K, V extends Comparable<? super V>> SortedSet<Map.Entry<K, V>> entriesSortedByValues(Map<K, V> map) {
    SortedSet<Map.Entry<K, V>> sortedEntries = new TreeSet<Map.Entry<K, V>>(new Comparator<Map.Entry<K, V>>() {
      @Override
      public int compare(Map.Entry<K, V> entry1, Map.Entry<K, V> entry2) {
        int result = entry1.getValue().compareTo(entry2.getValue());
        return result != 0 ? -result : 1;
      }
    });
    sortedEntries.addAll(map.entrySet());
    return sortedEntries;
  }
}
