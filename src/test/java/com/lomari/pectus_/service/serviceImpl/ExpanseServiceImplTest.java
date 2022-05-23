package com.lomari.pectus_.service.serviceImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.lomari.pectus_.model.Expanse;
import com.lomari.pectus_.repository.ExpanseRepository;
import com.lomari.pectus_.repository.utils.ExpanseSpecificationBuilder;
import com.lomari.pectus_.service.ExpanseService;
import com.lomari.pectus_.utils.ExpanseSumDto;
import com.lomari.pectus_.utils.NotFoundException;
import com.lomari.pectus_.utils.PagedResponse;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
class ExpanseServiceImplTest {

  @Mock
  ExpanseRepository expanseRepository;

  private ExpanseService expanseService;

  @BeforeEach
  public void setUp() {
    expanseService = new ExpanseServiceImpl(expanseRepository);
  }

  @Test
  @Disabled
  void getAllExpanses() {
    List<Expanse> expanseList = Arrays.asList(
        new Expanse(1L, "Salary", "Monthly", new BigDecimal(1000), "Salary", LocalDate.now()),
        new Expanse(2L, "Food", "Monthly", new BigDecimal(500), "Food", LocalDate.now()),
        new Expanse(3L, "Rent", "Monthly", new BigDecimal(1000), "Rent", LocalDate.now())
    );

    List<String> sort = List.of("department=asc");
    List<String> filter = List.of("amount>100");

    List<Order> sortOrder = getSortOrder(sort);
    Specification<Expanse> filterSpecification = getSpecification(filter);

    Pageable page = PageRequest.of(0, 3, Sort.by(sortOrder));

    Page<Expanse> result = new PageImpl<>(expanseList, page, expanseList.size());

    when(expanseRepository.findAll(filterSpecification, page)).thenReturn(result);

    verify(expanseRepository).findAll(filterSpecification, page);

    PagedResponse<Expanse> expansePagedResponse = expanseService.getAllExpanses( sort, filter, 3, 0);
    assertNotNull(expansePagedResponse);
  }

  @Test
  void getExpanseSum() {
    ExpanseSumDto expanse = new ExpanseSumDto("department", new BigDecimal(1000));
    ExpanseSumDto expanse1 = new ExpanseSumDto("department", new BigDecimal(2000));
    List<ExpanseSumDto> expanseSumDtoList = Arrays.asList(expanse, expanse1);

    given(expanseRepository.getSum("department")).willReturn(expanseSumDtoList);

    List<ExpanseSumDto> department = expanseService.getExpanseSum("department");
    assert(department.size() == 2);
    assertEquals(department.get(0).getCol(), "department");
    assertEquals(department.get(0).getSum(), new BigDecimal(1000));
  }

  @Test
  void getOneExpanseAllFields() {
    Expanse expanse = Expanse.builder()
        .id(1L)
        .department("department")
        .amount(BigDecimal.valueOf(100))
        .projectName("projectName")
        .date(LocalDate.now())
        .memberName("memberName")
        .build();
    when(expanseRepository.findById(1L)).thenReturn(java.util.Optional.of(expanse));
    Expanse oneExpanse = expanseService.getOneExpanse(1L, null);
    assertEquals(expanse.getId(), oneExpanse.getId());
    assertEquals(expanse.getDepartment(), oneExpanse.getDepartment());
    assertEquals(expanse.getAmount(), oneExpanse.getAmount());
    assertEquals(expanse.getProjectName(), oneExpanse.getProjectName());
    assertEquals(expanse.getDate(), oneExpanse.getDate());
    assertEquals(expanse.getMemberName(), oneExpanse.getMemberName());
  }

  @Test
  void getOneExpanseFewFields() {
    Expanse expanse = Expanse.builder()
        .id(1L)
        .department("department")
        .amount(BigDecimal.valueOf(100))
        .projectName("projectName")
        .date(LocalDate.now())
        .memberName("memberName")
        .build();
    when(expanseRepository.findById(1L)).thenReturn(java.util.Optional.of(expanse));
    List<String> fields = List.of("department", "amount");
    Expanse oneExpanse = expanseService.getOneExpanse(1L, fields);
    assertEquals(expanse.getDepartment(), oneExpanse.getDepartment());
    assertEquals(expanse.getAmount(), oneExpanse.getAmount());
    assertNull(expanse.getProjectName());
    assertNull(expanse.getDate());
    assertNull(expanse.getMemberName());
  }

  @Test
  void getOneThrowsException() {
    when(expanseRepository.findById(1L)).thenReturn(java.util.Optional.empty());
    assertThrows(NotFoundException.class, () -> expanseService.getOneExpanse(1L, null));
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