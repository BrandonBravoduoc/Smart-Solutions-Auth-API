package com.smart_solutions_auth.api.dto.address;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import io.swagger.v3.oas.annotations.media.Schema;

public class AddressDTO {

	@Schema(name = "AddressCreateRequest")
	public record CreateRequest(
		@NotBlank(message = "El nombre de la sucursal es obligatoria.")
		@Size(min = 2, max = 50, message = "El nombre de la sucursal debe tener entre 2 y 50 caracteres.")
		@Pattern(regexp = "^[a-zA-ZáéíóúñÁÉÍÓÚÑ\\s0-9]{1,}$", message = "El nombre de la sucursal contiene caracteres inválidos.")
		String sucursalName,


		@NotBlank(message = "La calle es obligatoria.")
		@Size(min = 2, max = 150, message = "La calle debe tener entre 2 y 150 caracteres.")
		@Pattern(regexp = "^[a-zA-ZáéíóúñÁÉÍÓÚÑ\\s0-9.\\-]{1,}$", message = "La calle contiene caracteres inválidos.")
		String street,

		@NotBlank(message = "El número es obligatorio.")
		@Size(min = 1, max = 20, message = "El número debe tener entre 1 y 20 caracteres.")
		@Pattern(regexp = "^[a-zA-Z0-9\\-\\s]{1,}$", message = "El número de calle contiene caracteres inválidos.")
		String number,

		@NotNull(message = "El id de la comuna es obligatorio.")
		Long communeId
	){}

	@Schema(name = "AddressUpdateRequest")
	public record UpdateRequest(
		Long id,

		@NotBlank(message = "El nombre de la sucursal es obligatoria.")
		@Size(min = 2, max = 50, message = "El nombre de la sucursal debe tener entre 2 y 50 caracteres.")
		@Pattern(regexp = "^[a-zA-ZáéíóúñÁÉÍÓÚÑ\\s0-9]{1,}$", message = "El nombre de la sucursal contiene caracteres inválidos.")
		String sucursalName,

		@NotBlank(message = "La calle es obligatoria")
		@Size(min = 2, max = 150, message = "La calle debe tener entre 2 y 150 caracteres.")
		@Pattern(regexp = "^[a-zA-ZáéíóúñÁÉÍÓÚÑ\\s0-9.\\-]{1,}$", message = "La calle contiene caracteres inválidos.")
		String street,

		@NotBlank(message = "El número es obligatorio")
		@Size(min = 1, max = 20, message = "El número debe tener entre 1 y 20 caracteres.")
		@Pattern(regexp = "^[a-zA-Z0-9\\-\\s]{1,}$", message = "El número de calle contiene caracteres inválidos.")
		String number,

		@NotNull(message = "El id de la comuna es obligatorio")
		Long communeId
	){}

	@Schema(name = "AddressResponse")
	public record Response(
		Long id,
		String sucursalName,
		String street,
		String number,
		Long communeId
	){}

}
