package de.bsi.chatbot.chat.llm;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest(properties = {"logging.level.org.springframework.ai=INFO"})
@ActiveProfiles("local")
@Disabled("This test is about quality rating and its execution produces AI usage costs, so it is disabled in build.")
@Import(AiResponseRater.class)
@Slf4j
class ChatServiceTest {

    @Autowired private ChatService chatService;
    @Autowired private AiResponseRater rater;

    @Value("classpath:goldenAnswers.json")
    private Resource goldenAnswersJson;
    private final ObjectMapper jsonMapper = new ObjectMapper();

    @Test
    void oneMessageTests() throws IOException {
        for (var goldenAnswer : readGoldenAnswers())
            oneMessageTest(goldenAnswer);
    }

    private void oneMessageTest(GoldenAnswer goldenAnswer) {
        var aiResponse = chatService.chat(goldenAnswer.getUserMessage());

        var rating = rater.rateAiResponse(goldenAnswer.getUserMessage(), goldenAnswer.getExpectedResponse(), aiResponse);
        log.info("\n\nRATING {}\nUSER QUESTION: {}\nAI RESPONSE  : {}\n",
                rating.score(),
                goldenAnswer.getUserMessage(),
                aiResponse);
        if (rating.score() <= 1 || rating.score() > 3)
            fail("Test failed due to poor or invalid rating: " + rating);
    }

    private List<GoldenAnswer> readGoldenAnswers() throws IOException {
        return jsonMapper.readValue(goldenAnswersJson.getInputStream(),
                jsonMapper.getTypeFactory().constructCollectionType(List.class, GoldenAnswer.class));
    }

    // Demo @Test
    void notWorkingTest() {
        var aiResponse = chatService.chat("Hello, which Internet connection is available?");
        assertEquals("TODO Copy first response", aiResponse);
    }

}