package com.mozcalti.gamingapp.utils;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public final class CollectionUtils {

    private CollectionUtils() {
        throw new IllegalStateException("Utility CollectionUtils");
    }

    public static List<Integer> getRandomNumbers(List<Integer> idEquipos) throws NoSuchAlgorithmException {

        Collections.sort(idEquipos);

        int min = idEquipos.get(0);
        int max = idEquipos.get(idEquipos.size()-1);

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
        return random.nextInt((max - min) + 1) + min;
    }

}
