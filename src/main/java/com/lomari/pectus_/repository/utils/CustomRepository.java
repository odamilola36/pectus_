package com.lomari.pectus_.repository.utils;

import com.lomari.pectus_.model.Expanse;
import com.lomari.pectus_.utils.ExpanseSumDto;
import com.lomari.pectus_.utils.SparseField;
import java.util.List;
import java.util.Optional;

public interface CustomRepository {

  List<ExpanseSumDto> getSum(String by);

}
