package com.lomari.pectus_.service;


import com.lomari.pectus_.model.Expanse;
import com.lomari.pectus_.utils.ExpanseSumDto;
import com.lomari.pectus_.utils.PagedResponse;
import java.util.List;

public interface ExpanseService {

  List<ExpanseSumDto> getExpanseSum(String by);

  PagedResponse<Expanse> getAllExpanses(List<String> sort, List<String> filter, Integer pageSize, Integer pageNumber);

  Expanse getOneExpanse(Long id, List<String> fields);
}
