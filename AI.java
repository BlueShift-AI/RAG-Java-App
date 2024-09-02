package RAGCON;

import java.time.Duration;
import dev.langchain4j.chain.ConversationalChain;
import dev.langchain4j.chain.ConversationalChain.ConversationalChainBuilder;
import dev.langchain4j.model.ollama.OllamaChatModel;

public class AI {

    public static ConversationalChain con() {
        ConversationalChainBuilder chainBuilder = ConversationalChain.builder()
                .chatLanguageModel(OllamaChatModel.builder()
                        .baseUrl("http://127.0.0.1:11434")
                        .modelName("tinyllama")
                        .temperature(0.8)
                        .timeout(Duration.ofMinutes(5))
                        .build());
        // Build the Conversation
        ConversationalChain smartchain = chainBuilder.build();
        return smartchain;
    }

    public static String getReponse(String prompt) {
        // First Prompt comes from user
        String input = con().execute(prompt);
        String response = input;
        return response;
    }
}
