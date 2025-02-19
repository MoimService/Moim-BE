package com.codeit.moim.web.dto.response.slice;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.SliceImpl;

import java.util.List;

@Getter
public class CustomSlice<T> extends SliceImpl<T> {
    private final Integer nextCursor;

    @Builder
    public CustomSlice(List<T> content, Pageable pageable, boolean hasNext, Integer nextCursor) {
        super(content, pageable, hasNext);
        this.nextCursor = nextCursor;
    }
}

