import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

public class AsyncJsonFetcher {

    public static void main(String[] args) {
        int userId = 1;

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
                .thenApply(HttpResponse::body);
    }
}
