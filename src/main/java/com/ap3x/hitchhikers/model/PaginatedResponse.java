package com.ap3x.hitchhikers.model;

import lombok.Data;

import java.util.List;

@Data
public class PaginatedResponse<T> {
    private Long count;
    private String nextPage;
    private String previousPage;
    private List<T> data;
}
