package org.woonyong.lotto.core.util;

import static org.woonyong.lotto.core.constant.LottoConstants.*;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class LottoNumberGenerator {

  public static List<Integer> generateRandomNumbers() {
    return generateRandomNumbers(Collections.emptyList(), LOTTO_NUMBERS_COUNT);
  }

  public static List<Integer> generateRandomNumbers(final int excludeNumber) {
    return generateRandomNumbers(List.of(excludeNumber), LOTTO_NUMBERS_COUNT);
  }

  public static List<Integer> generateRandomNumbers(final List<Integer> excludeNumbers) {
    return generateRandomNumbers(excludeNumbers, LOTTO_NUMBERS_COUNT);
  }

  public static int generateRandomNumber() {
    return generateRandomNumbers(Collections.emptyList(), 1).getFirst();
  }

  public static int generateRandomNumber(final int excludeNumber) {
    return generateRandomNumbers(List.of(excludeNumber), 1).getFirst();
  }

  public static int generateRandomNumber(final List<Integer> excludeNumbers) {
    return generateRandomNumbers(excludeNumbers, 1).getFirst();
  }

  private static List<Integer> generateRandomNumbers(
      final List<Integer> excludeNumbers, final int count) {
    List<Integer> availableNumbers = createAvailableNumbers(excludeNumbers);
    return generateFromAvailableNumbers(availableNumbers, count);
  }

  private static List<Integer> createAvailableNumbers(final List<Integer> excludeNumbers) {
    return IntStream.rangeClosed(MIN_NUMBER, MAX_NUMBER)
        .filter(number -> !excludeNumbers.contains(number))
        .boxed()
        .collect(Collectors.toList());
  }

  private static List<Integer> generateFromAvailableNumbers(
      final List<Integer> availableNumbers, final int count) {
    Collections.shuffle(availableNumbers, new Random(createSeed()));
    return availableNumbers.stream().limit(count).sorted().collect(Collectors.toList());
  }

  private static long createSeed() {
    return createSeed(System.nanoTime(), System.currentTimeMillis());
  }

  private static long createSeed(final long seed, final long salt) {
    return ((seed >>> 32) ^ (seed & 0xFFFFFFFFL)) ^ salt;
  }

  private LottoNumberGenerator() {}
}
