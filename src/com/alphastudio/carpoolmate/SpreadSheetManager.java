package com.alphastudio.carpoolmate;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import android.util.Log;

import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.ListEntry;
import com.google.gdata.data.spreadsheet.ListFeed;
import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.data.spreadsheet.SpreadsheetFeed;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gdata.data.spreadsheet.WorksheetFeed;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;

public class SpreadSheetManager {
	String USERNAME = "ohhye89@gmail.com";
	String PASSWORD = "password";

	public void spreadSheetTest() throws IOException, ServiceException{
		SpreadsheetService service = auth();

		URL SPREADSHEET_FEED_URL = new URL(
				"https://spreadsheets.google.com/feeds/spreadsheets/private/full");

		// Make a request to the API and get all spreadsheets.
		SpreadsheetFeed feed = service.getFeed(SPREADSHEET_FEED_URL,
				SpreadsheetFeed.class);
		List<SpreadsheetEntry> spreadsheets = feed.getEntries();

		if (spreadsheets.size() == 0) {
			// TODO: There were no spreadsheets, act accordingly.
		}

		// TODO: Choose a spreadsheet more intelligently based on your
		// app's needs.
		SpreadsheetEntry spreadsheet = spreadsheets.get(0);
		System.out.println(spreadsheet.getTitle().getPlainText());

		// Get the first worksheet of the first spreadsheet.
		// TODO: Choose a worksheet more intelligently based on your
		// app's needs.
		WorksheetFeed worksheetFeed = service.getFeed(
				spreadsheet.getWorksheetFeedUrl(), WorksheetFeed.class);
		List<WorksheetEntry> worksheets = worksheetFeed.getEntries();
		WorksheetEntry worksheet = worksheets.get(0);

		// Fetch the list feed of the worksheet.
		URL listFeedUrl = worksheet.getListFeedUrl();
		ListFeed listFeed = service.getFeed(listFeedUrl, ListFeed.class);

		// Iterate through each row, printing its cell values.
		for (ListEntry row : listFeed.getEntries()) {
			// Print the first column's cell value
			Log.e("Tag", row.getTitle().getPlainText() + "\t");
			// Iterate over the remaining columns, and print each cell value
			for (String tag : row.getCustomElements().getTags()) {
				Log.e("Tag", row.getCustomElements().getValue(tag) + "\t");
			}
			System.out.println();
		}



	}

	private SpreadsheetService auth() throws AuthenticationException {
		SpreadsheetService service = 
		new SpreadsheetService("MySpreadsheetIntegration-v1");

		service.setUserCredentials(USERNAME, PASSWORD);
		
		return service;
	}

}
