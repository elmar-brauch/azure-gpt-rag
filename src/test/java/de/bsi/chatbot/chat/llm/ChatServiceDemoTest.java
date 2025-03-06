package de.bsi.chatbot.chat.llm;

import org.junit.jupiter.api.Disabled;
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

    @Disabled
    @Test
    void notWorkingTest() {
        var aiResponse = chatService.chat("Hello, how are you?");
        assertEquals("TODO Copy first response", aiResponse);
    }

}
