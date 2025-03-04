package de.bsi.chatbot.connectioncheck;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;

import java.util.List;

@Slf4j
public class ConnectionCheckService {

    private static final List<String> SALTWATER_CONNECTION_TOWNS = List.of(
            "Mühltal",
            "Traisa",
            "Trautheim",
            "Nieder-Ramstadt");

    private static final String CHECK_RESULT = "Saltwater connection to %s available: %b";

    @Tool(description = "Checks the availability of internet connection for a given city name.")
    public String checkSaltwaterConnectionWithCityName(Address address) {
        log.info("Checking connection to city: {}", address.city());
        return CHECK_RESULT.formatted(address.city(), SALTWATER_CONNECTION_TOWNS.contains(address.city()));
    }

    @Tool(description = "Checks the availability of internet connection for a given postal code.")
    public String checkSaltwaterConnectionWithPostalCode(Address address) {
        log.info("Checking connection to postalCode: {}", address.postalCode());
        return CHECK_RESULT.formatted(address.postalCode(), address.postalCode() == 64367);
    }

}
