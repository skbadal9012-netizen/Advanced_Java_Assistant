package com.assistant;

import com.assistant.controller.PCController;
import com.assistant.engine.KnowledgeEngine;
import com.assistant.model.Intent;
import com.assistant.model.ParsedCommand;
import com.assistant.parser.CommandParser;
import com.assistant.parser.IntentRecognizer;
import com.assistant.ui.AssistantUI;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            IntentRecognizer intentRecognizer = new IntentRecognizer();
            CommandParser commandParser = new CommandParser();
            PCController pcController = new PCController();
            KnowledgeEngine knowledgeEngine = new KnowledgeEngine();

            AssistantUI ui = new AssistantUI(userInput -> {
                if (userInput == null || userInput.trim().isEmpty()) {
                    return "Please enter a command or query.";
                }

                Intent intent = intentRecognizer.recognize(userInput);
                ParsedCommand command = commandParser.parse(userInput, intent);

                return processCommand(command, pcController, knowledgeEngine);
            });

            ui.setVisible(true);
        });
    }

    private static String processCommand(ParsedCommand cmd, PCController pcController, KnowledgeEngine knowledgeEngine) {
        Intent intent = cmd.getIntent();
        String param = cmd.getParameter();

        switch (intent) {
            case GREETING:
                return knowledgeEngine.getGreeting();

            case OPEN_APP:
                return pcController.openApplication(param);

            case CLOSE_APP:
                return pcController.closeApplication(param);

            case OPEN_WEBSITE:
                return pcController.openWebsite(param);

            case TYPE_TEXT:
                return pcController.typeText(param);

            case SYSTEM_COMMAND:
                return pcController.executeSystemCommand(param);

            case VOLUME_UP:
                return pcController.adjustVolume("up");

            case VOLUME_DOWN:
                return pcController.adjustVolume("down");

            case VOLUME_MUTE:
                return pcController.adjustVolume("mute");

            case TAKE_SCREENSHOT:
                return pcController.takeScreenshot();

            case SEARCH_WEB:
                return pcController.searchWeb(param);

            case KNOWLEDGE:
                return knowledgeEngine.query(param);

            case UNKNOWN:
            default:
                String knowledgeResult = knowledgeEngine.query(cmd.getRawInput());
                if (knowledgeResult != null && !knowledgeResult.toLowerCase().contains("don't have information")) {
                    return knowledgeResult;
                }
                return pcController.searchWeb(cmd.getRawInput());
        }
    }
}
