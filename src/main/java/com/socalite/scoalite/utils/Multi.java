package com.socalite.scoalite.utils;

import java.util.Objects;

public record Multi<T>(int pageNumber, int pageSize, long totalElementsCount, int totalNumberOfPages, T content) {
//    public static <T> Multi<T> of(Page<T> content) {
//        return new Multi<T>(content.getNumber(), content.getSize(), content.getNumberOfElements(), content.getTotalPages(), content);
//    }

    public static <T> Multi<T> empty() {
        return new Multi<>(0, 0, 0, 0, null);
    }

    public boolean isEmpty() {
        return this.pageNumber == 0
                && this.pageSize == 0
                && this.totalNumberOfPages == 0
                && this.totalElementsCount == 0
                && Objects.isNull(content);
    }
}
