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
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Rice Pavel
 */
public class RequestParser {

  public static final String GET = "GET";

  public static final String POST = "POST";

  private Socket client;

  private String type = "";

  private String url = "";

  private String fileName = "";

  private Map<String, String> params = new HashMap();

  private Map<String, String> headers = new HashMap();

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
    BufferedReader br = new BufferedReader(new InputStreamReader(is, "ISO-8859-1"));
    String firstLine = br.readLine();
    boolean right = false;
    if (firstLine != null) {
      String[] arr = firstLine.split("\\s+");
      if (arr.length == 3) {
        type = arr[0];
        if (!type.equals(GET) && !type.equals(POST)) {
          throw new Exception("Неизвестный тип запроса");
        }
        url = decodeUrl(arr[1]);
        right = true;
      }
    }
    if (!right) {
      //throw new Exception("Неправильный формат запроса");
      return;
    }
    if (type.equals("GET")) {
      readGet(br);
      parseGetUrl();
    } else if (type.equals("POST")) {
      readPost(br);
      parsePostUrl();
    }
  }
  
  private String decodeUrl(String url) throws UnsupportedEncodingException {
    return java.net.URLDecoder.decode(url, "UTF-8");
  }

  private void parsePostUrl() {
    this.fileName = cutFileName(url);
  }

  private void readGet(BufferedReader br) throws IOException {
    for (String str = br.readLine(); str != null && str.length() > 0; str = br.readLine()) {
      addHeader(str);
    }
  }

  private void addHeader(String headerLine) {
    int i = headerLine.indexOf(":");
    if (i > 0) {
      String name = headerLine.substring(0, i);
      String value = headerLine.substring(i + 1);
      headers.put(name.trim(), value.trim());
    }
  }

  private void parseGetUrl() {
    if (url != null) {
      if (!url.contains("?")) {
        String fileName = url;
        this.fileName = cutFileName(fileName);
      } else {
        String[] arr = url.split("\\?");
        String fileName = arr[0];
        this.fileName = cutFileName(fileName);
        String paramString = arr[1];
        parseParamsString(paramString);
      }
    }
  }

  private void parseParamsString(String paramsString) {
    String[] arr = paramsString.split("&");
    for (String str : arr) {
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
    for (String str = br.readLine(); str != null && str.length() > 0; str = br.readLine()) {
      addHeader(str);
    }
    int contentLength = getContentLength();
    char[] content = new char[contentLength];
    br.read(content);
    String bodyLine = new String(content);
    bodyLine = decodeUrl(bodyLine);
    parseBodyLine(bodyLine);
  }

  private int getContentLength() {
    try {
      return Integer.valueOf(headers.get("Content-Length"));
    } catch (Exception e) {
      return 0;
    }
  }

  private void parseBodyLine(String str) {
    parseParamsString(str);
  }

}
