package de.bsi.chatbot.chat.llm;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ChatController {

    private final SimpleAiChatService simpleAiChatService;
    private final RagChatService ragChatService;
    private final ChatService ragWithFunctionChatService;

    @PostMapping("/chat")
    public String chat(@RequestBody ChatRequestDTO chatRequest) {
        log.debug("Following chat message with chatId {} received: {}", chatRequest.chatId(), chatRequest.message());
        // TODO Change service for demo
        var aiResponse = simpleAiChatService.chat(chatRequest.message());
        log.debug("AI response: {}", aiResponse.getContent());
        return aiResponse.getContent();
    }

}
