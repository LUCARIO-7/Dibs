package org.me.dibs;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.Optional;

public class TestResultLoggerExtension implements TestWatcher {
    private static final Logger logger = LoggerFactory.getLogger(TestResultLoggerExtension.class);
    private static final String LOG_FILE = "target/test-results.log";

    static {
        try {
            Files.createDirectories(Paths.get("target"));
        } catch (IOException e) {
            logger.error("Failed to create target directory", e);
        }
    }

    private void logResult(String className, String methodName, String status, String detail) {
        String logMessage = String.format("[%s] TEST %s - %s.%s: %s", 
                LocalDateTime.now(), status, className, methodName, detail != null ? detail : "");
        
        // Log to console/SLF4J
        logger.info(logMessage);
        
        // Log to file
        try {
            Files.writeString(Paths.get(LOG_FILE), logMessage + System.lineSeparator(), 
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            logger.error("Failed to write to test-results.log", e);
        }
    }

    @Override
    public void testDisabled(ExtensionContext context, Optional<String> reason) {
        String className = context.getTestClass().map(Class::getSimpleName).orElse("UnknownClass");
        String methodName = context.getTestMethod().map(java.lang.reflect.Method::getName).orElse("UnknownMethod");
        logResult(className, methodName, "DISABLED", reason.orElse("No reason provided"));
    }

    @Override
    public void testSuccessful(ExtensionContext context) {
        String className = context.getTestClass().map(Class::getSimpleName).orElse("UnknownClass");
        String methodName = context.getTestMethod().map(java.lang.reflect.Method::getName).orElse("UnknownMethod");
        logResult(className, methodName, "SUCCESS", "Passed");
    }

    @Override
    public void testAborted(ExtensionContext context, Throwable cause) {
        String className = context.getTestClass().map(Class::getSimpleName).orElse("UnknownClass");
        String methodName = context.getTestMethod().map(java.lang.reflect.Method::getName).orElse("UnknownMethod");
        logResult(className, methodName, "ABORTED", cause != null ? cause.getMessage() : "Aborted");
    }

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        String className = context.getTestClass().map(Class::getSimpleName).orElse("UnknownClass");
        String methodName = context.getTestMethod().map(java.lang.reflect.Method::getName).orElse("UnknownMethod");
        logResult(className, methodName, "FAILED", cause != null ? cause.getClass().getName() + ": " + cause.getMessage() : "Failed");
    }
}
