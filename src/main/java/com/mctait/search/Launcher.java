package com.mctait.search;

import java.io.File;

public class Launcher {
	
	// TODO: result__a search for links better and watch out for No Results return
	// TODO: check that directory for downloads is present
	// TODO: clean up comments, code and System out statements
	// TODO: Take out num search results

	public final static String queryTerms = "rihanna";
	public final static int numSearchResults = 10;
	public final static String proxyHost = "";
	public final static String proxyPort = "";
	public final static String downloadFolder = "C:/tmp/mp3/";
	
	public static void main(String[] args) {
		createDownloadDirectory(downloadFolder);
		
		DDGSearch search = new DDGSearch(queryTerms, numSearchResults, proxyHost, proxyPort);
		search.printSearchResults();

		MP3Downloader mp3downloader = new MP3Downloader(proxyHost, proxyPort);
		mp3downloader.downLoadMP3sFromResults(search.getResultLinks(), downloadFolder);
	}
	
	public static void createDownloadDirectory(String folder) {
		File dir = new File(folder);
		if(!dir.exists())
			dir.mkdir();
	}
}
