package uga.cs4370.sleighlist.config;

import com.samskivert.mustache.Mustache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Makes templates forgiving: a missing or null value renders as an empty
 * string instead of throwing, and empty strings / zero count as "false"
 * in {{#section}} blocks.
 */
@Configuration
public class MustacheConfig {

    @Bean
    public Mustache.Compiler mustacheCompiler(Mustache.TemplateLoader mustacheTemplateLoader) {
        return Mustache.compiler()
                .withLoader(mustacheTemplateLoader)
                .defaultValue("")
                .nullValue("")
                .emptyStringIsFalse(true)
                .zeroIsFalse(true);
    }
}
