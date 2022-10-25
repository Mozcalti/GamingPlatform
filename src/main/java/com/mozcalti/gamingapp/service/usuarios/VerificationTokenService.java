package com.mozcalti.gamingapp.service.usuarios;


import com.mozcalti.gamingapp.model.Usuario;
import com.mozcalti.gamingapp.model.dto.UsuarioDTO;
import com.mozcalti.gamingapp.model.security.VerificationToken;

import java.time.LocalDate;
import java.util.Optional;

public interface VerificationTokenService {
    void addVerificationToken(Usuario usuario, String token);

    Optional<VerificationToken> getVerificationToken(String token);

    Optional<UsuarioDTO> activateUserAccount(String token, String plainPassword);

    default Optional<String> findTokenErrors(Optional<VerificationToken> maybeVerificationToken) {

        if (maybeVerificationToken.isEmpty()) {
            return Optional.of("La información de activación solicitada no es válida");
        }

        VerificationToken verificationToken = maybeVerificationToken.get();

        if (!isTokenValid(verificationToken)) {
            return Optional.of("La información de activación de la cuenta solicitada ha expirado");
        }

        Usuario usuario = verificationToken.getUsuario();

        if (usuario.isHabilitado()) {
            return Optional.of("La información de activación de la cuenta ya ha se utilizó");
        }

        return Optional.empty();
    }

    default boolean isTokenValid(VerificationToken token) {
        return token.getExpiryDate().isAfter(LocalDate.now());
    }
}
