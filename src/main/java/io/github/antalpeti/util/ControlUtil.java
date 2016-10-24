package io.github.antalpeti.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.RegexFileFilter;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

import io.github.antalpeti.constant.ConfigConstant;

public class ControlUtil {
  private static ControlUtil instance = null;

  private ControlUtil() {}

  public static ControlUtil getInstance() {
    if (instance == null) {
      instance = new ControlUtil();
    }
    return instance;
  }

  /**
   * Modify the font size of the given control.
   * 
   * @param control the control
   * @param size the new size
   */
  public void setFontSize(Control control, int size) {
    FontData[] fontData = control.getFont().getFontData();
    fontData[0].setHeight(size);
    control.setFont(new Font(control.getDisplay(), fontData[0]));
  }

  /**
   * Collect the names of the subtitle files recursively inside the given directory path.
   * 
   * @param directoryPath the directory path
   * @return the collected names of the subtitle files
   */
  public String[] collectSubtitleFileNamesRecursively(String directoryPath) {
    if (directoryPath == null) {
      return new String[] {};
    }
    Collection<File> files = FileUtils.listFiles(new File(directoryPath), new RegexFileFilter("^(.*\\.srt?)"),
        DirectoryFileFilter.DIRECTORY);
    String[] fileNames = new String[files.size()];
    int index = 0;
    for (File file : files) {
      fileNames[index++] = file.toString();
    }
    return fileNames;
  }

  /**
   * Add message to the log view.
   * 
   * @param log the log view
   * @param message the message
   */
  public void addLogMessage(Text log, String message) {
    if (!log.getText().isEmpty()) {
      log.append("\n");
    }
    if (message != null && !message.isEmpty()) {
      log.append(message);
    }
  }

  /**
   * Write the content of the console to a file and update log view.
   * 
   * @param fileName the name of the file
   * @param console the console view
   * @param log the log view
   */
  public void writeContentToFile(String fileName, Text console, Text log) {
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
      addLogMessage(log, "Content write to " + fileName + ".");
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
  }

  public String loadConfigProperties(ConfigConstant key) {
    Properties properties = new Properties();
    InputStream input = null;
    File file = null;

    try {
      file = new File("config.properties");
      input = new FileInputStream(file);

      // load a properties file
      properties.load(input);

      // get the property value and print it out
      return properties.getProperty(key.getValue());
    } catch (FileNotFoundException exception) {
      System.out.println("The file " + file.getPath() + " was not found.");
    } catch (IOException ex) {
      ex.printStackTrace();
    } finally {
      if (input != null) {
        try {
          input.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    return null;
  }

  public void storeConfigProperties(ConfigConstant key, String value) {
    if (value == null) {
      return;
    }
    Properties properties = loadAllConfigProperties();
    OutputStream output = null;

    try {

      output = new FileOutputStream("config.properties");

      // set the properties value
      properties.setProperty(key.getValue(), value);

      // save properties to project root folder
      properties.store(output, null);

    } catch (IOException io) {
      io.printStackTrace();
    } finally {
      if (output != null) {
        try {
          output.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }

  private Properties loadAllConfigProperties() {

    Properties properties = new Properties();
    InputStream input = null;
    File file = null;

    try {

      file = new File("config.properties");
      input = new FileInputStream(file);

      properties.load(input);
    } catch (FileNotFoundException exception) {
      System.out.println("The file " + file.getPath() + " was not found.");
    } catch (IOException ex) {
      ex.printStackTrace();
    } finally {
      if (input != null) {
        try {
          input.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    return properties;
  }
}
