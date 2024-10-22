package de.bsi.chatbot.chat.llm;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;

@Service
public class SimpleAiChatService {

    private final ChatClient chatClient;

    public SimpleAiChatService(ChatModel chatModel) {
        this.chatClient = ChatClient.builder(chatModel).build();
    }

    public Message chat(String message) {
        var userMessage = new UserMessage(message);
        var prompt = new Prompt(userMessage);
        ChatResponse aiResponse = chatClient.prompt(prompt).call().chatResponse();
        return aiResponse.getResult().getOutput();
    }

}
