package com.codeit.moim.web.dto.response.comment;

import lombok.Builder;

@Builder
public record ReadCommentDistributionResponse (
        long fives,
        long fours,
        long threes,
        long twos,
        long ones
){
    public static ReadCommentDistributionResponse fromEntity(long fives, long fours, long threes, long twos, long ones){
        return ReadCommentDistributionResponse.builder()
                .fives(fives)
                .fours(fours)
                .threes(threes)
                .twos(twos)
                .ones(ones)
                .build();
    }
}
