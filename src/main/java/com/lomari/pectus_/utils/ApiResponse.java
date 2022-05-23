package com.lomari.pectus_.utils;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@NoArgsConstructor
@Data
@Builder
public class ApiResponse <T>{

  private T data;
  private HttpStatus status;
  private LocalDateTime dateTime = LocalDateTime.now();
  private String message;
  private String error;

  public ApiResponse(T data, HttpStatus status, LocalDateTime dateTime, String message,
      String error) {
    this.data = data;
    this.status = status;
    this.dateTime = LocalDateTime.now();
    this.message = message;
    this.error = error;
  }

  public ApiResponse(T data, HttpStatus status, String message) {
    this.data = data;
    this.status = status;
    this.message = message;
  }
  public ApiResponse( HttpStatus status, String message, String error) {
    this.status = status;
    this.message = message;
    this.error = error;
  }
}
