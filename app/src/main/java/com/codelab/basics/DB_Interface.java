package com.codelab.basics;

import java.util.List;
import java.util.Map;

public interface DB_Interface {

    public int count();

    public int save(DataModel dataModel);

    public int update(DataModel dataModel);  // Not implemented

    public int deleteById(Long id);  // Not implemented

    public List<DataModel> findAll();

    public String getNameById(Long id);  // Not implemented

    public DataModel getMax();

    public void incAccessCount(long id);

    public long getMostAccessed();

}
