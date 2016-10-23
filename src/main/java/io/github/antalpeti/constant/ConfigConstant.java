package io.github.antalpeti.constant;

public enum ConfigConstant {
  FILES_BUTTON_LAST_SELECTED_DIRECTORY_WIN32(
      "files_button_last_selected_directory_win32"), FILES_BUTTON_LAST_SELECTED_DIRECTORY(
          "files_button_last_selected_directory"), DIRECTORY_BUTTON_LAST_SELECTED_DIRECTORY_WIN32(
              "directory_button_last_selected_directory_win32"), DIRECTORY_BUTTON_LAST_SELECTED_DIRECTORY(
                  "directory_button_last_selected_directory"), EXPORT_BUTTON_LAST_SELECTED_DIRECTORY_WIN32(
                      "export_button_last_selected_directory_win32"), EXPORT_BUTTON_LAST_SELECTED_DIRECTORY(
                          "export_button_last_selected_directory");

  private final String value;

  ConfigConstant(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
