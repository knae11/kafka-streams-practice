package com.deliverystreams;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.Formatter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {

        registry.addFormatter(new LocalDateFormatter());
        registry.addFormatter(new LocalTimeFormatter());
    }

    static class LocalDateFormatter implements Formatter<LocalDate> {

        @Override
        public LocalDate parse(String text, Locale locale) throws ParseException {
            return LocalDate.parse(text, DateTimeFormatter.ISO_LOCAL_DATE);
        }

        @Override
        public String print(LocalDate object, Locale locale) {
            return DateTimeFormatter.ISO_LOCAL_DATE.format(object);
        }
    }


    static class LocalTimeFormatter implements Formatter<LocalTime> {


        @Override
        public LocalTime parse(String text, Locale locale) throws ParseException {
            return LocalTime.parse(text, DateTimeFormatter.ISO_LOCAL_TIME);
        }

        @Override
        public String print(LocalTime object, Locale locale) {
            return DateTimeFormatter.ISO_LOCAL_TIME.format(object);
        }
    }
}