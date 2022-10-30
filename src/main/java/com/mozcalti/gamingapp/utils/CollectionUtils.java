package com.mozcalti.gamingapp.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CollectionUtils {

    public static List<Integer> getRandomNumbers(Set<Integer> idEquipos) throws NoSuchAlgorithmException {

        List<Integer> numbers = new ArrayList<>(idEquipos);
        Collections.sort(numbers);

        int min = numbers.get(0);
        int max = numbers.get(numbers.size()-Numeros.UNO.getNumero());

        List<Integer> randomNumbers = new ArrayList<>();

        while (randomNumbers.size() < idEquipos.size()) {
            int random = getRandomInt(min, max);

            if (!randomNumbers.contains(random)) {
                randomNumbers.add(random);
            }
        }

        return randomNumbers;
    }

    private static int getRandomInt(int min, int max) throws NoSuchAlgorithmException {
        Random random = SecureRandom.getInstanceStrong();
        return random.nextInt((max - min) + Numeros.UNO.getNumero()) + min;
    }

}
