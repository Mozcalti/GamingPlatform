package com.mozcalti.gamingapp.controller;

import com.mozcalti.gamingapp.model.dto.ActivationTokenDto;
import com.mozcalti.gamingapp.model.dto.UsuarioDTO;
import com.mozcalti.gamingapp.model.security.VerificationToken;
import com.mozcalti.gamingapp.service.usuarios.VerificationTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class RegistrationController {
    private final VerificationTokenService verificationTokenService;

    @PostMapping("/verifyToken")
    public ResponseEntity<String> verifyToken(@RequestParam("token") String token) {
        Optional<VerificationToken> maybeVerificationToken = verificationTokenService.getVerificationToken(token);

        log.debug("maybeVerificationToken: {}", maybeVerificationToken);

        Optional<String> tokenErrors = verificationTokenService.findTokenErrors(maybeVerificationToken);

        if (tokenErrors.isPresent()) {
            String error = tokenErrors.get();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }

        return ResponseEntity.ok("");
    }

    @PostMapping("/registrationConfirm")
    public ResponseEntity<Optional<UsuarioDTO>> confirmRegistration(@RequestBody @Valid ActivationTokenDto activationToken) {

        if (verificationTokenService.findTokenErrors(verificationTokenService.getVerificationToken(activationToken.token())).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        return ResponseEntity.ok(verificationTokenService.activateUserAccount(activationToken.token(), activationToken.password()));
    }


    @PostMapping("/resetPassword")
    public ResponseEntity<String> resetPassword(@RequestParam("email") String email){

        verificationTokenService.resetPassword(email);

        return ResponseEntity.ok("");

    }



}
