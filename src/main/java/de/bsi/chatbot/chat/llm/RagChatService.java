package de.bsi.chatbot.chat.llm;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.azure.openai.AzureOpenAiChatModel;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RagChatService {

    private final AzureOpenAiChatModel chatModel;
    private final VectorStore vectorStore;

    public Message chat(String message) {
        var userMessage = new UserMessage(message);
        Message systemMessage = buildContextMessage(message);
        var prompt = new Prompt(List.of(systemMessage, userMessage));
        ChatResponse aiResponse = chatModel.call(prompt);
        return aiResponse.getResult().getOutput();
    }

    private Message buildContextMessage(String message) {
        log.debug("Searching similar documents for: {}", message);
        String similarDocuments = vectorStore.similaritySearch(message)
                .stream()
                .map(Document::getContent)
                .collect(Collectors.joining(System.lineSeparator()));
        return template.createMessage(Map.of("documents", similarDocuments));
    }

    private static final SystemPromptTemplate template = new SystemPromptTemplate("""
            Du assistierst bei Fragen zum Internetanschluss.
            
            Verwende die Informationen aus dem Abschnitt DOKUMENTE, um genaue Antworten zu geben,
            aber tu so, als ob du diese Informationen von Natur aus wüsstest.
            Wenn du dir nicht sicher bist, gib einfach an, dass du es nicht weißt.
            
            DOKUMENTE:
            {documents}
            
            """);

}