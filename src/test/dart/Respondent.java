/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.dart;

import java.net.Socket;
import java.util.Map;

/**
 *
 * @author Rice Pavel
 */
public class Respondent implements Runnable {

  private final Socket client;

  public Respondent(Socket client) {
    this.client = client;
  }

  public void run() {
    try {
      RequestParser parser = new RequestParser(client);
      String fileName = parser.getFileName();
      Map<String, String> params = parser.getParams();
      String type = parser.getType();
      FileManager fileManager = new FileManager(fileName);
      if (fileManager.exists()) {
        String fileContent = fileManager.getFileContent();
        fileContent = replaceParams(fileContent, params, type);
        Response response = new Response(client);
        DataType dataType = fileManager.getDataType();
        response.getFileContent(fileContent, dataType);
      } else {
        Response response = new Response(client);
        response.getErrorCode();
      }
    } catch (Exception e) {
      e.printStackTrace(System.err);
    } finally {
      try {
        client.close();
      } catch (Exception e) {

      }
    }
  }

  private String replaceParams(String content, Map<String, String> params, String type) {
    for (String key : params.keySet()) {
      String value = params.get(key);
      if (value != null) {
        if (type.equals(RequestParser.GET)) {
          content = content.replace("<get." + key + ">", value);
        } else if (type.equals(RequestParser.POST)) {
          content = content.replace("<post." + key + ">", value);
        }
      }
    }
    return content;
  }

}
