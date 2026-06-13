package com.smart_solutions_auth.api.model.cache;

import java.io.Serializable;

public record CommuneCache(
    Long id,
    String communeName,
    RegionCache region
) implements Serializable{
    private static final long serialVersionUID = 1L;
}
