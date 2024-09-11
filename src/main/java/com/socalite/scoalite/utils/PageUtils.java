package com.socalite.scoalite.utils;

import jakarta.annotation.Nullable;
import org.springframework.data.domain.*;

import java.util.Objects;

public interface PageUtils {
    static <T, R> Multi<R> toMulti(Page<T> page, R newContent) {
        return new Multi<R>(page.getNumber() + 1, page.getSize(), page.getTotalElements(), page.getTotalPages(), newContent);
    }

    static Pageable build(int pageNumber, int pageSize) {
        return build(pageNumber, pageSize, Sort.unsorted());
    }
    
    static Pageable build(int pageNumber, int pageSize, Sort sort) {
        if (pageNumber > 0) {
            pageNumber = pageNumber - 1;
        }
        return PageRequest.of(pageNumber, pageSize, sort);
    }

    static Pageable rebuild(Pageable pageable) {
        return rebuild(pageable, Sort.unsorted());
    }

//    TODO: to revisit
    static Pageable rebuild(Pageable pageable, @Nullable Sort sort) {
        if (Objects.isNull(pageable)) {
            pageable = PageRequest.of(1, 10, sort);
        } else  {
            var pageSize = 10;
            var pageNumber = pageable.getPageNumber();

            if (Objects.nonNull(sort) && sort.isSorted()) {
                sort = pageable.getSort();
            }
            if (pageable.isPaged()) {
                pageSize = pageable.getPageSize();
            }

            pageable = PageRequest.of(pageNumber, pageSize, sort);
        }
        return pageable;
    }
}
