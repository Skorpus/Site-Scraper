import java.util.Scanner;

import org.apache.commons.validator.routines.UrlValidator;

/**
 * Goal of this project is to be able to develop a page scraper whereby the user
 * presents a web page and this obtains all links found on that page.
 * 
 * Initially this will just present them in a list but can be left for further
 * improvement
 * 
 * @author dmaccora
 *
 */
public class Driver {

	public static void main(String[] args) {
		// Can ensure that the URL is correct using Apache URLValidator
		UrlValidator validator = new UrlValidator();
		System.out.println("Enter the website you wish to scrape");
		Scanner in = new Scanner(System.in);
		String userURL = in.next();
		if (validator.isValid(userURL)) {
			// For now will just provide it with the data
			Scraper scraper = new Scraper(userURL);
			scraper.getContent();

			// Print them all!!!
			System.out.println(scraper.getLinks());

			System.out.println(scraper.getWebLinks());
		} else {
			System.out.println("Please don't be ridiculous");
		}
		in.close();

	}

}
