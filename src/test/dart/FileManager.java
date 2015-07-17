/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.dart;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author Rice Pavel
 */
public class FileManager {
  
  private boolean exists = false;
  
  private String fileContent = "";
  
  public boolean exists() {
    return exists;
  }
  
  public String getFileContent() {
    return fileContent;
  }
  
  public FileManager(String fileName) throws IOException {
    File currentFile = new File(".");
    String currentFilePath = currentFile.getCanonicalPath();
    String fullPath = currentFilePath + System.getProperty("file.separator") + fileName;
    File file = new File(fullPath);
    exists = file.exists();
    if (exists) {
      String fileExt = getFileType(fileName);
      if (fileExt.equals("html") || fileExt.equals("txt")) {
      fileContent = getFileContent(file);
      } else {
        exists = false;
      }
    }
  }
  
  private String getFileType(String fileName) {
    String[] str = fileName.split(".");
    if (str.length != 0) {
      return str[str.length - 1];
    } else {
      return "";
    }
  }
  
  private String getFileContent(File file) throws FileNotFoundException, IOException {
    String content = "";
    BufferedReader reader = new BufferedReader(new FileReader(file));
    String str;
    while ((str = reader.readLine()) != null) {
      content += str;
      content += "\n";
    }
    return content;
  }
  
}
