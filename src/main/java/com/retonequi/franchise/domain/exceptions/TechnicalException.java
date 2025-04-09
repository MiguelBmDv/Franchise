package com.retonequi.franchise.domain.exceptions;

import com.retonequi.franchise.domain.enums.Messages;

import lombok.Getter;

@Getter
public class TechnicalException extends ProcessorException {

    public TechnicalException(Throwable cause, Messages technicalMessage) {
        super(cause, technicalMessage);
    }

    public TechnicalException(Messages technicalMessage) {
        super(technicalMessage.getMessage(), technicalMessage);
    }
}