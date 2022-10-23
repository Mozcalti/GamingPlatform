package com.mozcalti.gamingapp.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@NoArgsConstructor(access = AccessLevel.NONE)
public final class ServerUtils {
    public static String getBaseUrl() {
        return ServletUriComponentsBuilder.fromCurrentRequest()
                .replacePath(null)
                .build()
                .toUriString();
    }
}
