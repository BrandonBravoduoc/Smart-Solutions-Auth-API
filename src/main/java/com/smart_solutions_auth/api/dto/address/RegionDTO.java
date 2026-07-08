package com.smart_solutions_auth.api.dto.address;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import io.swagger.v3.oas.annotations.media.Schema;

public class RegionDTO {

	@Schema(name = "RegionCreateRequest")
	public record CreateRequest(
		@NotBlank(message = "El nombre de la región es obligatorio")
		@Size(min = 10, max = 100, message = "El nombre de la región debe tener entre 10 y 100 caracteres.")
		@Pattern(regexp = "^[a-zA-ZáéíóúñÁÉÍÓÚÑ\\s0-9]{1,}$", message = "El nombre de la región contiene caracteres inválidos.")
		String regionName
	){}

	@Schema(name = "RegionUpdateRequest")
	public record UpdateRequest(
		Long id,
		@NotBlank(message = "El nombre de la región es obligatorio")
		@Size(min = 10, max = 100, message = "El nombre de la región debe tener entre 10 y 100 caracteres.")
		@Pattern(regexp = "^[a-zA-ZáéíóúñÁÉÍÓÚÑ\\s0-9]{1,}$", message = "El nombre de la región contiene caracteres inválidos.")
		String regionName
	){}

	@Schema(name = "RegionResponse")
	public record Response(
		Long id,
		String regionName,
		boolean active
	){}

	@Schema(name = "RegionStatusRequest")
	public record StatusRequest(
		boolean active
	){}

}
