package com.ap3x.hitchhikers.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ErrorBody {

    private LocalDateTime timestamp;
    private String message;
}
