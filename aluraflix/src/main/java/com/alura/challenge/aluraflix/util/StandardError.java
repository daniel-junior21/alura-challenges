package com.alura.challenge.aluraflix.util;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class StandardError implements Serializable {
    private static final long serialVersionUID = 0L;
    private String message;

    public StandardError(String message) {
        super();
        this.message = message;
    }
}
