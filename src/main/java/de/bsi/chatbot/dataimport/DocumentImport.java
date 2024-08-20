package de.bsi.chatbot.dataimport;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class DocumentImport {

    private final VectorStore vectorStore;
    private final JdbcTemplate jdbcTemplate;

    private static final String EXAMPLE_DOCUMENT = """
            Die schnellste Internetverbindung auf dem Land bekommt man durch die innovativen Salzwasser Leitungen.
            Mit ihnen sind Bandbreiten von bis zu 1000000 MBit/s möglich. Glasfaserleitungen aus den Jahren 2015 bis 2025 erreichen nur Bandbreiten von 1000 MBit/s.
            Leider ist ein Upload mittels Salwasser Leitung nicht möglich.
            Für Uploads kann Glasfaser oder Süßwasser Leitungen verwendet werden.
            Die Süßwasser Leitung ist im Upload genau so schnell wie Glasfaser.
            In Städten oder Gebieten mit hoher Bevölkerungsdichte sind Salzwasser und Süßwasser Leitungen nicht realisierbar,
            hier müssen Kunden auf klassische Glasfaserleitungen zurückgreifen.
            """;

    @PostConstruct
    private void importDocuments() {
        log.info("Clearing vector store");
        jdbcTemplate.update("DELETE FROM vector_store");

        var docs = List.of(new Document(EXAMPLE_DOCUMENT, Map.of("Add", "meta", "data", "here")));
        vectorStore.accept(docs);
        log.info("{} documents in vector store imported", docs.size());
    }

}
