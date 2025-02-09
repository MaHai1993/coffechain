package com.kuro.coffechain.config;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import org.springframework.context.annotation.Configuration;

public class CRLFLogConverter extends ClassicConverter {
    @Override
    public String convert(ILoggingEvent event) {
        return System.lineSeparator();
    }
}
