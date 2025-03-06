package de.bsi.chatbot.chat.llm;

import de.bsi.chatbot.connectioncheck.ConnectionCheckService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RagChat {

    private final ChatClient chatClient;
    private final VectorStore vectorStore;

    public RagChat(ChatModel chatModel, VectorStore vectorStore) {
        this.chatClient = ChatClient.builder(chatModel).build();
        this.vectorStore = vectorStore;
    }

    public String chat(String message) {
        var userMessage = new UserMessage(message);
        var systemMessage = buildContextMessage(message);
        return chatClient.prompt()
                .messages(systemMessage, userMessage)
                .tools(new ConnectionCheckService())
                .call()
                .content();
    }

    private static final SystemPromptTemplate template = new SystemPromptTemplate("""
            Du assistierst bei Fragen zum Internetanschluss.
            
            Verwende die Informationen aus dem Abschnitt DOKUMENTE, um genaue Antworten zu geben,
            aber tu so, als ob du diese Informationen von Natur aus wüsstest.
            Wenn du dir nicht sicher bist, gib einfach an, dass du es nicht weisst.
            
            DOKUMENTE:
            {documents}
            """);

    private Message buildContextMessage(String message) {
        log.debug("Searching similar documents for: {}", message);
        String similarDocuments = vectorStore.similaritySearch(message)
                .stream()
                .map(Document::getText)
                .collect(Collectors.joining(System.lineSeparator()));
        return template.createMessage(Map.of("documents", similarDocuments));
    }

}
