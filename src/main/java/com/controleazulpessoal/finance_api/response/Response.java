package com.controleazulpessoal.finance_api.response;

import java.io.Serializable;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Response<T> implements Serializable {

    @JsonProperty("data")
    @JsonInclude(Include.ALWAYS)
    private final T data;

    Response(T data) {
        Objects.requireNonNull(data, "Response data must not be null");
        this.data = data;
    }

    public static <T> Response<T> of(T data) {
        return new Response<T>(data);
    }

    public T getData() {
        return this.data;
    }
}