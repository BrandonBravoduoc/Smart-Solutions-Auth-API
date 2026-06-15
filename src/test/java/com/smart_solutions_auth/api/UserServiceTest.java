package com.smart_solutions_auth.api;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import com.github.javafaker.Faker;
import com.smart_solutions_auth.api.dto.user.UserDTO;
import com.smart_solutions_auth.api.model.entity.Address;
import com.smart_solutions_auth.api.model.entity.User;
import com.smart_solutions_auth.api.model.entity.UserContact;
import com.smart_solutions_auth.api.model.entity.UserRole;
import com.smart_solutions_auth.api.repository.UserContactRepository;
import com.smart_solutions_auth.api.repository.UserRepository;
import com.smart_solutions_auth.api.service.UserService;
import com.smart_solutions_auth.api.util.Validations;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private UserContactRepository userContactRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private Validations validations;

    @InjectMocks private UserService userService;

    private final Faker faker = new Faker();

    private User testUser;
    private UserContact testContact;
    private UserRole testRole;
    private Address testAddress;

    @BeforeEach
    void setUp() {
        testRole = new UserRole();
        testRole.setId(1L);
        testRole.setNameRole("CLIENTE");

        testAddress = new Address();
        testAddress.setId(1L);
        testAddress.setSucursalName("Sucursal Santiago Centro");
        testAddress.setStreet("Avenida Libertador");
        testAddress.setNumber("1234");

        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail(faker.internet().emailAddress());
        testUser.setPassword("encoded_password_hash");
        testUser.setUserRole(testRole);
        testUser.setAsset(true);

        testContact = new UserContact();
        testContact.setId(1L);
        testContact.setName(faker.name().firstName());
        testContact.setLastName(faker.name().lastName());
        testContact.setPhoneNumber("9" + faker.number().digits(8));
        testContact.setUser(testUser);
        testContact.setUserAddress(testAddress);
    }

    @Test
    void obtener_perfil() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userContactRepository.findByUserId(1L)).thenReturn(Optional.of(testContact));

        UserDTO.Response result = userService.profile(1L);

        assertNotNull(result);
        assertEquals(testUser.getEmail(), result.email());
        assertEquals(testContact.getName(), result.name());
        assertEquals("Sucursal Santiago Centro", result.sucursalName());
    }

    @Test
    void usuario_no_encontrado() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.profile(99L));
    }

    @Test
    void buscar_usuario_por_email() {
        String email = testUser.getEmail();
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(testUser));
        when(userContactRepository.findByUserId(testUser.getId())).thenReturn(Optional.of(testContact));

        UserDTO.Response result = userService.getUserByEmail(email);

        assertNotNull(result);
        assertEquals(email, result.email());
        assertEquals(testContact.getPhoneNumber(), result.phone());
    }

    @Test
    void email_no_registrado() {
        String unknownEmail = "no.existe@smartsolutions.cl";
        when(userRepository.findByEmail(unknownEmail)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.getUserByEmail(unknownEmail));
    }

    @Test
    void listar_todos_los_usuarios() {
        User secondUser = new User();
        secondUser.setId(2L);
        secondUser.setEmail(faker.internet().emailAddress());
        secondUser.setUserRole(testRole);

        UserContact secondContact = new UserContact();
        secondContact.setName(faker.name().firstName());
        secondContact.setLastName(faker.name().lastName());
        secondContact.setPhoneNumber("2" + faker.number().digits(8));
        secondContact.setUserAddress(testAddress);
        secondUser.setUserContact(secondContact);

        testUser.setUserContact(testContact);

        when(userRepository.findAll()).thenReturn(List.of(testUser, secondUser));

        List<UserDTO.Response> result = userService.listUsers();

        assertEquals(2, result.size());
        assertEquals(testUser.getEmail(), result.get(0).email());
    }

    @Test
    void contrasena_actual_incorrecta() {
        when(validations.getCurrentUserId()).thenReturn(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(anyString(), eq("encoded_password_hash"))).thenReturn(false);

        UserDTO.ChangePasswordRequest dto = new UserDTO.ChangePasswordRequest(
                "contrasenaIncorrecta123",
                "NuevaPass1",
                "NuevaPass1"
        );

        assertThrows(RuntimeException.class, () -> userService.changePassword(dto));
    }

    @Test
    void verificar_total_usuarios_sistema() {
        testUser.setUserContact(testContact);
        when(userRepository.findAll()).thenReturn(List.of(testUser));

        List<UserDTO.Response> result = userService.listUsers();

        assertEquals(3, result.size()); // falla: la lista tiene 1, no 3
    }
}
