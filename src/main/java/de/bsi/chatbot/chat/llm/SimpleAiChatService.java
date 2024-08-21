package de.bsi.chatbot.chat.llm;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.azure.openai.AzureOpenAiChatModel;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SimpleAiChatService {

    private final AzureOpenAiChatModel chatModel;

    public Message chat(String message) {
        var userMessage = new UserMessage(message);
        var prompt = new Prompt(userMessage);
        ChatResponse aiResponse = chatModel.call(prompt);
        return aiResponse.getResult().getOutput();
    }

}
