package com.smart_solutions_auth.api.model.cache;

import java.io.Serializable;

public record RegionCache (
    Long id,
    String nameRegion
)implements Serializable{
    private static final long serialVersionUID = 1l;
}
    

