# Ragcon v0.26.1 - JAVA RAG MANAGER

## Overview
`ragcon` is a command-line tool designed to integrate with a Neo4j graph database and the Ollama service to handle embeddings and queries. This tool supports embedding PDF files, content from URLs, or plain text strings into a database, and querying the stored data. The embeddings can be managed through a local or remote instance of the Ollama service.

## Requirements
    - Java 8 or higher
    - Neo4j Database (default connection: `bolt://localhost:7687`)
    - Ollama service for embeddings (default service URL: `http://127.0.0.1:11434`)

## Usage
    To use `ragcon`, run the JAR file with the appropriate options:

Options

    --embed <file/folder>
    Embed the contents of the specified PDF file or all PDFs in the specified folder into the database.
      Examples:
        java -jar ragcon-0.26.1.jar --embed /path/to/file.pdf
        java -jar ragcon-0.26.1.jar --embed /path/to/folder/

    --embed-url <url>
    Embed content from the specified URL into the database.
      Example:
        java -jar ragcon-0.26.1.jar --embed-url https://example.com/document.pdf

    --embed-string <string>
    Embed the specified string directly into the database.
      Example:
        java -jar ragcon-0.26.1.jar --embed-string "This is the content to embed."

    --query <query>
    Query the database with the specified text.
      Example:
        java -jar ragcon-0.26.1.jar --query "Find related content"

    --db-url <url>
    Set the Neo4j database URL. The default is bolt://localhost:7687.
      Example:
        java -jar ragcon-0.26.1.jar --db-url bolt://remotehost:7687

    --db-user <username>
    Set the Neo4j database username. The default is neo4j.
      Example:
        java -jar ragcon-0.26.1.jar --db-user myUsername

    --db-pass <password>
    Set the Neo4j database password. The default is password.
      Example:
        java -jar ragcon-0.26.1.jar --db-pass myPassword

    --ollama-model <model>
    Set the Ollama model to use for embeddings. The default is tinyllama.
      Example:
        java -jar ragcon-0.26.1.jar --ollama-model large-llama

    --ollama-url <url>
    Set the Ollama service URL. The default is http://127.0.0.1:11434.
      Example:
        java -jar ragcon-0.26.1.jar --ollama-url http://remotehost:11434

Example Usage Scenarios:

    - Embed a single PDF file:
          java -jar ragcon-0.26.1.jar --embed /path/to/document.pdf
      
    - Query the database:
          java -jar ragcon-0.26.1.jar --query "Find all documents related to AI"

Contributing
Contributions are welcome! Please submit a pull request or file an issue for any improvements or bug reports.
