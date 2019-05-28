package com.ap3x.hitchhikers.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class HitchhikersGuideError {
    private LocalDateTime timestamp;
    private String message;
}
