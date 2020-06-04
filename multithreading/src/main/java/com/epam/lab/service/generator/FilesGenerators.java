package com.epam.lab.service.generator;

import com.epam.lab.model.News;
import com.epam.lab.model.News_;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public enum FilesGenerators {
    VALID(true),
    NOT_VALID_JSON(false),
    NOT_VALID_FIELD_NAME(false) {
        @Override
        public void generate(Path path, int count) throws IOException {
            for (int i = 0; i < count; i++) {
                List<Map> list = new ArrayList<>();
                for (int x = 0; x < OBJECTS_PER_FILE; x++) {
                    Map<String, String> jsonObject = new HashMap<>();
                    boolean isMiddle = x == OBJECTS_PER_FILE / 2;
                    jsonObject.put(News_.TITLE + (isMiddle ? FIELD_NAME_SPOILER : ""),
                            FAKE_VALUES_SERVICE.regexify(NewsFieldsRegex.TITLE.regex) + NEWS_COUNTER.incrementAndGet());
                    jsonObject.put(News_.SHORT_TEXT + (isMiddle ? FIELD_NAME_SPOILER : ""),
                            FAKE_VALUES_SERVICE.regexify(NewsFieldsRegex.SHORT_TEXT.regex));
                    jsonObject.put(News_.FULL_TEXT + (isMiddle ? FIELD_NAME_SPOILER : ""),
                            FAKE_VALUES_SERVICE.regexify(NewsFieldsRegex.FULL_TEXT.regex));
                    list.add(jsonObject);
                }
                createAndWriteFile(path, list.toArray(), this);
            }
        }
    },
    NOT_VALID_BEAN(false),
    NOT_VALID_DB_CONSTR(false);

    public static final int OBJECTS_PER_FILE = 3;
    private static final FakeValuesService FAKE_VALUES_SERVICE = new FakeValuesService(new Locale("en-GB"), new RandomService());
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final AtomicLong NEWS_COUNTER = new AtomicLong();
    private static final AtomicLong FILES_COUNTER = new AtomicLong();
    private static final String FILE_NAME_FORMAT = "news%s_%d.json";
    private static final String FIELD_NAME_SPOILER = "_random";

    private final boolean isValid;

    FilesGenerators(boolean isValid) {
        this.isValid = isValid;
    }


    public void generate(Path path, int count) throws IOException {
        for (int i = 0; i < count; i++) {
            News[] news = new News[OBJECTS_PER_FILE];
            for (int x = 0; x < OBJECTS_PER_FILE; x++) {
                news[x] = createNews(this, x == OBJECTS_PER_FILE / 2);
            }
            createAndWriteFile(path, news, this);
        }
    }


    private static News createNews(FilesGenerators option, boolean isMiddle) {
        return News.builder()
                .title(FAKE_VALUES_SERVICE.regexify(option.equals(FilesGenerators.NOT_VALID_DB_CONSTR) && isMiddle
                        ? NewsFieldsRegex.NOT_VALID_LENGTH_TITLE.regex
                        : NewsFieldsRegex.TITLE.regex)
                        + NEWS_COUNTER.incrementAndGet())
                .shortText(FAKE_VALUES_SERVICE.regexify(NewsFieldsRegex.SHORT_TEXT.regex))
                .fullText(option.equals(FilesGenerators.NOT_VALID_BEAN) && isMiddle
                        ? null
                        : FAKE_VALUES_SERVICE.regexify(NewsFieldsRegex.FULL_TEXT.regex))
                .build();
    }

    private static void createAndWriteFile(Path path, Object[] news, FilesGenerators option) throws IOException {

        Path filePath = Files.write(
                Paths.get(path +
                        FileSystems.getDefault().getSeparator() +
                        String.format(FILE_NAME_FORMAT, option.isValid ? "_valid" : "_notValid", FILES_COUNTER.incrementAndGet()))
                , option.equals(NOT_VALID_JSON)
                        ? Arrays.toString(news).getBytes(StandardCharsets.UTF_8)
                        : MAPPER.writeValueAsBytes(news)
                , StandardOpenOption.CREATE);

    }

    private enum NewsFieldsRegex {
        TITLE("[a-zA-Z]{20}_"),
        SHORT_TEXT("[a-zA-Z1-9,. ]{25}"),
        FULL_TEXT("[a-zA-Z1-9,. ]{500}"),
        NOT_VALID_LENGTH_TITLE("[a-zA-Z]{40}_");

        private final String regex;

        NewsFieldsRegex(String regex) {
            this.regex = regex;
        }
    }
}
