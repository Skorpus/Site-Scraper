import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Scraper {

	private URL scrapeURL;
	private BufferedReader reader;
	private Set<String> allLinks;
	// Make this instance variable so won't read until thread is complete
	private Thread contentThread;

	Scraper(String url) {
		allLinks = new LinkedHashSet<String>();
		try {
			// Create the URL that we are scraping
			this.scrapeURL = new URL(url);

		} catch (MalformedURLException e) {
			// Driver didn't check properly, you idiot
			System.out.println("Illegal URL :" + e.getMessage());
		}
	}

	public void getContent() {
		// Grab the content in a new thread
		contentThread = new Thread(new Runnable() {
			@Override
			public void run() {
				// Collect the data from the URL

				try {
					// Have a reader that takes in the input of the URL
					reader = new BufferedReader(new InputStreamReader(scrapeURL.openStream()));

					// Now we print it to the console to check it out
					String line;
					// Need .* as can have any number of characters in front
					// We know links will be of the form <a href="...">
<<<<<<< HEAD
					String linkPattern = "(.*)([hH][rR][eE][fF])=\"(.+?)\"";
=======
					// Use the *? to be non-greedy(reluctant) hence only goes to
					// first match
					String linkPattern = "(.*)([hH][rR][eE][fF])=\"(.*?)\"";
>>>>>>> master
					// Pattern is a nice class that compiles the REGEX
					Pattern pattern = Pattern.compile(linkPattern);

					while ((line = reader.readLine()) != null) {
						// System.out.println(line);
						// Rather than just printing the file lets use some
						// regex to scrape
						// Matcher is a REGEX check class that we use with the
						// pattern class, create the matcher using the pattern
						Matcher matcher = pattern.matcher(line);
						if (matcher.lookingAt()) {
							// System.out.println(line);
							// Get the substring so can just obtain the url
							String output = line.substring(matcher.start(), matcher.end());
							// Still not there yet, need to split the string and
							// get the url
<<<<<<< HEAD
							output = output.split("href=\"")[1];
							// And remove trailing \"
							output = output.substring(0, output.length() - 1);
=======
							// System.out.println(output);
							output = output.split("href=\"")[1];
							// Just need to remove trailing \"
							output = output.substring(0, output.length() - 1);
							// System.out.println(output);
>>>>>>> master
							// Next we want to put this into a set
							allLinks.add(output);
							// System.out.println("Added to set");
						}

					}

					// Tidy up after ourselves
					reader.close();
					// System.out.println("Here....");

				} catch (IOException e) {
					System.out.println("Error reading the page: " + e.getMessage());
				}

			}
		});
		contentThread.start();
	}

	/**
	 * This will wait for the thread to complete before returning the value
	 * 
	 * @return set of all links
	 */
	public Set<String> getLinks() {
		System.out.print("Fetching...");
		// Wait for thread to complete
		try {
			contentThread.join();
		} catch (InterruptedException e) {
			System.out.println("Thread failed");
			e.printStackTrace();
		}
		System.out.println("");
		return allLinks;
	}

	/**
	 * Filter all the links to only look for web links both internal and
	 * external
	 * 
	 * @return set of web links
	 */
	public Set<String> getWebLinks() {
		// Make regex for links
		Pattern pattern = Pattern.compile("(.*).((html)|(php)|(htm)|(com))");
		Set<String> result = new LinkedHashSet<String>();
		for (String link : allLinks) {
			// Check each of the links found
			Matcher matcher = pattern.matcher(link);
			if (matcher.lookingAt()) {
				result.add(link);
			}
		}

		return result;
	}

}
