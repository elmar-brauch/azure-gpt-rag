package de.bsi.chatbot.chat.llm;

import lombok.Data;

@Data
public class GoldenAnswer {

    private String userMessage;
    private String expectedResponse;

}
