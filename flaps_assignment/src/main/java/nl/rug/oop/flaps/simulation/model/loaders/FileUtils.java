package nl.rug.oop.flaps.simulation.model.loaders;

import lombok.extern.java.Log;
import org.apache.commons.lang3.ClassUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author T.O.W.E.R.
 */
@Log
public class FileUtils {

    public static final Map<Class<?>, Function<Object, Object>> customDeserializers = new HashMap<>();

    static {
        customDeserializers.put(LocalDateTime.class,
                o -> LocalDateTime.parse((String) o, DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        customDeserializers.put(boolean.class, o -> ((int) o) == 1);
        customDeserializers.put(Boolean.class, o -> ((int) o) == 1);
    }

    public static List<Path> findMatches(Path directory, String pattern) throws IOException {
        var matcher = FileSystems.getDefault().getPathMatcher(pattern);
        try (var paths = Files.walk(directory)) {
            return paths.filter(matcher::matches).collect(Collectors.toList());
        }
    }

    public static Optional<Path> findMatch(Path directory, String pattern) throws IOException {
        var matcher = FileSystems.getDefault().getPathMatcher(pattern);
        try (var paths = Files.walk(directory)) {
            return paths.filter(matcher::matches).findAny();
        } catch (NullPointerException e) {
            return Optional.empty();
        }
    }

    public static Path findMatchOrThrow(Path directory, String pattern) throws IOException {
        return findMatch(directory, pattern)
                .orElseThrow(() -> new IOException("No match found for pattern " + pattern + " in directory " + directory + "."));
    }

    public static String toSnakeCase(String camelCase) {
        Matcher m = Pattern.compile("(?<=[a-z])[A-Z]").matcher(camelCase);
        return m.replaceAll(match -> "_" + match.group().toLowerCase());
    }

    public static boolean isFieldPrimitiveDeserializable(Field field) {
        return ClassUtils.isPrimitiveOrWrapper(field.getType()) ||
                field.getType() == String.class ||
                customDeserializers.containsKey(field.getType());
    }

    public static List<Field> getAllFields(Class<?> type) {
        List<Field> fields = new ArrayList<>();
        for (Class<?> c = type; c != null; c = c.getSuperclass()) {
            fields.addAll(Arrays.asList(c.getDeclaredFields()));
        }
        return fields;
    }

    public  static String toNiceCase(String camelCase) {
        Matcher m = Pattern.compile("(?<=[a-z])[A-Z]").matcher(camelCase);
        return m.replaceAll(match -> " " + match.group().toLowerCase());
    }
}
