package com.smartbear.zephyrscale.junit.file;

import com.smartbear.zephyrscale.junit.customformat.CustomFormatContainer;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class CustomFormatFile {

    public static final String DEFAULT_ZEPHYRSCALE_RESULT_FILE_NAME = "zephyrscale_result.json";

    public static void generateCustomFormatFile(CustomFormatContainer customFormatContainer) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(DEFAULT_ZEPHYRSCALE_RESULT_FILE_NAME), customFormatContainer);
    }
}
