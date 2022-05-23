package com.lomari.pectus_.controller;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.lomari.pectus_.model.Expanse;
import com.lomari.pectus_.repository.utils.ExpanseSpecificationBuilder;
import com.lomari.pectus_.service.ExpanseService;
import com.lomari.pectus_.utils.ExpanseSumDto;
import com.lomari.pectus_.utils.PagedResponse;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@Slf4j
@RunWith(SpringRunner.class)
@WebMvcTest(ExpanseController.class)
class ExpanseControllerTest {

  @Autowired
  private MockMvc mvc;

  @MockBean
  private ExpanseService expanseService;

  @Test
  void getAllExpansesWithoutFilterAndSortParameters() throws Exception {
    List<Expanse> expanseList = Arrays.asList(
        new Expanse(1L, "Salary", "Monthly", new BigDecimal(1000), "Salary", LocalDate.now()),
        new Expanse(2L, "Food", "Monthly", new BigDecimal(500), "Food", LocalDate.now()),
        new Expanse(3L, "Rent", "Monthly", new BigDecimal(1000), "Rent", LocalDate.now())
    );
    PagedResponse<Expanse> response = new PagedResponse(0, 10, expanseList, 3L, 1, true, true);
    when(expanseService.getAllExpanses(null, null, 10, 0)).thenReturn(response);
    mvc.perform(get("/api/expanse_data/expanses"))
        .andExpect(status().isOk());
  }

  @Test
  void getAllExpansesWithOneFilterWithoutSortParameters() throws Exception {
    List<Expanse> expanseList = Arrays.asList(
        new Expanse(1L, "Salary", "Monthly", new BigDecimal(1000), "Salary", LocalDate.now()),
        new Expanse(3L, "Rent", "Monthly", new BigDecimal(1000), "Rent", LocalDate.now())
    );

    List<String> filter = Arrays.asList("amount>500");
    given(expanseService.getAllExpanses(null, filter, 10, 0)).willReturn(
        new PagedResponse(1, 10, expanseList, 2L, 1, true, false));
    mvc.perform(get("/api/expanse_data/expanses?filter=" + filter.get(0)))
        .andExpect(status().isOk());
  }

  @Test
  void getAllExpansesWithOneFilterWithSortParameters() throws Exception {
    List<Expanse> expanseList = Arrays.asList(
        new Expanse(1L, "Salary", "Monthly", new BigDecimal(1000), "Salary", LocalDate.now()),
        new Expanse(3L, "Rent", "Monthly", new BigDecimal(1000), "Rent", LocalDate.now())
    );

    List<String> filter = Arrays.asList("amount>500");
    List<String> sort = Arrays.asList("department");
    given(expanseService.getAllExpanses(sort, filter, 10, 0)).willReturn(
        new PagedResponse(1, 10, expanseList, 2L, 1, true, false));
    mvc.perform(get("/api/expanse_data/expanses?filter=" + filter.get(0)+"&sort="+sort.get(0)))
        .andExpect(status().isOk());
  }

