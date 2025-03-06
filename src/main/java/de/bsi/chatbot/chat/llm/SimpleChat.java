package de.bsi.chatbot.chat.llm;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.stereotype.Service;

@Service
public class SimpleChat {

    private final ChatClient chatClient;

    public SimpleChat(ChatModel chatModel) {
        this.chatClient = ChatClient.builder(chatModel).build();
    }

    public String chat(String message) {
        return chatClient.prompt(message).call().content();
    }

}
