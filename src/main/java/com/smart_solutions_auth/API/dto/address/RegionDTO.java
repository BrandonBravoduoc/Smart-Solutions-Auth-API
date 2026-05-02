package com.smart_solutions_auth.API.dto.address;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import io.swagger.v3.oas.annotations.media.Schema;

public class RegionDTO {

	@Schema(name = "RegionCreateRequest")
	public record CreateRequest(
		@NotBlank(message = "El nombre de la región es obligatorio")
		@Size(max = 100)
		String regionName
	){}

	@Schema(name = "RegionUpdateRequest")
	public record UpdateRequest(
		Long id,
		@NotBlank(message = "El nombre de la región es obligatorio")
		@Size(max = 100)
		String regionName
	){}

	@Schema(name = "RegionResponse")
	public record Response(
		Long id,
		String regionName
	){}

}
