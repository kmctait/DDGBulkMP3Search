package com.mctait.search;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.HashSet;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class DDGSearch {

	private int numResultsObtained = 0;
	private Set<String> resultLinks = new HashSet<String>();
	private final static String DUCKDUCKGO_SEARCH_URL = "https://duckduckgo.com/html/?q=";
	
	public DDGSearch(String queryTerms, int numSearchResults, String proxyHost, String proxyPort) {
		if((proxyHost != null && !proxyHost.isEmpty()) && (proxyPort != null && !proxyPort.isEmpty())) {
			System.setProperty("http.proxyHost", proxyHost);
			System.setProperty("http.proxyPort", proxyPort);
		}
		
		String preamble = "intitle:%22index+of%22+mp3+";
		String searchQuery = DUCKDUCKGO_SEARCH_URL + preamble + "%22" + encodeForURL(queryTerms) + "%22";
		System.out.println("searchQuery: " + searchQuery);
		
		setResultLinks(getDataFromSearchEngine(searchQuery));
		setNumResultsObtained(getResultLinks().size());
	}
	
	protected Set<String> getDataFromSearchEngine(String query) {
		Set<String> result = new HashSet<String>();	
		System.out.println("Sending request..." + query);

		try {
			Document doc = Jsoup
					.connect(query)
					//.userAgent("Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)")
					.userAgent("JSOUP")
					.timeout(5000)
					.get();
			
			System.out.println(doc.html());

			Elements items = doc.getElementById("links").getElementsByClass("results_links");

			for (Element item : items) {

				Element title = item.getElementsByClass("links_main").first().getElementsByTag("a").first();
				System.out.println("\nURL:" + title.attr("href"));
				System.out.println("Title:" + title.text());
				System.out.println("Snippet:" + item.getElementsByClass("result__snippet").first().text());
				String link = title.attr("href");
				String[] array = link.split("=");
				String realLink = array[2];
				result.add(URLDecoder.decode(realLink, "UTF-8"));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	protected String getLinkURL(String line) {
		String link = new String();
		if((line.indexOf("http") >= 0) && (line.indexOf("&") >= 0)) {
			link = line.substring(line.indexOf("http"), line.indexOf("&")); 
		}
		return link;
	}
	
	protected void printSearchResults() {
		System.out.println("Num of search result links: " + getNumResultsObtained());
		
		for(String link : getResultLinks()){
			System.out.println(link);
		}	
	}

	protected int getNumResultsObtained() {
		return numResultsObtained;
	}

	protected Set<String> getResultLinks() {
		return resultLinks;
	}
	
	protected void setNumResultsObtained(int numResultsObtained) {
		this.numResultsObtained = numResultsObtained;
	}

	protected void setResultLinks(Set<String> resultLinks) {
		this.resultLinks = resultLinks;
	}
	
	private String encodeForURL(String input) {
		return input.replace(" ", "+");
	}
}
