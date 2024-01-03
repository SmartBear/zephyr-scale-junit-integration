package com.smartbear.zephyrscale.junit.file;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartbear.zephyrscale.junit.customformat.CustomFormatContainer;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access=AccessLevel.PRIVATE)
public class CustomFormatFile {

    public static final String DEFAULT_ZEPHYRSCALE_RESULT_FILE_NAME = "zephyrscale_result.json";

    public static void generateCustomFormatFile(CustomFormatContainer customFormatContainer) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(DEFAULT_ZEPHYRSCALE_RESULT_FILE_NAME), customFormatContainer);
    }
}
