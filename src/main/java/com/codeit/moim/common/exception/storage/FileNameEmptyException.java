package com.codeit.moim.common.exception.storage;

import com.codeit.moim.common.exception.global.StorageException;

public class FileNameEmptyException extends StorageException {


    private static final String ENTITY_TYPE = "Image";

    public FileNameEmptyException(int statusCode, String request) {
        super(statusCode, request, ENTITY_TYPE);
    }
}
