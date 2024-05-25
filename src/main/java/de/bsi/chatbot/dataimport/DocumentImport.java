package de.bsi.chatbot.dataimport;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class DocumentImport {

    private final VectorStore vectorStore;
    private final JdbcTemplate jdbcTemplate;
    private final TokenTextSplitter textSplitter = new TokenTextSplitter();

    private static final String EXAMPLE_DOCUMENT = """
            Die schnellste Internetverbindung auf dem Land bekommt man durch die innovativen Salzwasser Leitungen.
            Mit ihnen sind Bandbreiten von bis zu 1000000 MBit/s möglich. Glasfaserleitungen aus den Jahren 2015 bis 2025 erreichen nur Bandbreiten von 1000 MBit/s.
            Leider ist ein Upload mittels Salwasser Leitung nicht möglich.
            Für Uploads kann Glasfaser oder Süßwasser Leitungen verwendet werden.
            Die Süßwasser Leitung ist im Upload genau so schnell wie Glasfaser.
            """;

    public void importDocuments() {
        jdbcTemplate.update("DELETE FROM vector_store");

        var vectorizedDocumentChunks = textSplitter.split(EXAMPLE_DOCUMENT, 800)
                .stream()
                .map(chunk -> new Document(chunk, Map.of("Add", "meta", "data", "here")))
                .toList();
        vectorStore.accept(vectorizedDocumentChunks);
    }

}
