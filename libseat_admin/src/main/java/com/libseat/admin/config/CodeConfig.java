package com.libseat.admin.config;

import com.libseat.utils.utils.CodeGenerateUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CodeConfig {

    @Bean
    public CodeGenerateUtils getCodeGenerateUtils() {
        return new CodeGenerateUtils();
    }
}
