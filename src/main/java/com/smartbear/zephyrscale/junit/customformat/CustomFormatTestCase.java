package com.smartbear.zephyrscale.junit.customformat;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonInclude(Include.NON_NULL)
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomFormatTestCase {
	
    private String key;

    private String name;
    
    // To mitigate javadoc compile time issue for lombok
    // https://stackoverflow.com/questions/51947791/javadoc-cannot-find-symbol-error-when-using-lomboks-builder-annotation
    public static class CustomFormatTestCaseBuilder {}
}
