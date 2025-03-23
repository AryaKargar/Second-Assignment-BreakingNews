package AP;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Scanner;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import java.time.LocalDate;

public class Infrastructure {

    private final String URL;
    private final String APIKEY;
    private final String JSONRESULT;
    private ArrayList<News> newsList;


    public Infrastructure(String APIKEY) {
        this.APIKEY = APIKEY;
        this.URL = "https://newsapi.org/v2/everything?q=tesla&from=" + LocalDate.now().minusDays(1) + "&sortBy=publishedAt&apiKey=";
        this.JSONRESULT = getInformation();
        parseInformation();
    }

    public ArrayList<News> getNewsList() {
        return newsList;
    }

    private String getInformation() {
        try {
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(URL + APIKEY))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                return response.body();
            } else {
                throw new IOException("HTTP error code: " + response.statusCode());
            }
        } catch (Exception e) {
            System.out.println("!!Exception : " + e.getMessage());
        }
        return null;
    }

    private void parseInformation() {
        if (JSONRESULT == null) {
            System.out.println("Error: No JSON data to parse.");
            return;
        }

        newsList = new ArrayList<>();

        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(JSONRESULT, JsonObject.class);

        if (!jsonObject.has("articles")) {
            System.out.println("Error: No articles found.");
            return;
        }

        JsonArray articlesArray = jsonObject.getAsJsonArray("articles");

        int count = Math.min(20, articlesArray.size());

        for (int i = 0; i < count; i++) {
            JsonObject article = articlesArray.get(i).getAsJsonObject();

            String title = article.has("title") ? article.get("title").getAsString() : "No Title";
            String description = article.has("description") ? article.get("description").getAsString() : "No Description";
            String sourceName = article.has("source") ? article.getAsJsonObject("source").get("name").getAsString() : "Unknown";
            String author = article.has("author") ? article.get("author").getAsString() : "Unknown";
            String url = article.has("url") ? article.get("url").getAsString() : "No URL";
            String publishedAt = article.has("publishedAt") ? article.get("publishedAt").getAsString() : "No Date";

            News news = new News(title, description, sourceName, author, url, publishedAt);
            newsList.add(news);
        }

        System.out.println("Successfully parsed " + newsList.size() + " news articles.");
    }


    public void displayNewsList() {
        if (newsList == null || newsList.isEmpty()) {
            System.out.println("No news articles available.");
            return;
        }

        System.out.println("\n📰 Latest News:");
        for (int i = 0; i < newsList.size(); i++) {
            System.out.println((i + 1) + ". " + newsList.get(i).getTitle());
        }

        Scanner scanner = new Scanner(System.in);
        System.out.print("\nEnter the number of the news article to see more details, or 0 to exit: ");
        int choice = scanner.nextInt();

        if (choice == 0) {
            System.out.println("Exiting.");
            return;
        }

        if (choice < 1 || choice > newsList.size()) {
            System.out.println("Invalid choice. Please try again.");
        } else {
            News selectedNews = newsList.get(choice - 1);
            System.out.println("\nTitle: " + selectedNews.getTitle());
            System.out.println("Description: " + selectedNews.getDescription());
            System.out.println("Source: " + selectedNews.getSourceName());
            System.out.println("Author: " + selectedNews.getAuthor());
            System.out.println("URL: " + selectedNews.getUrl());
            System.out.println("Published At: " + selectedNews.getPublishedAt());
        }
    }

}
