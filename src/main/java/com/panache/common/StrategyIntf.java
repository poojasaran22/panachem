package com.panache.common;

import java.sql.PreparedStatement;

@FunctionalInterface
public interface StrategyIntf<T> {
    public T strategy(PreparedStatement pstmt) throws Exception;
}
