package com.assistant.parser;

import com.assistant.model.Intent;
import com.assistant.model.ParsedCommand;
import java.util.Locale;

public class CommandParser {

    public static ParsedCommand parse(String input) {
        Intent intent = IntentRecognizer.recognize(input);
        ParsedCommand cmd = new ParsedCommand(intent, input);

        if (input == null || input.trim().isEmpty()) {
            return cmd;
        }

        String lower = input.toLowerCase(Locale.ROOT).trim();

        switch (intent) {
            case LAUNCH_APP:
                cmd.setParameter("app", extractAppName(input, lower));
                break;
            case TYPE_TEXT:
                cmd.setParameter("text", extractTextToType(input, lower));
                break;
            case OPEN_WEBSITE:
                cmd.setParameter("url", extractUrl(input, lower));
                break;
            case SYSTEM_COMMAND:
                cmd.setParameter("command", extractSystemCommand(lower));
                break;
            case KNOWLEDGE_QUERY:
                cmd.setParameter("query", input.trim());
                break;
            default:
                break;
        }

        return cmd;
    }

    private static String extractAppName(String original, String lower) {
        String app = original.trim();
        // Remove trigger words
        String[] triggers = {"open", "launch", "run", "start"};
        for (String trigger : triggers) {
            if (lower.startsWith(trigger)) {
                app = app.substring(trigger.length()).trim();
                break;
            }
        }
        return app;
    }

    private static String extractTextToType(String original, String lower) {
        String text = original.trim();
        String[] triggers = {"type this", "type", "write", "enter text"};
        for (String trigger : triggers) {
            if (lower.startsWith(trigger)) {
                text = text.substring(trigger.length()).trim();
                break;
            }
        }
        return text;
    }

    private static String extractUrl(String original, String lower) {
        String url = original.trim();
        String[] triggers = {"open site", "go to", "browse to", "browse"};
        for (String trigger : triggers) {
            if (lower.startsWith(trigger)) {
                url = url.substring(trigger.length()).trim();
                break;
            }
        }

        // Clean up URL format
        if (!url.toLowerCase().startsWith("http://") && !url.toLowerCase().startsWith("https://")) {
            // Check if it's a domain name or query
            if (url.contains(".") && !url.contains(" ")) {
                url = "https://" + url;
            } else {
                // If it looks like a query search, we'll format as google search
                try {
                    url = "https://www.google.com/search?q=" + java.net.URLEncoder.encode(url, "UTF-8");
                } catch (Exception e) {
                    url = "https://www.google.com/search?q=" + url;
                }
            }
        }
        return url;
    }

    private static String extractSystemCommand(String lower) {
        if (lower.contains("shutdown")) return "shutdown";
        if (lower.contains("restart")) return "restart";
        if (lower.contains("lock screen") || lower.contains("lock pc")) return "lock";
        if (lower.contains("sleep")) return "sleep";
        if (lower.contains("minimize")) return "minimize";
        if (lower.contains("maximize")) return "maximize";
        return "unknown";
    }
}
