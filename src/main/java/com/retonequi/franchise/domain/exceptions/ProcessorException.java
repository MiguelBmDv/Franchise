package com.retonequi.franchise.domain.exceptions;

import com.retonequi.franchise.domain.enums.Messages;

import lombok.Getter;

@Getter
public class ProcessorException extends RuntimeException{
    private final Messages technicalMessage;

    public ProcessorException(Throwable cause, Messages message) {
        super(cause);
        technicalMessage = message;
    }

    public ProcessorException(String message,
                              Messages technicalMessage) {

        super(message);
        this.technicalMessage = technicalMessage;
    }
}
