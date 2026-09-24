package com.assistant.model;

import java.util.HashMap;
import java.util.Map;

public class ParsedCommand {
    private final Intent intent;
    private final String rawInput;
    private final Map<String, String> parameters;

    public ParsedCommand(Intent intent, String rawInput) {
        this.intent = intent;
        this.rawInput = rawInput;
        this.parameters = new HashMap<>();
    }

    public Intent getIntent() {
        return intent;
    }

    public String getRawInput() {
        return rawInput;
    }

    public void setParameter(String key, String value) {
        parameters.put(key, value);
    }

    public String getParameter(String key) {
        return parameters.get(key);
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    @Override
    public String toString() {
        return "ParsedCommand{" +
                "intent=" + intent +
                ", rawInput='" + rawInput + '\'' +
                ", parameters=" + parameters +
                '}';
    }
}
