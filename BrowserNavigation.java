// Main browser navigation class: manages stacks, history, and current session
public class BrowserNavigation {
    // Stack for forward navigation
    BrowserLinkedList<String> forwardStack = new BrowserLinkedList<String>();
    // Stack for back navigation
    BrowserLinkedList<String> backStack = new BrowserLinkedList<String>();
    // Queue for browsing history
    BrowserArrayList<String> historyList = new BrowserArrayList<String>();
    // Current page being viewed
    String currentPage;

    // Visit a new website and update history/stacks
    public String visitWebsite(String url) {
        currentPage = url;
        historyList.enqueue(url);
        backStack.push(url);
        forwardStack.clear();
        // Launch the website in the default browser
        try {
            if (java.awt.Desktop.isDesktopSupported()) {
                java.awt.Desktop.getDesktop().browse(new java.net.URI("https://" + url.replaceFirst("^https?://", "")));
            }
        } catch (Exception e) {
            // Optionally log or ignore
        }
        return "Now at " + url;
    }

    // Go back in history (if possible)
    public String goBack() {
        if (backStack.isEmpty()) {
            return "No previous page available";
        }
        // If only one item left, can't go back further
        if (backStack.size() == 1) {
            return "No previous page available";
        }
        forwardStack.push(backStack.pop());
        String dest = backStack.pop();
        backStack.push(dest);
        currentPage = dest;
        return "Now at " + currentPage;
    }

    // Go forward in history (if possible)
    public String goForward() {
        if (forwardStack.isEmpty()) {
            return "No forward page available";
        }
        // Move current page to back stack
        backStack.push(currentPage);
        // Set currentPage to new top of forwardStack
        currentPage = forwardStack.pop();
        return "Now at " + currentPage;
    }

    public String showHistory() {
        java.util.Iterator<String> it = historyList.iterator();
        if (!it.hasNext()) {
            return "No browsing history available.";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Browsing History: ");
        while (it.hasNext()) {
            sb.append(it.next());
            if (it.hasNext())
                sb.append(", ");
        }
        return sb.toString();
    }

    public String clearHistory() {
        historyList.clear();
        return "Browsing history cleared.";
    }

    public String closeBrowser() {
        try (java.io.PrintWriter out = new java.io.PrintWriter("session_data.txt")) {
            // Save backStack (oldest to newest)
            out.println("BACKSTACK");
            java.util.Iterator<String> backIt = backStack.iterator();
            while (backIt.hasNext()) {
                out.println(backIt.next());
            }
            // Save forwardStack (oldest to newest)
            out.println("FORWARDSTACK");
            java.util.Iterator<String> forwardIt = forwardStack.iterator();
            while (forwardIt.hasNext()) {
                out.println(forwardIt.next());
            }
            // Save history
            out.println("HISTORY");
            for (String s : historyList) {
                out.println(s);
            }
        } catch (Exception e) {
            return "Error saving browser session.";
        }
        return "Browser session saved.";
    }

    public String restoreLastSession() {
        java.io.File file = new java.io.File("session_data.txt");
        if (!file.exists()) {
            return "No previous session found.";
        }
        try (java.util.Scanner sc = new java.util.Scanner(file)) {
            backStack.clear();
            forwardStack.clear();
            historyList.clear();
            String section = "";
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                if (line.equals("BACKSTACK") || line.equals("FORWARDSTACK") || line.equals("HISTORY")) {
                    section = line;
                } else if (!line.isEmpty()) {
                    switch (section) {
                        case "BACKSTACK":
                            backStack.push(line);
                            break;
                        case "FORWARDSTACK":
                            forwardStack.push(line);
                            break;
                        case "HISTORY":
                            historyList.enqueue(line);
                            break;
                    }
                }
            }
        } catch (Exception e) {
            return "Error restoring session.";
        }
        return "Previous session restored.";
    }
}