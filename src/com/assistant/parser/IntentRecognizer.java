package com.assistant.parser;

import com.assistant.model.Intent;
import java.util.Locale;

public class IntentRecognizer {

    public static Intent recognize(String input) {
        if (input == null || input.trim().isEmpty()) {
            return Intent.UNKNOWN;
        }

        String lower = input.toLowerCase(Locale.ROOT).trim();

        // Help commands
        if (lower.equals("help") || lower.equals("menu") || lower.contains("what can you do") || lower.contains("guide")) {
            return Intent.HELP;
        }

        // Screen capturing
        if (lower.contains("screenshot") || lower.contains("capture screen") || lower.contains("take a shot") || lower.contains("snap screen")) {
            return Intent.TAKE_SCREENSHOT;
        }

        // Website launching
        if (lower.startsWith("open site") || lower.startsWith("go to") || lower.contains("website") || lower.contains(".com") || lower.contains(".org") || lower.contains("www.") || lower.contains("browse")) {
            return Intent.OPEN_WEBSITE;
        }

        // App launching
        if (lower.startsWith("open") || lower.startsWith("launch") || lower.startsWith("run") || lower.startsWith("start")) {
            // Check if it's more of a website (contains .com etc)
            if (lower.contains(".com") || lower.contains("http") || lower.contains("www.")) {
                return Intent.OPEN_WEBSITE;
            }
            return Intent.LAUNCH_APP;
        }

        // Typing text
        if (lower.startsWith("type") || lower.startsWith("write") || lower.startsWith("enter text") || lower.contains("type this")) {
            return Intent.TYPE_TEXT;
        }

        // System actions (shutdown, restart, volume, lock, etc.)
        if (lower.contains("shutdown") || lower.contains("restart") || lower.contains("sleep mode") || lower.contains("lock screen") || lower.contains("lock pc") || lower.contains("minimize") || lower.contains("maximize")) {
            return Intent.SYSTEM_COMMAND;
        }

        // Fallback to Knowledge query or Unknown
        // If it looks like a question or informational statement, assume Knowledge Query
        if (lower.contains("who") || lower.contains("what") || lower.contains("how") || lower.contains("why") || lower.contains("when") || lower.contains("where") || lower.contains("tell me") || lower.contains("define") || lower.contains("search") || lower.contains("calculate") || lower.contains("weather")) {
            return Intent.KNOWLEDGE_QUERY;
        }

        // Default: search general query/chat
        return Intent.KNOWLEDGE_QUERY;
    }
}
