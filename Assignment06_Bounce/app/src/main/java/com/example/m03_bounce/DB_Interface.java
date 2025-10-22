package com.example.m03_bounce;

import java.util.List;

public interface DB_Interface {

    public int count();

    public int save(DataModel dataModel);

    public int update(DataModel dataModel);  // Not implemented

    public int deleteById(Long id);  // Not implemented

    public List<DataModel> findAll();

    public String getNameById(Long id);  // Not implemented

}
