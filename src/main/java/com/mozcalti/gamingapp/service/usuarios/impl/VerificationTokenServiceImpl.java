package com.mozcalti.gamingapp.service.usuarios.impl;

import com.mozcalti.gamingapp.model.Usuario;
import com.mozcalti.gamingapp.model.dto.UsuarioDTO;
import com.mozcalti.gamingapp.model.security.VerificationToken;
import com.mozcalti.gamingapp.repository.VerificationTokenRepository;
import com.mozcalti.gamingapp.service.usuarios.UsuarioService;
import com.mozcalti.gamingapp.service.usuarios.VerificationTokenService;
import com.mozcalti.gamingapp.utils.DateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VerificationTokenServiceImpl implements VerificationTokenService {

    @Value("${security.account.activation.token-validity}")
    private int tokenValidity;

    private final VerificationTokenRepository tokenRepository;
    private final UsuarioService usuarioService;

    private final PasswordEncoder passwordEncoder;
    @Override
    public void addVerificationToken(Usuario usuario, String token) {
        tokenRepository.save(new VerificationToken(token, usuario, LocalDate.now().plusDays(tokenValidity)));
    }

    @Override
    public Optional<VerificationToken> getVerificationToken(String token) {
        return tokenRepository.findByToken(token);
    }

    @Override
    public Optional<UsuarioDTO> activateUserAccount(String token, String plainPassword) {

        Optional<VerificationToken> maybeToken = tokenRepository.findByToken(token);

        if(maybeToken.isPresent()) {

            LocalDateTime now = DateUtils.now();

            VerificationToken verificationToken = maybeToken.get();

            Usuario usuario = verificationToken.getUsuario();

            verificationToken.setActivationDate(now);
            usuario.setHabilitado(true);
            usuario.setPassword(passwordEncoder.encode(plainPassword));

            tokenRepository.save(verificationToken);

            return Optional.of(new UsuarioDTO(usuarioService.update(usuario)));

        }

        return Optional.empty();
    }
}
