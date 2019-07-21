package com.florianingerl.uni.pdfdownloader;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

public class HttpResponseHeaderReader {

	public static boolean isUrlToPdfFile(String url) {
		URLConnection conn;
		try {
			URL obj = new URL(url);
			conn = obj.openConnection();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		Map<String, List<String>> map = conn.getHeaderFields();
		if (map.get("Content-Type").contains("application/pdf")) {
			return true;
		} else {
			return false;
		}

	}

}
