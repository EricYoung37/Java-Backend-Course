package com.company.backend.mongoblog;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoException;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import io.github.cdimascio.dotenv.Dotenv;

// If no run button shows up or Dotenv.load() fails to read the .env file,
// check out README.md for instructions on setting up the environment.

public class MongoPing {
    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load();

        String user = dotenv.get("MONGO_USER");
        String password = dotenv.get("MONGO_PWD");
        String cluster = dotenv.get("MONGO_CLUSTER");

        String connectionString = String.format(
                "mongodb+srv://%s:%s@%s.nzqgtvd.mongodb.net/?retryWrites=true&w=majority&appName=%s",
                user, password, cluster, cluster
        );

        ServerApi serverApi = ServerApi.builder()
                .version(ServerApiVersion.V1)
                .build();

        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(connectionString))
                .serverApi(serverApi)
                .build();

        // Create a new client and connect to the server
        try (MongoClient mongoClient = MongoClients.create(settings)) {
            try {
                // Send a ping to confirm a successful connection
                MongoDatabase database = mongoClient.getDatabase("admin");
                database.runCommand(new Document("ping", 1));
                System.out.println("Pinged your deployment. You successfully connected to MongoDB!");
            } catch (MongoException e) {
                e.printStackTrace();
            }
        }
    }
}
