package com.florianingerl.uni.pdfdownloader;

import org.codefx.libfx.control.webview.WebViewHyperlinkListener;
import org.codefx.libfx.control.webview.WebViews;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Pair;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.io.File;
import java.io.IOException;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

public class Main extends Application {

	public static final String INITIAL_URL = "https://www.portal.uni-muenchen.de/intranet/mzl_staatsexamen_online/sammlung_aufgaben/ews/psychologie/gymnasium/index.html";
	
	@Override
        public void start(Stage primaryStage) throws Exception {
    	GridPane root = new GridPane();
    	 
        root.setPadding(new Insets(20));
        root.setHgap(25);
        root.setVgap(15);
        
        Label lblUrl = new Label("Url:");
        root.add(lblUrl, 0, 0,1,1);
        
        TextField fieldUrl = new TextField();
        fieldUrl.setText(INITIAL_URL);
        GridPane.setHgrow(fieldUrl, Priority.ALWAYS);
        root.add(fieldUrl, 1, 0,1,1);
        
        Button btnLoad = new Button("Load");
        root.add(btnLoad, 2, 0,1,1);
        
        WebView webView = new WebView();
        GridPane.setHgrow(webView,  Priority.ALWAYS);
        WebEngine webEngine = webView.getEngine();
        webEngine.load(INITIAL_URL);
        
        root.add(webView, 0, 1,3,1);
        
        ListView<String> list = new ListView<String>();
        ObservableList<String> olLinks =FXCollections.observableArrayList (
            );
        list.setItems(olLinks);
        
        GridPane.setHgrow(list, Priority.ALWAYS);
        root.add(list, 0, 2,2,1);
        
        VBox vBox = new VBox();
        root.add(vBox, 2, 2,1,1);
        
        Button btnDelete = new Button("Delete");
        vBox.getChildren().add(btnDelete);
        
        Button btnClear = new Button("Clear");
        vBox.getChildren().add(btnClear);
        
        Button btnDownload = new Button("Download");
        root.add(btnDownload, 0,3,3,1);
        
        
        btnLoad.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                webEngine.load(fieldUrl.getText());
            }
        });
        
        btnClear.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				olLinks.clear();			
			}
        });
        
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
        
        btnDownload.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
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
        	
        });
        
        Scene scene = new Scene(root, 800, 800);
        primaryStage.setTitle("Pdf Downloader");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    public static void main(String[] args) {
    	
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
    	
        Application.launch(args);
    }


}
