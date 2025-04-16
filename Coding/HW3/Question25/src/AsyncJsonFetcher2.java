import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

public class AsyncJsonFetcher2 {

    public static void main(String[] args) {
        int userId = 2;

        String userUrl = "https://jsonplaceholder.typicode.com/users/" + userId;
        String postsUrl = "https://jsonplaceholder.typicode.com/posts?userId=" + userId;
        String albumsUrl = "https://jsonplaceholder.typicode.com/albums?userId=" + userId;

        HttpClient client = HttpClient.newHttpClient();

        CompletableFuture<String> userFuture = fetch(client, userUrl);
        CompletableFuture<String> postsFuture = fetch(client, postsUrl);
        CompletableFuture<String> albumsFuture = fetch(client, albumsUrl);

        CompletableFuture.allOf(userFuture, postsFuture, albumsFuture)
                .thenRun(() -> {
                    try {
                        String user = userFuture.get();
                        String posts = postsFuture.get();
                        String albums = albumsFuture.get();

                        System.out.println("=== User (ID: " + userId + ") ===");
                        System.out.println(user);
                        System.out.println("\n=== Posts (User ID: " + userId + ") ===");
                        System.out.println(posts);
                        System.out.println("\n=== Albums (User ID: " + userId + ") ===");
                        System.out.println(albums);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).join();
    }

    private static CompletableFuture<String> fetch(HttpClient client, String url) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)  // onSuccess, return body
                .exceptionally(ex -> {  // onException, handle the error and return a default value
                    System.err.println("Error fetching from URL: " + url);
                    ex.printStackTrace();  // Log the exception stack trace
                    return "{}";  // Return a default empty JSON object as a fallback
                });
    }
}
