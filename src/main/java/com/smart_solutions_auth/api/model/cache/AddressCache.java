package com.smart_solutions_auth.api.model.cache;

import java.io.Serializable;

public record AddressCache(
    Long id,
    String street,
    String number,
    String complement,
    CommuneCache commune
) implements Serializable{
    private static final long serialVersionUID = 1L;
}
