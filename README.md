# Browser Nav

Emily Butz

## Implementation Choices:
- Used a circular array for browser history (`BrowserArrayList`) for enqueue/dequeue.
- Used a doubly linked list for stack (`BrowserLinkedList`).
- Implemented browser navigation with back/forward stacks and a history queue.
- Used generics for reusable data structures.
- Used Java's Desktop API to launch URLs in the default browser.

## Complexity Analysis
- BrowserArrayList (circular array)
  - add(x) at end (enqueue): O(1) amortized
  - add(idx, x): O(n)
  - remove(0) (dequeue): O(n) due to shifting in current implementation
  - remove(idx): O(n)
- BrowserLinkedList (doubly linked list)
  - add(x) at end: O(1)
  - push(x) (stack): O(1)
  - pop() (stack): O(1)
  - remove(idx): O(n)
- BrowserNavigation
  - visitWebsite: O(1) amortized (enqueue + push + clear)
  - goBack/goForward: O(1)
  - showHistory: O(n)
  - closeBrowser/restoreLastSession: O(n)

## Test Cases (Also in Main)
```java
// 1. Visit multiple websites and verify history order
BrowserNavigation browser = new BrowserNavigation();
browser.visitWebsite("www.google.com");
browser.visitWebsite("www.youtube.com");
browser.visitWebsite("www.reddit.com");
System.out.println(browser.showHistory()); // Browsing History: www.google.com, www.youtube.com, www.reddit.com

// 2. Save and restore session to/from file
System.out.println(browser.closeBrowser()); // Browser session saved.
System.out.println(browser.restoreLastSession()); // Previous session restored.
System.out.println(browser.showHistory()); // Browsing History: ...

// 3. Use goBack and goForward to navigate and check current page
System.out.println(browser.goBack()); // Now at www.youtube.com
System.out.println(browser.goBack()); // Now at www.google.com
System.out.println(browser.goForward()); // Now at www.youtube.com
System.out.println(browser.goForward()); // Now at www.reddit.com

// 4. Clear history and verify it is empty
System.out.println(browser.clearHistory()); // Browsing history cleared.
System.out.println(browser.showHistory()); // No browsing history available.

// 5. Save and restore session to/from file
System.out.println(browser.closeBrowser()); // Browser session saved.
System.out.println(browser.restoreLastSession()); // Previous session restored.
System.out.println(browser.showHistory()); // Browsing History: ...

// Edge Cases
// ----------

// 1. Attempt to go back/forward with only one page in history
BrowserNavigation myBrowser = new BrowserNavigation();
myBrowser.visitWebsite("www.google.com");
System.out.println(myBrowser.goBack()); // Cannot go back: no more history.
System.out.println(myBrowser.goForward()); // Cannot go forward: no more history.

// 2. Visit, go back, visit new site (should clear forward stack)
System.out.println(myBrowser.visitWebsite("www.youtube.com")); // Now at www.youtube.com
System.out.println(myBrowser.visitWebsite("www.google.com")); // Now at www.google.com
System.out.println(myBrowser.visitWebsite("www.reddit.com")); // Now at www.reddit.com
System.out.println(myBrowser.goBack()); // Now at www.google.com
System.out.println(myBrowser.visitWebsite("www.github.com")); // Forward stack cleared
System.out.println(myBrowser.goForward()); // Cannot go forward: no more history.
System.out.println(myBrowser.showHistory()); // Browsing History: www.google.com, www.youtube.com, www.reddit.com, www.github.com

// 3. Clear history after navigation
System.out.println(myBrowser.clearHistory()); // Browsing history cleared.
System.out.println(myBrowser.showHistory()); // No browsing history available.

// 4. Save/restore session with empty and non-empty stacks
System.out.println(myBrowser.closeBrowser()); // Browser session saved.
System.out.println(myBrowser.restoreLastSession()); // Previous session restored.
System.out.println(myBrowser.showHistory()); // Browsing History: ...

// 5. Restore session when file is missing
BrowserNavigation newBrowser = new BrowserNavigation();
System.out.println(newBrowser.restoreLastSession()); // No previous session found.

// 6. Visit invalid URLs (should not crash, browser launch may fail)
browser.visitWebsite("");
browser.visitWebsite("not_a_url");
System.out.println(browser.showHistory()); // Browsing History: ...

// 7. Checks for proper resizing of circular array
browser.visitWebsite("www.google.com");
browser.visitWebsite("www.youtube.com");
browser.visitWebsite("www.reddit.com");
browser.visitWebsite("www.github.com");
browser.visitWebsite("www.stackoverflow.com");
browser.visitWebsite("www.linkedin.com");
browser.visitWebsite("www.twitter.com");
browser.visitWebsite("www.facebook.com");
browser.visitWebsite("www.instagram.com");
browser.visitWebsite("www.netflix.com");
browser.visitWebsite("www.amazon.com");
browser.visitWebsite("www.microsoft.com");
System.out.println(browser.showHistory()); // Browsing History: ...
```
