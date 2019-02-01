package evaluator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;

import com.florianingerl.util.regex.Matcher;
import com.florianingerl.util.regex.Pattern;

public class FairEvaluator {

	public static void main(String[] args) throws IOException {
		
		if(args.length != 3 ) {
			System.err.println("Arguments: directory_to_zip_and_upload txt_file_with_uniworxids_and_points sheetnumber");
			System.exit(1);
		}
		
		File dir_to_zip_and_upload = new File(args[0] );
		File txt_file_with_uniworxids_and_points = new File(args[1]);
		
		Pattern pnumber = Pattern.compile("\\d+");
		Matcher mnumber = pnumber.matcher(args[2]);
		int sheetnumber = -1;
		if( mnumber.matches() ) {
			sheetnumber = Integer.parseInt( mnumber.group() );
		}
		else {
			System.err.println("Arguments: directory_to_zip_and_upload txt_file_with_uniworxids_and_points sheetnumber");
			System.exit(1);
		}
		
		String s = FileUtils.readFileToString(txt_file_with_uniworxids_and_points);
		ClassLoader classLoader = FairEvaluator.class.getClassLoader();
		File file = new File(classLoader.getResource("bewertung.txt").getFile());
		String bewertung_vorlage = FileUtils.readFileToString(file);
		bewertung_vorlage = bewertung_vorlage.replaceAll("Blatt:", "Blatt: Blatt " + sheetnumber);
		
		Pattern p = Pattern.compile("^(?<uniworxid>\\d{6})\\s+(?<points>\\d+)$", Pattern.MULTILINE);
		Matcher m = p.matcher(s);
		
		while(m.find()) {
			String uniworxid = m.group("uniworxid");
			File f = new File(dir_to_zip_and_upload, uniworxid);
			
			
			File bewertung = new File(f, "bewertung_" + uniworxid + ".txt");
			String fair_bewertung = bewertung_vorlage.replaceAll("Bewertung:", "Bewertung: " + m.group("points"));
			fair_bewertung = fair_bewertung.replaceAll("Abgabe-Id:" , "Abgabe-Id: " + uniworxid);
			
			FileUtils.writeStringToFile(bewertung, fair_bewertung);
		}
	}

}
