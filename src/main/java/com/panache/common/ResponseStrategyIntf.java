package com.panache.common;

@FunctionalInterface
public interface ResponseStrategyIntf<T> {
    T stratgy();
}
