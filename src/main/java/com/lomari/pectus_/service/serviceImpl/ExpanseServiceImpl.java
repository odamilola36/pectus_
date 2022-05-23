package com.lomari.pectus_.service.serviceImpl;

import com.lomari.pectus_.model.Expanse;
import com.lomari.pectus_.repository.ExpanseRepository;
import com.lomari.pectus_.repository.utils.ExpanseSpecificationBuilder;
import com.lomari.pectus_.service.ExpanseService;
import com.lomari.pectus_.utils.ExpanseSumDto;
import com.lomari.pectus_.utils.NotFoundException;
import com.lomari.pectus_.utils.PagedResponse;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ExpanseServiceImpl implements ExpanseService {

  private final ExpanseRepository expanseRepository;

  public ExpanseServiceImpl(ExpanseRepository expanseRepository) {
    this.expanseRepository = expanseRepository;
  }

  @Override
  public List<ExpanseSumDto> getExpanseSum(String by) {
    return expanseRepository.getSum(by);
  }

  @Override
  public PagedResponse<Expanse> getAllExpanses(List<String> sort, List<String> filter,
      Integer pageSize, Integer pageNumber) {
    Sort sortParams = Sort.unsorted();
    if (sort != null && !sort.isEmpty()) {
      sortParams = Sort.by(getSortOrder(sort));
    }

    Specification<Expanse> spec = getSpecification(filter);

    Pageable pageable = PageRequest.of(pageNumber, pageSize, sortParams);

    Page<Expanse> page = expanseRepository.findAll(spec, pageable);

    return PagedResponse.pagedResponse(page);
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

  @Override
  public Expanse getOneExpanse(Long condition, List<String> fields) {
    Expanse expanse = expanseRepository.findById(condition)
        .orElseThrow(() -> new NotFoundException("Expanse not found"));
    return filterOneExpanse(expanse, fields);
  }

  private Expanse filterOneExpanse(Expanse expanse, List<String> fields){
    if( fields == null || fields.isEmpty() )
      return expanse;
    if (!fields.contains("department"))
      expanse.setDepartment(null);
    if (!fields.contains("projectName"))
      expanse.setProjectName(null);
    if(!fields.contains("memberName"))
      expanse.setMemberName(null);
    if(!fields.contains("date"))
      expanse.setDate(null);
    if(!fields.contains("amount"))
      expanse.setAmount(null);
    return expanse;
  }
}