  @Test
  void getExpanseSumGroupedByFieldDate() throws Exception {
    LocalDate date = LocalDate.now();
    given(expanseService.getExpanseSum("date"))
        .willReturn(Arrays.asList(
            ExpanseSumDto.builder()
                .col(date)
                .sum(BigDecimal.TEN)
                .build()
        ));

    mvc.perform(get("/api/expanse_data/aggregates?by=date"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data[0].col[0]").value(date.getYear()))
        .andExpect(jsonPath("$.data[0].sum").value(BigDecimal.TEN.toString()));
  }

  @Test
  void getExpanseSumGroupedByFieldOther() throws Exception {
    String department = "department";
    given(expanseService.getExpanseSum(department))
        .willReturn(Arrays.asList(
            ExpanseSumDto.builder()
                .col(department)
                .sum(BigDecimal.TEN)
                .build()
        ));

    mvc.perform(get("/api/expanse_data/aggregates?by=department"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data[0].col").value(department))
        .andExpect(jsonPath("$.data[0].sum").value(BigDecimal.TEN.toString()));
  }

  @Test
  void getOneExpanseWithAllFields() throws Exception {
    LocalDate date = LocalDate.now();
    Expanse expanse = Expanse.builder()
        .date(date)
        .memberName("Damilola Omowumi")
        .projectName("Pectus")
        .amount(BigDecimal.valueOf(100))
        .department("IT")
        .id(1L)
        .build();

    given(expanseService.getOneExpanse(1L, null)).willReturn(expanse);

    mvc.perform(get("/api/expanse_data/expanse/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.id").value(1))
        .andExpect(jsonPath("$.data.date[0]").value(date.getYear()))
        .andExpect(jsonPath("$.data.date[1]").value(date.getMonthValue()))
        .andExpect(jsonPath("$.data.date[2]").value(date.getDayOfMonth()))
        .andExpect(jsonPath("$.data.memberName").value(expanse.getMemberName()))
        .andExpect(jsonPath("$.data.projectName").value(expanse.getProjectName()))
        .andExpect(jsonPath("$.data.amount").value(expanse.getAmount().toString()))
        .andExpect(jsonPath("$.data.department").value(expanse.getDepartment()));
  }

  @Test
  void getOneExpanseWithExemptedFields() throws Exception {
    LocalDate date = LocalDate.now();
    Expanse expanse = Expanse.builder()
        .date(date)
        .memberName("Damilola Omowumi")
        .projectName("Pectus")
        .amount(BigDecimal.valueOf(100))
        .department("IT")
        .id(1L)
        .build();
    List<String> fields = Arrays.asList("department", "date");
    Expanse filtered = filterOneExpanse(expanse, fields);
    given(expanseService.getOneExpanse(1L, fields)).willReturn(filtered);

    mvc.perform(get("/api/expanse_data/expanse/1?fields=department,date"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.id").value(1))
        .andExpect(jsonPath("$.data.date[0]").value(date.getYear()))
        .andExpect(jsonPath("$.data.date[1]").value(date.getMonthValue()))
        .andExpect(jsonPath("$.data.date[2]").value(date.getDayOfMonth()))
        .andExpect(jsonPath("$.data.memberName").doesNotExist())
        .andExpect(jsonPath("$.data.projectName").doesNotExist())
        .andExpect(jsonPath("$.data.amount").doesNotExist())
        .andExpect(jsonPath("$.data.department").value(filtered.getDepartment()));
  }

  private Expanse filterOneExpanse(Expanse expanse, List<String> fields) {
    if (fields == null || fields.isEmpty()) {
      return expanse;
    }
    if (!fields.contains("department")) {
      expanse.setDepartment(null);
    }
    if (!fields.contains("projectName")) {
      expanse.setProjectName(null);
    }
    if (!fields.contains("memberName")) {
      expanse.setMemberName(null);
    }
    if (!fields.contains("date")) {
      expanse.setDate(null);
    }
    if (!fields.contains("amount")) {
      expanse.setAmount(null);
    }
    return expanse;
  }

  private Specification<Expanse> getSpecification(List<String> filter) {
    if (filter == null || filter.isEmpty()) {
      return null;
    }
    Pattern pattern = Pattern.compile("(\\w+)(=|<|>)((\\w+-\\w+-\\w+)|(\\w+-\\w+)|\\w+)");
    ExpanseSpecificationBuilder builder = new ExpanseSpecificationBuilder();
    filter.forEach(f -> {
      Matcher matcher = pattern.matcher(f);
      if (matcher.find()) {
        String key = matcher.group(1);
        String operation = matcher.group(2);
        String value = matcher.group(3);
        builder.with(key, operation, value);
      }
    });
    return builder.build();
  }

  private List<Order> getSortOrder(List<String> sort) {
    return sort.stream().map(s -> {
      String[] split = s.split("=");
      return new Order(Sort.Direction.fromString(split[1]), split[0]);
    }).collect(Collectors.toList());
  }
}