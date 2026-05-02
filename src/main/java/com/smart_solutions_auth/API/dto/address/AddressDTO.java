package com.smart_solutions_auth.API.dto.address;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import io.swagger.v3.oas.annotations.media.Schema;

public class AddressDTO {

	@Schema(name = "AddressCreateRequest")
	public record CreateRequest(
		@NotBlank(message = "La calle es obligatoria")
		@Size(max = 150)
		String street,

		@NotBlank(message = "El número es obligatorio")
		@Size(max = 20)
		String number,

		@NotNull(message = "El id de la comuna es obligatorio")
		Long communeId
	){}

	@Schema(name = "AddressUpdateRequest")
	public record UpdateRequest(
		Long id,
		@NotBlank(message = "La calle es obligatoria")
		@Size(max = 150)
		String street,

		@NotBlank(message = "El número es obligatorio")
		@Size(max = 20)
		String number,

		@NotNull(message = "El id de la comuna es obligatorio")
		Long communeId
	){}

	@Schema(name = "AddressResponse")
	public record Response(
		String street,
		String number,
		Long communeId
	){}

}
