/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.dart;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Map;

/**
 *
 * @author Rice Pavel
 */
public class RequestParser {

  public static final String GET = "GET";
  
  public static final String POST = "POST";
  
  private Socket client;

  private String type;

  private String url;

  private String fileName;

  private Map<String, String> params;

  private Map<String, String> headers;

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public Map<String, String> getParams() {
    return params;
  }
  
  public String getType() {
    return type;
  }

  public void setParams(Map<String, String> params) {
    this.params = params;
  }

  public Map<String, String> getHeaders() {
    return headers;
  }

  public void setHeaders(Map<String, String> headers) {
    this.headers = headers;
  }

  
  
  public RequestParser(Socket client) throws IOException, Exception {
    this.client = client;
    parse();
  }

  private void parse() throws IOException, Exception {
    InputStream is = client.getInputStream();
    BufferedReader br = new BufferedReader(new InputStreamReader(is));
    String firstLine = br.readLine();
    boolean right = false;
    if (firstLine != null) {
      String[] arr = firstLine.split("\\s+");
      if (arr.length == 3) {
        type = arr[0];
        if (!type.equals(GET) && !type.equals(POST)) {
          throw new Exception("Неизвестный тип запроса");
        }
        url = arr[1];
        right = true;
      }
    }
    if (!right) {
      throw new Exception("Неправильный формат запроса");
    }
    if (type.equals("GET")) {
      readGet(br);
      parseGetUrl();
    } else if (type.equals("POST")) {
      readPost(br);
      parsePostUrl();
    }
  }
  
  private void parsePostUrl() {
    this.fileName = cutFileName(url);
  }


  private void readGet(BufferedReader br) throws IOException {
    String str = null;
    while ((str = br.readLine()) != null) {
      String[] strArray = str.split(":");
      if (strArray.length == 2) {
        headers.put(strArray[0].trim(), strArray[1].trim());
      }
    }
  }

  private void parseGetUrl() {
    if (url != null) {
      if (!url.contains("?")) {
        String fileName = url;
        this.fileName = cutFileName(fileName);
      } else {
        String[] arr = url.split("?");
        String fileName = arr[0];
        this.fileName = cutFileName(fileName);
        String paramString = arr[1];
        parseParamsString(paramString);
      }
    }
  }
  
  private void parseParamsString(String paramsString) {
    String[] arr = paramsString.split("&");
    for (String str: arr) {
      String[] keyValue = str.split("=");
      if (keyValue.length == 2) {
        params.put(keyValue[0], keyValue[1]);
      }
    }
  }

  private String cutFileName(String fileName) {
    if (fileName.indexOf("/") == 0) {
      fileName = fileName.substring(1);
    }
    return fileName;
  }

  private void readPost(BufferedReader br) throws IOException {
    String str;
    boolean bodyLine = false;
    while ((str = br.readLine()) != null) {
      if (!bodyLine) {
        if (str.isEmpty()) {
          bodyLine = true;
        } else {
          String[] strArray = str.split(":");
          if (strArray.length == 2) {
            headers.put(strArray[0].trim(), strArray[1].trim());
          }
        }
      } else {
        parseBodyLine(str);
        break;
      }
    }
  }

  private void parseBodyLine(String str) {
    parseParamsString(str);
  }

}
