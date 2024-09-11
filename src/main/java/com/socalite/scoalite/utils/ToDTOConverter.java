package com.socalite.scoalite.utils;

@FunctionalInterface
public interface ToDTOConverter<I, R> {
    R convert(I input);
}
