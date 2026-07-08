package com.smart_solutions_auth.api.util;

import java.util.LinkedHashMap;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.smart_solutions_auth.api.model.entity.Address;
import com.smart_solutions_auth.api.model.entity.Commune;
import com.smart_solutions_auth.api.model.entity.Region;
import com.smart_solutions_auth.api.model.entity.User;
import com.smart_solutions_auth.api.model.entity.UserContact;
import com.smart_solutions_auth.api.model.entity.UserRole;
import com.smart_solutions_auth.api.repository.AddressRepository;
import com.smart_solutions_auth.api.repository.CommuneRepository;
import com.smart_solutions_auth.api.repository.RegionRepository;
import com.smart_solutions_auth.api.repository.UserContactRepository;
import com.smart_solutions_auth.api.repository.UserRepository;
import com.smart_solutions_auth.api.repository.UserRoleRepository;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRoleRepository roleRepository;
    private final UserRepository userRepository;
    private final UserContactRepository contactRepository;
    private final PasswordEncoder passwordEncoder;
    private final RegionRepository regionRepository;
    private final CommuneRepository communeRepository;
    private final AddressRepository addressRepository;

    private static final Map<String, String[]> REGIONS_AND_COMMUNES = new LinkedHashMap<>();
    static {
        REGIONS_AND_COMMUNES.put("Región Metropolitana", new String[] {
            "cerrillos", "santiago", "providencia", "las condes", "maipu"
        });
        REGIONS_AND_COMMUNES.put("Valparaíso", new String[] {
            "valparaiso", "viña del mar", "quilpue"
        });
        REGIONS_AND_COMMUNES.put("Biobío", new String[] {
            "concepcion", "talcahuano"
        });
    }

    private static final Map<String, String[]> SAMPLE_ADDRESSES = new LinkedHashMap<>();
    static {
        SAMPLE_ADDRESSES.put("cerrillos", new String[] { "Plaza Oeste", "Colo Colo", "921" });
        SAMPLE_ADDRESSES.put("santiago", new String[] { "Sucursal Centro", "Alameda", "1500" });
        SAMPLE_ADDRESSES.put("providencia", new String[] { "Sucursal Providencia", "Av. 11 de Septiembre", "2200" });
        SAMPLE_ADDRESSES.put("las condes", new String[] { "Sucursal Las Condes", "Av. Apoquindo", "4700" });
        SAMPLE_ADDRESSES.put("maipu", new String[] { "Sucursal Maipú", "Av. Pajaritos", "3200" });
        SAMPLE_ADDRESSES.put("valparaiso", new String[] { "Sucursal Valparaíso", "Av. Errázuriz", "1010" });
        SAMPLE_ADDRESSES.put("viña del mar", new String[] { "Sucursal Viña del Mar", "Av. San Martín", "550" });
        SAMPLE_ADDRESSES.put("quilpue", new String[] { "Sucursal Quilpué", "Av. Los Carrera", "300" });
        SAMPLE_ADDRESSES.put("concepcion", new String[] { "Sucursal Concepción", "Barros Arana", "640" });
        SAMPLE_ADDRESSES.put("talcahuano", new String[] { "Sucursal Talcahuano", "Av. Colón", "1200" });
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {

        UserRole adminRole = createRoleIfNotFound("ADMINISTRADOR");
        createRoleIfNotFound("CLIENTE");

        Address defaultAddress = seedRegionsCommunesAndAddresses();

        if (userRepository.findByEmail("admin@smart.com").isEmpty()) {

            User adminUser = new User();
            adminUser.setEmail("admin@smart.com");
            adminUser.setPassword(passwordEncoder.encode("Admin123$"));
            adminUser.setUserRole(adminRole);
            adminUser.setAsset(true);

            User savedAdmin = userRepository.save(adminUser);

            UserContact adminContact = new UserContact();
            adminContact.setName("Admin");
            adminContact.setLastName("Sistema");
            adminContact.setPhoneNumber("999999999");
            adminContact.setUserAddress(defaultAddress);
            adminContact.setUser(savedAdmin);

            contactRepository.save(adminContact);

            System.out.println("--> Configuración inicial: Roles y Usuario Admin creados con éxito.");
        }
    }

    private Address seedRegionsCommunesAndAddresses() {
        if (regionRepository.count() == 0) {
            for (Map.Entry<String, String[]> entry : REGIONS_AND_COMMUNES.entrySet()) {
                Region region = new Region();
                region.setRegionName(entry.getKey());
                region.setActive(true);
                Region savedRegion = regionRepository.save(region);

                for (String communeName : entry.getValue()) {
                    Commune commune = new Commune();
                    commune.setCommuneName(communeName);
                    commune.setRegion(savedRegion);
                    commune.setActive(true);
                    communeRepository.save(commune);
                }
            }
            System.out.println("--> Configuración inicial: regiones y comunas precargadas.");
        }

        Address defaultAddress = null;
        for (Commune commune : communeRepository.findAll()) {
            boolean hasAddress = addressRepository.findAll().stream()
                .anyMatch(a -> a.getCommune().getId().equals(commune.getId()));

            Address communeAddress;
            if (hasAddress) {
                communeAddress = addressRepository.findAll().stream()
                    .filter(a -> a.getCommune().getId().equals(commune.getId()))
                    .findFirst()
                    .orElseThrow();
            } else {
                String[] addressInfo = SAMPLE_ADDRESSES.getOrDefault(commune.getCommuneName(),
                    new String[] { "Sucursal " + commune.getCommuneName(), "Av. Principal", "100" });

                Address address = new Address();
                address.setSucursalName(addressInfo[0]);
                address.setStreet(addressInfo[1]);
                address.setNumber(addressInfo[2]);
                address.setCommune(commune);
                communeAddress = addressRepository.save(address);
                System.out.println("--> Sucursal precargada para la comuna: " + commune.getCommuneName());
            }

            if (defaultAddress == null || "cerrillos".equalsIgnoreCase(commune.getCommuneName())) {
                defaultAddress = communeAddress;
            }
        }

        return defaultAddress;
    }

    private UserRole createRoleIfNotFound(String roleName) {
        return roleRepository.findByNameRole(roleName)
                .orElseGet(() -> {
                    UserRole newRole = new UserRole();
                    newRole.setNameRole(roleName);
                    return roleRepository.save(newRole);
                });
    }
}
