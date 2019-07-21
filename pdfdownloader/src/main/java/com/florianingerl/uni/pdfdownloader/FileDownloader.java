package com.florianingerl.uni.pdfdownloader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FileDownloader {

	public static void download(URL url, Path saveTo) throws IOException {
		URLConnection conn;
		try {
			conn = url.openConnection();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		InputStream in = conn.getInputStream();
		Files.copy(in, saveTo, StandardCopyOption.REPLACE_EXISTING);
		in.close();
	}
	
	public static void main(String [] args) throws IOException {
		
		Authenticator.setDefault (new Authenticator() {
		    protected PasswordAuthentication getPasswordAuthentication() {
		        return new PasswordAuthentication ("Florian.Ingerl", "Schachflori1".toCharArray());
		    }
		});
		
		URL url = new URL("https://www.physik.uni-muenchen.de/lehre/vorlesungen/wise_18_19/t4p/_studenten/ub08l.pdf");
		
		Path path = Paths.get(System.getProperty("user.home"), "loesung8.pdf");
		download(url, path );
	}
}
