package RAGCON;

import java.io.IOException;

public class RAGCommandLine {

    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            printUsage();
            return;
        }

        // Default values for Neo4j connection
        String dbUrl = "bolt://127.0.0.1:7687";
        String dbUser = "neo4j";
        String dbPass = "BRavo-201";

        // Default values for Ollama connection
        String ollamaModel = "tinyllama";
        String ollamaUrl = "http://127.0.0.1:11434";

        // Parse command-line arguments
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "--embed":
                    if (i + 1 < args.length && !args[i + 1].startsWith("--")) {
                        Embedding.embedFiles(args[i + 1], dbUrl, dbUser, dbPass);
                    } else {
                        System.out.println("Error: --embed requires a file or folder argument");
                        printUsage();
                    }
                    return;
                case "--embed-url":
                    if (i + 1 < args.length && !args[i + 1].startsWith("--")) {
                    	Embedding.embedUrl(args[i + 1], dbUrl, dbUser, dbPass);
                    } else {
                        System.out.println("Error: --embed-url requires a URL argument");
                        printUsage();
                    }
                    return;
                case "--embed-string":
                    if (i + 1 < args.length && !args[i + 1].startsWith("--")) {
							Embedding.embedString(args[i + 1], dbUrl, dbUser, dbPass);

                    } else {
                        System.out.println("Error: --embed-string requires a string argument");
                        printUsage();
                    }
                    return;
                case "--query":
                    if (i + 1 < args.length && !args[i + 1].startsWith("--")) {
                        Querying.queryDatabase(args[i + 1], dbUrl, dbUser, dbPass);
                    } else {
                        System.out.println("Error: --query requires a query argument");
                        printUsage();
                    }
                    return;

                case "--db-url":
                    if (i + 1 < args.length && !args[i + 1].startsWith("--")) {
                        dbUrl = args[i + 1];
                    } else {
                        System.out.println("Error: --db-url requires a URL argument");
                        printUsage();
                    }
                    i++; // Skip next argument as it is consumed
                    break;
                case "--db-user":
                    if (i + 1 < args.length && !args[i + 1].startsWith("--")) {
                        dbUser = args[i + 1];
                    } else {
                        System.out.println("Error: --db-user requires a username argument");
                        printUsage();
                    }
                    i++; // Skip next argument as it is consumed
                    break;
                case "--db-pass":
                    if (i + 1 < args.length && !args[i + 1].startsWith("--")) {
                        dbPass = args[i + 1];
                    } else {
                        System.out.println("Error: --db-pass requires a password argument");
                        printUsage();
                    }
                    i++; // Skip next argument as it is consumed
                    break;
                case "--ollama-model":
                    if (i + 1 < args.length && !args[i + 1].startsWith("--")) {
                        ollamaModel = args[i + 1];
                    } else {
                        System.out.println("Error: --ollama-model requires a model argument");
                        printUsage();
                    }
                    i++; // Skip next argument as it is consumed
                    break;
                case "--ollama-url":
                    if (i + 1 < args.length && !args[i + 1].startsWith("--")) {
                        ollamaUrl = args[i + 1];
                    } else {
                        System.out.println("Error: --ollama-url requires a URL argument");
                        printUsage();
                    }
                    i++; // Skip next argument as it is consumed
                    break;
                default:
                    System.out.println("Unknown option: " + args[i]);
                    printUsage();
                    return;
            }
        }
    }

    private static void printUsage() {
        System.out.println("Usage: java -jar ragcon-0.26.1.jar [options]");
        System.out.println("Options:");
        System.out.println("  --embed <file/folder>    Embed the contents of the specified PDF file or all PDFs in the specified folder.");
        System.out.println("  --embed-url <url>        Embed the content from the specified URL.");
        System.out.println("  --embed-string <string>  Embed the specified string.");
        System.out.println("  --query <query>          Query the database with the specified text.");
        System.out.println("  --db-url <url>           Set the Neo4j database URL. Default is bolt://localhost:7687.");
        System.out.println("  --db-user <username>     Set the Neo4j database username. Default is neo4j.");
        System.out.println("  --db-pass <password>     Set the Neo4j database password. Default is password.");
        System.out.println("  --ollama-model <model>   Set the Ollama model to use for embeddings. Default is tinyllama.");
        System.out.println("  --ollama-url <url>       Set the Ollama service URL. Default is http://127.0.0.1:11434.");
    }

}
