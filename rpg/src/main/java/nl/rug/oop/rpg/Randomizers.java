package nl.rug.oop.rpg;
import java.util.*;
import java.security.SecureRandom;

public class Randomizers {
    /**
     * Generate a random id for the room
     */
    private static final SecureRandom rand = new SecureRandom();
    private final char[] strand;
    private final Random random;
    private final char[] chars;
    public static final String upcase = "ABGHLMNOPQRUVWXYZ";
    public static final String lowcase = upcase.toLowerCase(Locale.ROOT);
    public static final String digits = "1234567890";
    public static final String allchar = upcase + lowcase + digits;
    public Randomizers(int length, Random random, String symbols) {
        if (length < 1)
            throw new IllegalArgumentException();
        if (symbols.length() < 2)
            throw new IllegalArgumentException();
        this.random = Objects.requireNonNull(random);
        this.chars = symbols.toCharArray();
        this.strand = new char[length];
    }
    public String generateId() {
        for (int idx = 0; idx < strand.length; ++idx)
            strand[idx] = chars[random.nextInt(chars.length)];
        return new String(strand);
    }
    public static <T extends Enum<?>> T randomMtrl(Class<T> set) {
        int x = rand.nextInt(set.getEnumConstants().length);
        return set.getEnumConstants()[x];
    }
    public Randomizers(int length, Random random) {
        this(length, random, allchar);
    }

    public boolean itemInsert(String in) {
        return in.matches("^[0-9|a-z].*$");
    }
}