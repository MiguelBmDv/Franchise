package com.retonequi.franchise.domain.exceptions;

import com.retonequi.franchise.domain.enums.Messages;

import lombok.Getter;

@Getter
public class DomainException extends ProcessorException{

    public DomainException(Messages technicalMessage) {
        super(technicalMessage.getMessage(), technicalMessage);
    }

}