package com.smart_solutions_auth.api.dto.address;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import io.swagger.v3.oas.annotations.media.Schema;

public class CommuneDTO {

	@Schema(name = "CommuneCreateRequest")
	public record CreateRequest(
		@JsonProperty("communeName")
		@NotBlank(message = "El nombre de la comuna es obligatorio")
		@Size(min = 2, max = 100, message = "El nombre de la comuna debe tener entre 2 y 100 caracteres.")
		@Pattern(regexp = "^[a-zA-ZáéíóúñÁÉÍÓÚÑ\\s0-9]{1,}$", message = "El nombre de la comuna contiene caracteres inválidos.")
		String communeName,

		@JsonProperty("regionId")
		@NotNull(message = "El id de la región es obligatorio")
		Long regionId
	){}

	@Schema(name = "CommuneUpdateRequest")
	public record UpdateRequest(
		Long id,

		@JsonProperty("communeName")
		@NotBlank(message = "El nombre de la comuna es obligatorio")
		@Size(min = 2, max = 100, message = "El nombre de la comuna debe tener entre 2 y 100 caracteres.")
		@Pattern(regexp = "^[a-zA-ZáéíóúñÁÉÍÓÚÑ\\s0-9]{1,}$", message = "El nombre de la comuna contiene caracteres inválidos.")
		String communeName,

		@JsonProperty("regionId")
		@NotNull(message = "El id de la región es obligatorio")
		Long regionId
	){}

	@Schema(name = "CommuneResponse")
	public record Response(
		@JsonProperty("id") Long id,
		@JsonProperty("communeName") String communeName,
		@JsonProperty("regionId") Long regionId,
		@JsonProperty("regionName") String regionName
	){}

}
