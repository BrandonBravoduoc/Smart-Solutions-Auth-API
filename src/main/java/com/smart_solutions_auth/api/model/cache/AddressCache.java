package com.smart_solutions_auth.api.model.cache;

import java.io.Serializable;

public record AddressCache(
    Long id,
    String sucursalName,
    String street,
    String number,
    CommuneCache commune
) implements Serializable{
    private static final long serialVersionUID = 1L;
}
