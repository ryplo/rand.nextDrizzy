package scraper;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Iterator;
import java.util.Random;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.*;

public class Scraper {

	public static void main(String[] args) throws URISyntaxException {
	try {
		getHtml();
	} catch (IOException e) {
		e.printStackTrace();
	}
	}
	
	
	public static void getHtml() throws IOException, URISyntaxException {
		Document doc = Jsoup.connect("http://www.allsongsby.com/artist/drake/271256/").get();
		String title = doc.title();
		System.out.println(title);
		
		printHeader(doc);
		printBody(doc);
		playSong(doc);

	}
	
	
	public static void printHeader(Document doc) {
		//parse + print headings 
		Element thead = doc.select("thead").first();
		Element trhead = thead.select("tr").first();
		Elements ths = trhead.select("th");
		Iterator<Element> thIt = ths.iterator();
		
		boolean header = true;

			for (int i = 0; i < 6; i++) {
				if (header) {
					Element th = (Element) thIt.next();
					if ( (i == 0) || (i == 4) || (i == 5) )
					{
					System.out.print(th.text() + "     ");
					}
				}
			}
		System.out.println("");
	}
	
	//PLays a random drake song :)
	public static void playSong (Document doc) throws IOException, URISyntaxException {
		//parse + print body
		Iterator<Element> trIt = getBodyTrIt(doc);
		Random rand = new Random();
		boolean found = false;
		while (!found) {
			Element tr = trIt.next();
			double number = rand.nextDouble();
			System.out.println( "DA RANDOM: " + number);
			URL url;
			
			if (number < 0.5) {
				System.out.println("YAY");
				Elements tds = tr.select("td");
				Iterator<Element> tdIt = tds.iterator();
				for (int i = 0; i < 4; i++) {
					Element td = tdIt.next();
					System.out.println(td.text());
					if (i == 3) {
						String link = td.select("a").attr("onclick");
						System.out.println(link);
						link = link.substring(link.indexOf("h"), link.indexOf("\"",10));
						url = new URL(link);
						Desktop.getDesktop().browse(url.toURI());
						found = true;
						break;
					}
				}
			}
		}
	}
	
	//prints body of table
	public static void printBody(Document doc) {
		//parse + print body
		Iterator<Element> trIt = getBodyTrIt(doc);
		while (trIt.hasNext()) {
			Element tr = trIt.next();
			Elements tds = tr.select("td");
			Iterator<Element> tdIt = tds.iterator();
			for (int i = 0; i < 6; i++) {
				Element td = (Element) tdIt.next();
				if ( (i == 0) || (i == 4) || (i == 5) )
				{
				System.out.print(td.text() + "     ");
				}
			}
			System.out.println("");
		}
	}
	
	//returns iterator for body table row elements
	public static Iterator<Element> getBodyTrIt(Document doc) {
		Element table = doc.select("tbody").first();
		Elements trs = table.select("tr");
		Iterator<Element> trIt = trs.iterator();
		return trIt;
	}
}