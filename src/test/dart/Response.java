/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.dart;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 *
 * @author Rice Pavel
 */
public class Response {
  
  private Socket client;
  
  private String response = "";
  
  public Response(Socket client) {
    this.client = client;
  }
  
  public void getFileContent(String fileContent, DataType dataType) throws IOException {
    response = "HTTP/1.1 200 OK\r\n" 
            + "Content-Type: " + getContentType(dataType) + "; charset=UTF-8\r\n"
            
            + "\r\n" 
            + fileContent;
    writeResponse();
  }
  
  private String getContentType(DataType dataType) {    
    if (dataType.equals(DataType.HTML)) {
      return "text/html";
    } else if (dataType.equals(DataType.TEXT)) {
      return "text/plain";
    } else {
      return "";
    }
  }
  
  public void getErrorCode() throws IOException {
    response = "HTTP/1.1 404 Not Found\r\n"
            + "Content-Type: text/html; charset=UTF-8\r\n"
            + "\r\n" 
            + "Page Not Found";
    writeResponse();
  }
  
  private void writeResponse() throws IOException {
    OutputStream os = client.getOutputStream();
    os.write(response.getBytes());
    os.flush();
  }
  
}
