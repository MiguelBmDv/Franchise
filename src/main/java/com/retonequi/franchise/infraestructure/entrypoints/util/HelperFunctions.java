package com.retonequi.franchise.infraestructure.entrypoints.util;

import java.util.UUID;

import com.retonequi.franchise.domain.enums.Messages;
import com.retonequi.franchise.domain.exceptions.DomainException;

import lombok.experimental.UtilityClass;

@UtilityClass
public class HelperFunctions {
    public static UUID validateUUID(String idStr) {
        try {
            return UUID.fromString(idStr);
        } catch (IllegalArgumentException | NullPointerException ex) {
            throw new DomainException(Messages.INVALID_ID);
        }
    }
}
