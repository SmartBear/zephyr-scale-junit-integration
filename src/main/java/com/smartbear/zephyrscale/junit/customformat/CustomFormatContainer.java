package com.smartbear.zephyrscale.junit.customformat;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
public class CustomFormatContainer {

	@Default
    private Integer version = 1;

    private List<CustomFormatExecution> executions;
    
 // To mitigate javadoc compile time issue for lombok
    // https://stackoverflow.com/questions/51947791/javadoc-cannot-find-symbol-error-when-using-lomboks-builder-annotation
    public static class CustomFormatContainerBuilder {}
}
