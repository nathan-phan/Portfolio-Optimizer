package com.cs490.vo;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.jasypt.util.text.BasicTextEncryptor;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.data.spreadsheet.SpreadsheetFeed;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gdata.util.ServiceException;

public class GoogleSheetVO {
	private SpreadsheetEntry sheet;
	private List<WorksheetEntry> worksheets;

	public GoogleSheetVO() throws GeneralSecurityException, IOException, ServiceException, URISyntaxException{
		InputStream in = getClass().getClassLoader().getResourceAsStream("mysql.properties");
		Properties props = new Properties();
		String decryptedId = "";
		if (in != null) {
			props.load(in);
			String encryptedId = props.getProperty("google_id");
			BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
			textEncryptor.setPassword("keyForPass"); 
			decryptedId = textEncryptor.decrypt(encryptedId);
		} else {
			System.err.println("Uh oh properties file not found. Abort mission!");
		}
		URL SPREADSHEET_FEED_URL = new URL("https://spreadsheets.google.com/feeds/spreadsheets/private/full");
		ClassLoader classLoader = getClass().getClassLoader();
		File p12 = new File(classLoader.getResource("Portfolio Gorilla-516a29ef99d3.p12").toURI());
		HttpTransport httpTransport = new NetHttpTransport();
		JacksonFactory jsonFactory = new JacksonFactory();
		String[] SCOPESArray = {"https://spreadsheets.google.com/feeds", "https://spreadsheets.google.com/feeds/spreadsheets/private/full", "https://docs.google.com/feeds"};
		final List SCOPES = Arrays.asList(SCOPESArray);
		GoogleCredential credential = new GoogleCredential.Builder()
				.setTransport(httpTransport)
				.setJsonFactory(jsonFactory)
				.setServiceAccountId(decryptedId)
				.setServiceAccountScopes(SCOPES)
				.setServiceAccountPrivateKeyFromP12File(p12)
				.build();
		
		SpreadsheetService service = new SpreadsheetService("google-spreadsheet");

		service.setOAuth2Credentials(credential);
		SpreadsheetFeed feed = service.getFeed(SPREADSHEET_FEED_URL, SpreadsheetFeed.class);
		List<SpreadsheetEntry> spreadsheets = feed.getEntries();
		
		if (spreadsheets.size() == 0) {
			System.out.println("No spreadsheets found.");
		}

		for (int i = 0; i < spreadsheets.size(); i++) {
			if (spreadsheets.get(i).getTitle().getPlainText().equals("Portfolio Data")) {
				SpreadsheetEntry sheet = spreadsheets.get(i);
				this.setSheet(sheet);
				this.worksheets = sheet.getWorksheets();
				System.out.println("Name of editing spreadsheet: " + sheet.getTitle().getPlainText());
			}
		}
	}

	public SpreadsheetEntry getSheet() {
		return sheet;
	}

	public void setSheet(SpreadsheetEntry sheet) {
		this.sheet = sheet;
	}
	
	public WorksheetEntry getWorkSheet(String title) {
		for (WorksheetEntry worksheet : worksheets) {
      if(title.equals(worksheet.getTitle().getPlainText())){
      	return worksheet;
      }
    }
		return null;
	}

}
