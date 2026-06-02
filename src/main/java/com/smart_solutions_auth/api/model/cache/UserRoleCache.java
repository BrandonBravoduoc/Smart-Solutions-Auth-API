package com.smart_solutions_auth.api.model.cache;

import java.io.Serializable;

public record UserRoleCache(
    Long id,
    String nameRole
) implements Serializable {
    private static final long serialVersionUID = 1L;
}
