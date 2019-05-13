package com.ap3x.hitchhikers.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseBody<T> {

    private String nextPage;
    private String previousPage;
    private List<T> data;
    private String message;
}
