import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GitHubTrendingScraper {
    private static final String URL = "https://github.com/trending";

    public static void main(String[] args) {
        try {
            // Fetch the HTML content
            String html = fetchHTML(URL);

            // Basic parsing of HTML content
            int repoIndex = 0;
            while ((repoIndex = html.indexOf("class=\"h3 lh-condensed\"", repoIndex)) != -1) {
                // Extract repository name
                int titleStart = html.indexOf("href=\"", repoIndex) + 6;
                int titleEnd = html.indexOf("\"", titleStart);
                String title = html.substring(titleStart, titleEnd).replace("/","");

                // Extract repository description
                int descStart = html.indexOf("class=\"col-9 color-fg-muted my-1 pr-4\"", titleEnd);
                int descEnd = html.indexOf("</p>", descStart);
                String description = descStart != -1 && descStart < descEnd ? 
                    html.substring(descStart + 42, descEnd).trim() : "No description";

                // Extract repository stars
                int starStart = html.indexOf("aria-label=\"star\" class=\"Link--muted d-inline-block mr-3\"", titleEnd);
                int starCountStart = html.indexOf(">", starStart) + 1;
                int starCountEnd = html.indexOf("<", starCountStart);
                String stars = html.substring(starCountStart, starCountEnd).trim();

                // Print the extracted information
                System.out.println("Repository: " + title);
                System.out.println("Description: " + description);
                System.out.println("Stars: " + stars);
                System.out.println("-----------------------------");

                // Move to the next repository
                repoIndex = titleEnd;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String fetchHTML(String urlString) throws Exception {
        StringBuilder result = new StringBuilder();
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
        }
        return result.toString();
    }
}
