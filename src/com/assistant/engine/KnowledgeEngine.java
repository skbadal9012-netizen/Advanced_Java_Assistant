package com.assistant.engine;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class KnowledgeEngine {

    public String query(String input) {
        if (input == null || input.trim().isEmpty()) {
            return "Please ask me something, I am listening!";
        }

        String lower = input.toLowerCase(Locale.ROOT).trim();

        // Greetings
        if (lower.matches("hello|hi|hey|greetings|yo|hello assistant")) {
            return "Hello there! I am your Advanced Java Assistant. I can control your PC, launch apps, type text, open websites, or answer your questions. How can I help you today?";
        }
        
        if (lower.contains("who are you") || lower.contains("your name") || lower.contains("what is your name")) {
            return "I am Maya, an Advanced Java Assistant built to control your PC and answer questions instantly!";
        }

        if (lower.contains("how are you")) {
            return "I am running smoothly and ready to take your commands! How can I assist you on your PC today?";
        }

        // Math / Calculations
        String mathResult = solveSimpleMath(lower);
        if (mathResult != null) {
            return mathResult;
        }

        // PC Capability Info
        if (lower.contains("what can you do") || lower.contains("help") || lower.contains("capabilities") || lower.contains("features")) {
            return "Here is what I can do for you:\n" +
                   "1. Launch Apps: 'open Notepad', 'run Calculator', 'start chrome'\n" +
                   "2. Type Text: 'type hello world' (automatically types into the active window)\n" +
                   "3. Open Websites: 'go to google.com', 'open site github.com'\n" +
                   "4. System Controls: 'lock screen', 'sleep mode', 'take a screenshot'\n" +
                   "5. Knowledge Query: Ask me general questions, math problems, or tech facts!";
        }

        // Java Questions
        if (lower.contains("java") && (lower.contains("what is") || lower.contains("define") || lower.contains("explain"))) {
            return "Java is a popular, class-based, object-oriented programming language designed to have as few implementation dependencies as possible. It runs on billions of devices worldwide using the Java Virtual Machine (JVM).";
        }

        if (lower.contains("jvm") || lower.contains("java virtual machine")) {
            return "The Java Virtual Machine (JVM) is an engine that provides a runtime environment to drive Java code or applications. It converts Java bytecode into machine language.";
        }

        if (lower.contains("oop") || lower.contains("object oriented")) {
            return "Object-Oriented Programming (OOP) is a programming paradigm based on the concept of 'objects', which contain data (attributes) and code (methods). The 4 pillars of OOP are Inheritance, Polymorphism, Encapsulation, and Abstraction.";
        }

        // General knowledge facts
        if (lower.contains("sky") && lower.contains("blue")) {
            return "The sky is blue because of a phenomenon called Rayleigh scattering. Sunlight reaches Earth's atmosphere and is scattered in all directions by all the gases and particles in the air. Blue light is scattered more than the other colors because it travels as shorter, smaller waves.";
        }

        if (lower.contains("capital of india")) {
            return "The capital of India is New Delhi.";
        }

        if (lower.contains("capital of usa") || lower.contains("capital of united states")) {
            return "The capital of the United States is Washington, D.C.";
        }

        if (lower.contains("speed of light")) {
            return "The speed of light in a vacuum is approximately 299,792 kilometers per second (about 186,282 miles per second).";
        }

        if (lower.contains("internet") && (lower.contains("what is") || lower.contains("define"))) {
            return "The Internet is a global network of billions of computers and other electronic devices. It makes it possible to access almost any information, communicate with anyone else in the world, and do much more.";
        }

        // Default: Mock Search engine / Generative Response
        return "Based on my knowledge base: \"" + input + "\"\n" + 
               "I found that this is related to general informatics. To search live results on the web, " +
               "you can tell me to: 'open site google.com/search?q=" + java.net.URLEncoder.encode(input) + "'.";
    }

    private String solveSimpleMath(String input) {
        // Simple regex to match "calculate 4 + 5", "what is 100 * 2", "50 / 2"
        Pattern pattern = Pattern.compile(".*?(\\d+)\\s*([+\\-*/])\\s*(\\d+).*");
        Matcher matcher = pattern.matcher(input);
        if (matcher.matches()) {
            try {
                double num1 = Double.parseDouble(matcher.group(1));
                String op = matcher.group(2);
                double num2 = Double.parseDouble(matcher.group(3));
                double res = 0;

                switch (op) {
                    case "+": res = num1 + num2; break;
                    case "-": res = num1 - num2; break;
                    case "*": res = num1 * num2; break;
                    case "/": 
                        if (num2 == 0) return "Error: Division by zero is not allowed.";
                        res = num1 / num2; 
                        break;
                }
                
                // Format nicely (avoid decimal point for whole numbers)
                if (res == (long) res) {
                    return "Result: " + (long) res;
                } else {
                    return "Result: " + res;
                }
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }
}
