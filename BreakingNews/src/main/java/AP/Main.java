package AP;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        String apiKey = "e4ab6188b6754ee49b123585f93455c3";

        Infrastructure infrastructure = new Infrastructure(apiKey);

        while (true) {
            displayMenu(infrastructure);

            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter the number of the news article to read more, or 0 to exit: ");
            int choice = scanner.nextInt();

            if (choice == 0) {
                System.out.println("Exiting program.");
                break;
            }

            displayNewsDetails(infrastructure, choice);
        }
    }

    public static void displayMenu(Infrastructure infrastructure) {
        if (infrastructure.getNewsList() == null || infrastructure.getNewsList().isEmpty()) {
            System.out.println("No news available.");
            return;
        }

        System.out.println("\n📰 Latest News Articles:");
        for (int i = 0; i < infrastructure.getNewsList().size(); i++) {
            System.out.println((i + 1) + ". " + infrastructure.getNewsList().get(i).getTitle());
        }
    }

    public static void displayNewsDetails(Infrastructure infrastructure, int choice) {
        if (choice < 1 || choice > infrastructure.getNewsList().size()) {
            System.out.println("Invalid choice. Please try again.");
            return;
        }

        News selectedNews = infrastructure.getNewsList().get(choice - 1);

        System.out.println("\nTitle: " + selectedNews.getTitle());
        System.out.println("Description: " + selectedNews.getDescription());
        System.out.println("Source: " + selectedNews.getSourceName());
        System.out.println("Author: " + selectedNews.getAuthor());
        System.out.println("URL: " + selectedNews.getUrl());
        System.out.println("Published At: " + selectedNews.getPublishedAt());
    }
}

