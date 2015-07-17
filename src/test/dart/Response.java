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
  
  public void getFileContent(String fileContent) throws IOException {
    response = "HTTP/1.1 200 OK\r\n" 
            + "Content-Type: text/html\r\n"
            + "\r\n" 
            + fileContent;
    writeResponse();
  }
  
  public void getErrorCode() throws IOException {
    response = "HTTP/1.1 404 Not Found";
    writeResponse();
  }
  
  private void writeResponse() throws IOException {
    OutputStream os = client.getOutputStream();
    os.write(response.getBytes());
  }
  
}
