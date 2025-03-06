package de.bsi.chatbot.chat.llm;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("local")
public class ChatServiceDemoTest {

    @Autowired
    private SimpleChat chatService;

    @Test
    void notWorkingTest() {
        var aiResponse = chatService.chat("Hello, tell me a joke about AI Barcamps in Bonn. Use only 1 line.");
        // Answer of 06.03.2025.
        assertEquals("Why did the AI Barcamp in Bonn get so crowded? Because everyone wanted to \"byte\" into the latest tech!", aiResponse);
    }

}
