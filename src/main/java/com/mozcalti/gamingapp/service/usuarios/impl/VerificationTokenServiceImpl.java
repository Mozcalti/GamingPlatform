package com.mozcalti.gamingapp.service.usuarios.impl;

import com.mozcalti.gamingapp.exceptions.SendMailException;
import com.mozcalti.gamingapp.exceptions.UtilsException;
import com.mozcalti.gamingapp.exceptions.ValidacionException;
import com.mozcalti.gamingapp.model.Usuario;
import com.mozcalti.gamingapp.model.dto.UsuarioDTO;
import com.mozcalti.gamingapp.model.security.VerificationToken;
import com.mozcalti.gamingapp.repository.UsuarioRepository;
import com.mozcalti.gamingapp.repository.VerificationTokenRepository;
import com.mozcalti.gamingapp.service.correos.SendMailService;
import com.mozcalti.gamingapp.service.usuarios.UsuarioService;
import com.mozcalti.gamingapp.service.usuarios.VerificationTokenService;
import com.mozcalti.gamingapp.utils.DateUtils;
import com.mozcalti.gamingapp.utils.StackTraceUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class VerificationTokenServiceImpl implements VerificationTokenService {

    @Value("${security.account.activation.token-validity}")
    private int tokenValidity;
    private static final String MAIL_TEMPLATE_KEY = "reset";

    @Value("${server.baseUrl}/#")
    private String baseUrl;

    private final VerificationTokenRepository tokenRepository;
    private final UsuarioRepository usuarioRepository;
    private final UsuarioService usuarioService;
    private final SendMailService sendMailService;

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

    @Override
    public Optional<UsuarioDTO> resetPassword(String email) {
        Optional<Usuario> usuarioDB = usuarioRepository.findByEmail(email);
        if (usuarioDB.isPresent()){
            Optional<VerificationToken> mytoken = tokenRepository.findByUsuario(usuarioDB.get());
            if (mytoken.isPresent()){
                VerificationToken verificationToken = mytoken.get();
                Usuario usuario = verificationToken.getUsuario();
                usuario.setHabilitado(false);
                usuario.setPassword("<EMPTY>");
                verificationToken.setActivationDate(null);
                verificationToken.setToken(UUID.randomUUID().toString());
                verificationToken.setExpiryDate(LocalDate.now().plusDays(tokenValidity));
                tokenRepository.save(verificationToken);
                usuarioService.update(usuario);

                Map<String, Object> parameters = new HashMap<>();
                parameters.put("token", verificationToken.getToken());
                parameters.put("baseUrl", baseUrl);
                parameters.put("nombre", usuario.getNombre());
                parameters.put("apellido", usuario.getApellidos());

                try {
                    sendMailService.sendMail(usuario.getEmail(), MAIL_TEMPLATE_KEY, parameters);
                } catch (SendMailException | UtilsException e) {
                    log.error("Error al enviar el correo de cambio de contraseña: {}", StackTraceUtils.getCustomStackTrace(e));
                }

            }
        }else
            throw new ValidacionException("No existe el usuario el la base de datos");
        return Optional.empty();
    }
}
