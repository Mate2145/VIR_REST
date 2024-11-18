package com.vir.postmanbe.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

@Component
public class ConsoleApiTester implements CommandLineRunner {

    private final RestTemplate restTemplate = new RestTemplate();

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_CYAN = "\u001B[36m";

    @Override
    public void run(String... args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println(ANSI_CYAN + "         _________ _______ ");
            System.out.println("|\\     /|\\__   __/(  ____ )");
            System.out.println("| )   ( |   ) (   | (    )|");
            System.out.println("| |   | |   | |   | (____)|");
            System.out.println("( (   ) )   | |   |     __)");
            System.out.println(" \\ \\_/ /    | |   | (\\ (   ");
            System.out.println("  \\   /  ___) (___| ) \\ \\__");
            System.out.println("   \\_/   \\_______/|/   \\__/");
            System.out.println("Üdvözöllek a PostWoman Konzolos API tesztelőben!" + ANSI_RESET);

            String url = "";
            while (url.isEmpty()) {
                System.out.print(ANSI_GREEN + "Add meg az API URL-t: " + ANSI_RESET);
                url = scanner.nextLine().trim();
                if (url.isEmpty()) {
                    System.out.println(ANSI_RED + "URL nem lehet üres, próbáld újra!" + ANSI_RESET);
                }
            }

            HttpMethod method = null;
            while (method == null) {
                System.out.print(ANSI_GREEN + "Válaszd ki a HTTP metódust (1. GET, 2. POST, 3. PUT, 4. DELETE, 5. PATCH): " + ANSI_RESET);
                String methodChoice = scanner.nextLine().trim();
                method = switch (methodChoice) {
                    case "1" -> HttpMethod.GET;
                    case "2" -> HttpMethod.POST;
                    case "3" -> HttpMethod.PUT;
                    case "4" -> HttpMethod.DELETE;
                    case "5" -> HttpMethod.PATCH;
                    default -> {
                        System.out.println(ANSI_RED + "Érvénytelen választás, próbáld újra!" + ANSI_RESET);
                        yield null;
                    }
                };
            }

            Map<String, String> headers = new HashMap<>();
            System.out.print(ANSI_GREEN + "Alapértelmezett fejlécek hozzáadása? (i/n): " + ANSI_RESET);
            String useBasicHeaders = scanner.nextLine().trim();

            if (useBasicHeaders.equalsIgnoreCase("i")) {
                headers.put("Content-Type", "application/json");
                headers.put("Accept", "application/json");
                System.out.println(ANSI_BLUE + "Alapértelmezett fejlécek hozzáadva: Content-Type: application/json, Accept: application/json" + ANSI_RESET);
            }

            System.out.print(ANSI_GREEN + "Szeretnél egyedi fejléceket hozzáadni? (i/n): " + ANSI_RESET);
            String addCustomHeaders = scanner.nextLine().trim();

            if (addCustomHeaders.equalsIgnoreCase("i")) {
                System.out.println(ANSI_BLUE + "Add meg a fejléceket (kulcs:érték formátumban, 'done' a befejezéshez): " + ANSI_RESET);
                UserInput(scanner, headers);
            }

            Map<String, String> params = new HashMap<>();
            System.out.println(ANSI_BLUE + "Add meg a paramétereket (kulcs:érték formátumban, 'done' a befejezéshez): " + ANSI_RESET);
            UserInput(scanner, params);

            String body = null;
            if (method == HttpMethod.POST || method == HttpMethod.PUT || method == HttpMethod.PATCH) {
                while (true) {
                    System.out.print(ANSI_BLUE + "Add meg a kérés törzsét (body) JSON formátumban: " + ANSI_RESET);
                    body = scanner.nextLine().trim();
                    if (isValidJson(body)) {
                        break;
                    } else {
                        System.out.println(ANSI_RED + "Hibás JSON formátum, kérlek próbáld újra!" + ANSI_RESET);
                    }
                }
            }

            sendRequest(url, headers, params, method, body);

            System.out.print(ANSI_GREEN + "Szeretnél újabb tesztet futtatni? (i/n): " + ANSI_RESET);
            String choice = scanner.nextLine().trim();
            if (choice.equalsIgnoreCase("n")) {
                System.out.println(ANSI_CYAN + "Kilépés a PostWoman API tesztelőből. Viszlát!" + ANSI_RESET);
                break;
            }
        }
    }


    private boolean isValidJson(String body) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.readTree(body);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void sendRequest(String url, Map<String, String> headers, Map<String, String> params, HttpMethod method, String body) {
        try {
            HttpHeaders httpHeaders = new HttpHeaders();
            headers.forEach(httpHeaders::set);

            HttpEntity<String> entity = body != null ? new HttpEntity<>(body, httpHeaders) : new HttpEntity<>(httpHeaders);

            ResponseEntity<String> response = restTemplate.exchange(url, method, entity, String.class, params);
            System.out.println(ANSI_GREEN + "Válasz:\n" + response.getBody() + ANSI_RESET);
        } catch (Exception e) {
            System.err.println(ANSI_RED + "Hiba történt: " + e.getMessage() + ANSI_RESET);
        }
    }

    private void UserInput(Scanner scanner, Map<String, String> map) {
        while (true) {
            String input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("done")) break;
            if (!input.contains(":") || input.startsWith(":")) {
                System.out.println(ANSI_RED + "Hibás formátum, próbáld újra (kulcs:érték)!" + ANSI_RESET);
            } else {
                String[] parts = input.split(":", 2);
                map.put(parts[0].trim(), parts[1].trim());
            }
        }
    }
}
