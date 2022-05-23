package com.lomari.pectus_.controller;


import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.lomari.pectus_.model.Expanse;
import com.lomari.pectus_.service.ExpanseService;
import com.lomari.pectus_.utils.ApiResponse;
import com.lomari.pectus_.utils.ExpanseSumDto;
import com.lomari.pectus_.utils.PagedResponse;
import com.lomari.pectus_.utils.SparseField;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/api/expanse_data")
public class ExpanseController {

  private final ExpanseService expanseService;

  public ExpanseController(ExpanseService expanseService) {
    this.expanseService = expanseService;
  }

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<ApiResponse> handleException(NotFoundException ex) {
    ApiResponse response = new ApiResponse(HttpStatus.NOT_FOUND, "Not Found", ex.getMessage());
    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
  }

  private ResponseEntity<ApiResponse> buildResponseEntity (Object t) {
    ApiResponse apiResponse = ApiResponse.builder()
        .data(t)
        .message("Successful")
        .status(HttpStatus.OK)
        .build();

    return ResponseEntity.ok().body(apiResponse);
  }

  @GetMapping("/expanses")
  public ResponseEntity<ApiResponse> getAllExpanses(
      @RequestParam(value = "sort", required = false) List<String> sort,
      @RequestParam(value = "filter", required = false) List<String> filter,
      @RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer pageSize,
      @RequestParam(value = "pageNumber", required = false, defaultValue = "0") Integer pageNumber) {
    PagedResponse<Expanse> expanses = expanseService.getAllExpanses(sort, filter, pageSize,
        pageNumber);

    return buildResponseEntity(expanses);
  }

  @GetMapping("/aggregates")
  public ResponseEntity<ApiResponse> getExpanseSum(@RequestParam String by) {
    List<ExpanseSumDto> expanseSum = expanseService.getExpanseSum(by);

    return buildResponseEntity(expanseSum);
  }

  @GetMapping("/expanse/{id}")
  public Object getOneExpanse(
      @RequestParam(value = "fields", required = false) List<String> fields,
      @PathVariable("id") Long id) {
    Expanse expanse = expanseService.getOneExpanse(id, fields);

    return buildResponseEntity(expanse);
  }

}
