package com.retonequi.franchise.infraestructure.entrypoints.util;

import java.util.UUID;

import com.retonequi.franchise.domain.enums.Messages;
import com.retonequi.franchise.domain.exceptions.DomainException;

public class HelperFunctions {
    private HelperFunctions() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static UUID validateUUID(String idStr) {
        try {
            return UUID.fromString(idStr);
        } catch (IllegalArgumentException | NullPointerException ex) {
            throw new DomainException(Messages.INVALID_ID);
        }
    }
}
