package RAGCON;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Scanner;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.AllMiniLmL6V2EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.neo4j.Neo4jEmbeddingStore;

public class Embedding {

    public static void embedString(String text, String dbUrl, String dbUser, String dbPass) throws IOException {
        if (text != null && !text.trim().isEmpty()) {
            EmbeddingStore<TextSegment> embeddingStore = connectNeo4j(dbUrl, dbUser, dbPass);
            AllMiniLmL6V2EmbeddingModel embeddingModel = new AllMiniLmL6V2EmbeddingModel();

            String[] sentences = text.split("(?<=[.?!])\\s+");
            for (String sentence : sentences) {
                String trimmedSentence = sentence.trim();
                if (!trimmedSentence.isEmpty()) {
                    try {
                        TextSegment segment = TextSegment.from(trimmedSentence);
                        dev.langchain4j.data.embedding.Embedding embedding = embeddingModel.embed(segment).content();
                        embeddingStore.add(embedding, segment);
                        System.out.println("Embedded sentence: " + trimmedSentence);
                    } catch (IllegalArgumentException e) {
                        System.out.println("Error: " + e.getMessage() + " for sentence: " + trimmedSentence);
                    }
                }
            }
        } else {
            System.out.println("Error: Provided text is null or empty.");
        }
    }

    public static void embedUrl(String urlString, String dbUrl, String dbUser, String dbPass) {
        try {
            System.out.println("Fetching content from URL: " + urlString);
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                System.out.println("Error: Failed to fetch content from URL, response code: " + responseCode);
                return;
            }

            Scanner scanner = new Scanner(conn.getInputStream());
            scanner.useDelimiter("\\A");
            String content = scanner.hasNext() ? scanner.next() : "";
            scanner.close();

            // Using JSoup to parse the HTML and extract <p> elements
            Document doc = Jsoup.parse(content);
            Elements paragraphs = doc.select("p");

            StringBuilder textBuilder = new StringBuilder();
            for (Element paragraph : paragraphs) {
                textBuilder.append(paragraph.text()).append("\n\n");
            }

            String extractedText = textBuilder.toString().trim();
            if (!extractedText.isEmpty()) {
                embedString(extractedText, dbUrl, dbUser, dbPass);
            } else {
                System.out.println("No valid text found in <p> elements.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error: Failed to fetch content from URL");
        }
    }

    public static void embedFiles(String path, String dbUrl, String dbUser, String dbPass) {
        File fileOrFolder = new File(path);
        if (!fileOrFolder.exists()) {
            System.out.println("Error: File or folder not found");
            return;
        }

        EmbeddingStore<TextSegment> embeddingStore = connectNeo4j(dbUrl, dbUser, dbPass);

        if (fileOrFolder.isDirectory()) {
            File[] files = fileOrFolder.listFiles();
            if (files != null) {
                Arrays.stream(files)
                        .filter(file -> file.getName().endsWith(".pdf"))
                        .forEach(file -> embedFile(file, embeddingStore));
            } else {
                System.out.println("Error: No files found in directory");
            }
        } else {
            System.out.println("Error: Not a directory");
        }
    }

    private static EmbeddingStore<TextSegment> connectNeo4j(String dbUrl, String dbUser, String dbPass) {
        try {
            return Neo4jEmbeddingStore.builder()
                    .withBasicAuth(dbUrl, dbUser, dbPass)
                    .dimension(384)
                    .build();
        } catch (Exception e) {
            System.out.println("Error connecting to Neo4j: " + e.getMessage());
            return null;
        }
    }

    private static void embedFile(File file, EmbeddingStore<TextSegment> embeddingStore) {
        if (file.getName().endsWith(".pdf")) {
            System.out.println("Processing file: " + file.getName());
            
            try (PDDocument document = PDDocument.load(file)) {
                PDFTextStripper stripper = new PDFTextStripper();
                String text = stripper.getText(document);

                // Log the extracted text
                System.out.println("Extracted text: ");
                System.out.println(text);

                if (text != null && !text.trim().isEmpty()) {
                    AllMiniLmL6V2EmbeddingModel embeddingModel = new AllMiniLmL6V2EmbeddingModel();
                    String[] paragraphs = text.split("\\n\\n+");

                    for (String paragraph : paragraphs) {
                        String[] sentences = paragraph.split("(?<=[.?!])\\s+");

                        for (String sentence : sentences) {
                            String trimmedSentence = sentence.trim();
                            
                            if (!trimmedSentence.isEmpty()) {
                                try {
                                    TextSegment segment = TextSegment.from(trimmedSentence);
                                    dev.langchain4j.data.embedding.Embedding embedding = embeddingModel.embed(segment).content();
                                    embeddingStore.add(embedding, segment);
                                } catch (IllegalArgumentException e) {
                                    System.out.println("Error: " + e.getMessage() + " for sentence: " + trimmedSentence);
                                }
                            } else {
                                System.out.println("Skipping empty or blank sentence.");
                            }
                        }
                    }
                } else {
                    System.out.println("No valid text found in file: " + file.getName());
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Failed to process file: " + file.getName());
            }
        } else {
            System.out.println("Error: Not a PDF file: " + file.getName());
        }
    }

}
