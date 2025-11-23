package org.woonyong.lotto.core.util;

import org.woonyong.lotto.core.constant.LottoConstants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class LottoNumberGenerator {

    public static List<Integer> generateRandomNumbers() {
        return generateRandomNumbers(1).getFirst();
    }

    public static List<List<Integer>> generateRandomNumbers(int count) {
        List<List<Integer>> tickets = new ArrayList<>();
        List<Integer> baseNumbers = createBaseNumbers();
        long seed = createSeed();

        for (int i = 0; i < count; i++) {
            tickets.add(generateSingleTicket(new ArrayList<>(baseNumbers), seed));
            seed = createSeed(seed);
        }

        return tickets;
    }

    private static List<Integer> createBaseNumbers() {
        return IntStream.rangeClosed(
                        LottoConstants.MIN_NUMBER,
                        LottoConstants.MAX_NUMBER)
                .boxed()
                .collect(Collectors.toList());
    }

    private static List<Integer> generateSingleTicket(List<Integer> numbers, long seed) {
        Collections.shuffle(numbers, new Random(seed));
        return numbers.stream()
                .limit(LottoConstants.NUMBERS_COUNT)
                .sorted()
                .collect(Collectors.toList());
    }

    private static long createSeed() {
        return createSeed(System.nanoTime());
    }

    private static long createSeed(long previousSeed) {
        return createSeed(createSeed(), previousSeed);
    }

    private static long createSeed(long seed, long salt) {
        return ((seed >>> 32) ^ (seed & 0xFFFFFFFFL)) ^ salt;
    }

    private LottoNumberGenerator() {
    }
}