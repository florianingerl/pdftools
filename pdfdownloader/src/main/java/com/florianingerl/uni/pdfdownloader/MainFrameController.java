package com.florianingerl.uni.pdfdownloader;

import java.io.File;
import java.io.IOException;
import java.net.Authenticator;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import javax.swing.event.HyperlinkEvent;

import org.codefx.libfx.control.webview.WebViewHyperlinkListener;
import org.codefx.libfx.control.webview.WebViews;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.util.Pair;

public class MainFrameController {

	@FXML
	private Button btnDelete;

	@FXML
	private Button btnDownload;

	@FXML
	private TextField fieldUrl;
	
	@FXML
	private ListView<String> list;

	@FXML
	private Button btnLoad;

	@FXML
	private WebView webView;

	@FXML
	private Button btnClear;

	public static final String INITIAL_URL = "https://www.portal.uni-muenchen.de/intranet/mzl_staatsexamen_online/sammlung_aufgaben/ews/psychologie/gymnasium/index.html";

	private WebEngine webEngine;

	private ObservableList<String> olLinks;

	@FXML
	public void initialize() {
		
		CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
		Authenticator.setDefault (new Authenticator() {
    		private String username = null;
	    	private char[] password = null;
		    
	    	protected PasswordAuthentication getPasswordAuthentication() {
		    	
		    	
		    	if(username != null && password != null) {
		    		return new PasswordAuthentication(username, password);
		    	}
		    	LoginDlg loginDlg = new LoginDlg();
		        Optional<Pair<String, String>> result = loginDlg.showAndWait();
		    	if( result.isPresent() ) {
		    		username = result.get().getKey();
		    		password = result.get().getValue().toCharArray();
		    		return new PasswordAuthentication(username, password);
		    	}
		        return null;
		    }
		});
		
		
		
		fieldUrl.setText(INITIAL_URL);

		webEngine = webView.getEngine();
		webEngine.load(INITIAL_URL);

		olLinks = FXCollections.observableArrayList();
		list.setItems(olLinks);
		
		 WebViewHyperlinkListener hyperlinkListener = new WebViewHyperlinkListener() {
				@Override
				public boolean hyperlinkUpdate(HyperlinkEvent event) {
					String s = event.getURL().toString();
					if( (s.endsWith(".pdf") || HttpResponseHeaderReader.isUrlToPdfFile(s)) && !olLinks.contains(s)) {
						olLinks.add(s);
						return true;
					}
					System.out.println( s );
					return false;
				}
	        };
	        		
	        WebViews.addHyperlinkListener(
	        		webView, hyperlinkListener,
	        		HyperlinkEvent.EventType.ACTIVATED);
	}

	public void btnLoadClicked() {
		webEngine.load(fieldUrl.getText());
	}

	public void btnDeleteClicked() {

	}

	public void btnClearClicked() {
		olLinks.clear();
	}

	public void btnDownloadClicked() {
		try {
			Path dir = Files.createTempDirectory("pdfs");
			int i = 1;
			for(String link : olLinks) {
				URL url = new URL(link);
				FileDownloader.download(url, Paths.get(dir.toString(), i+".pdf"));
				i++;
			}
			
			FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save");
            File file = fileChooser.showSaveDialog(null);
			
			PdfConcater.main(new String[] { dir.toString(), file.getAbsolutePath()} );
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
