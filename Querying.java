package RAGCON;

import java.util.List;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.AllMiniLmL6V2EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.neo4j.Neo4jEmbeddingStore;

public class Querying {

    @SuppressWarnings("deprecation")
	static void queryDatabase(String query, String dbUrl, String dbUser, String dbPass) {
        EmbeddingStore<TextSegment> embeddingStore = null;
        Driver neo4jDriver = null;
        StringBuilder resultBuilder = new StringBuilder();

        try {
            neo4jDriver = GraphDatabase.driver(dbUrl, AuthTokens.basic(dbUser, dbPass));
            embeddingStore = Neo4jEmbeddingStore.builder()
                    .driver(neo4jDriver)
                    .dimension(384)
                    .build();

            AllMiniLmL6V2EmbeddingModel embeddingModel = new AllMiniLmL6V2EmbeddingModel();
            dev.langchain4j.data.embedding.Embedding queryEmbedding = embeddingModel.embed(TextSegment.from(query)).content();

            @SuppressWarnings("deprecation")
			List<EmbeddingMatch<TextSegment>> matches = embeddingStore.findRelevant(queryEmbedding, 7);

            for (EmbeddingMatch<TextSegment> match : matches) {
                TextSegment item = match.embedded();
                resultBuilder.append("").append(item.text()).append("\n");
            }

        } catch (Exception e) {
            System.out.println("Failed to query database: " + e.getMessage());
        } finally {
            if (neo4jDriver != null) {
                neo4jDriver.close(); // Properly close the Neo4j driver
            }
        }

        String AIResp1 = AI.getReponse(query + ": answer the query");
        String AIResp = AI.getReponse(AIResp1 + ": factor in the following output into the answer: " + resultBuilder.toString());
        System.out.print(AIResp);
    }

}
