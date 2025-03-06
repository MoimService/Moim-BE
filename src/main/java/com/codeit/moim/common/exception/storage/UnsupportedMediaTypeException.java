package com.codeit.moim.common.exception.storage;

import com.codeit.moim.common.exception.global.StorageException;

public class UnsupportedMediaTypeException extends StorageException {


    private static final String ENTITY_TYPE = "Image";

    public UnsupportedMediaTypeException(int statusCode, String request) {
        super(statusCode, request, ENTITY_TYPE);
    }
}
