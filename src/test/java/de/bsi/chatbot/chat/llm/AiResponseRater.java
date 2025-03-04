package de.bsi.chatbot.chat.llm;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AiResponseRater {

    private final ChatClient ratingClient;

    public AiResponseRater(ChatModel chatModel) {
        this.ratingClient = ChatClient.create(chatModel);
    }

    public Rating rateAiResponse(String userMessage, String expected, String actual) {
        var ratingMessage = new UserMessage(RATING_MESSAGE_TEMPLATE.formatted(actual, userMessage, expected));
        var systemMessage = new SystemPromptTemplate(RATING_SYSTEM_PROMPT).createMessage();

        return ratingClient.prompt()
                .messages(systemMessage, ratingMessage)
                .call()
                .entity(Rating.class);
    }

    private static final String RATING_MESSAGE_TEMPLATE = """
            AI response to be rated is: '%s'
            
            The user wrote this message to be answered by the AI: '%s'
            
            The expected response is: '%s'
            """;

    private static final String RATING_SYSTEM_PROMPT = """
            Your task is to rate the quality of an existing RAG chat bot.
            You will always get messages in this format:
            AI response to be rated is: PLACEHOLDER_AI_RESPONSE
            The user wrote this message to be answered by the AI: PLACEHOLDER_USER_MESSAGE
            The expected response is: PLACEHOLDER_EXPECTED_RESPONSE
            
            Compare the AI response PLACEHOLDER_AI_RESPONSE with the expected response PLACEHOLDER_EXPECTED_RESPONSE.
            The comparison result has to be scored by you.
            Use the following rating scale 1, 2, 3.
            1 is used for a wrong response given by the AI.
            A wrong response has no similarity to the expected response PLACEHOLDER_EXPECTED_RESPONSE.
            A wrong message has a different meaning compared to the expected response PLACEHOLDER_EXPECTED_RESPONSE.
            A wrong response does also not fit to the message of the user PLACEHOLDER_USER_MESSAGE.
            3 is used for a good response given by the AI.
            A good response is similar or has the same meaning compared to the expected response PLACEHOLDER_EXPECTED_RESPONSE.
            A good response is short, but contains all relevant information, which you find in PLACEHOLDER_EXPECTED_RESPONSE.
            2 is used for an acceptable response given by the AI.
            An acceptable response has a similar meaning compared to the expected response PLACEHOLDER_EXPECTED_RESPONSE.
            An acceptable response might be too long or contain additional, but irrelevant information for the user.
            
            Your answer must contain the userMessage and its score, so that it can be parsed into this Java record: Rating(String userMessage, int score).
            """;

}
