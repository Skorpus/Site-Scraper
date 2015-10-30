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
	private volatile boolean isComplete = false;

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
		Thread contentThread = new Thread(new Runnable() {
			@Override
			public void run() {
				// Collect the data from the URL
				isComplete = false;
				try {
					// Have a reader that takes in the input of the URL
					reader = new BufferedReader(new InputStreamReader(scrapeURL.openStream()));

					// Now we print it to the console to check it out
					String line;
					// Need .* as can have any number of characters in front
					// We know links will be of the form <a href="...">
					String linkPattern = "(.*)([hH][rR][eE][fF])=\"(.+)\"";
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
							// Get the substring so can just obtain the url
							String output = line.substring(matcher.start(), matcher.end());
							// Still not there yet, need to split the string and
							// get the url
							output = output.split("\"")[1];
							// Next we want to put this into a set
							allLinks.add(output);
							// System.out.println("Added to set");
						}

					}

					// Tidy up after ourselves
					reader.close();
					// System.out.println("Here....");
					isComplete = true;

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
		System.out.print("Fetching");
		while (!isComplete) {
			System.out.print(".");
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("");
		return allLinks;
	}

}
