package com.codeit.moim.web.dto.request.comment;

import jakarta.validation.constraints.Min;

public record ReadMyCommentRequest(
        Integer lastCommentId,
        @Min(3)
        int size
){

}
