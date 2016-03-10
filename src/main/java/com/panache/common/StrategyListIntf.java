package com.panache.common;

import java.sql.PreparedStatement;
import java.util.List;

@FunctionalInterface
public interface StrategyListIntf<T> {
    public List<T> strategy(PreparedStatement pstmt)throws Exception;
}
