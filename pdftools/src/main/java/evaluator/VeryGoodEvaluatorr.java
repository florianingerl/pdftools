package evaluator;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;

public class VeryGoodEvaluatorr {

	private static int maxPoints = -1;
	
	public static void main(String[] args) throws IOException {
		if(args.length != 1) {
			System.err.println("Arguments: folder-with-student's solutions!");
			System.exit(1);
		}
		
		File dir = new File(args[0]);
		if(!dir.exists() || !dir.isDirectory() ) {
			System.err.println(args[0] + " does not exist or is not a directory!");
			System.exit(1);
		}
		
		processDirectory(dir);
		
		System.out.println("Finsihed!");

	}

	private static void processDirectory(File dir) throws IOException {
		for(File f: dir.listFiles() ) {
			if(f.isDirectory() ) {
				processDirectory(f);
			}
			else if(f.isFile() && f.getName().startsWith("bewertung") && f.getName().endsWith(".txt") ) {
				processFile(f);
			}
		}
	}

	private static void processFile(File f) throws IOException {
		
		
		String content = FileUtils.readFileToString(f);
		
		if(maxPoints == -1) {
			Pattern p = Pattern.compile("Maximalpunktzahl\\s*:\\s*(\\d+)\\s+Punkte");
			Matcher m = p.matcher(content);
			if(m.find() ) {
				maxPoints = Integer.parseInt(m.group(1) );
				System.out.println("Maximalpunktzahl für dieses Blatt ist " + maxPoints + " Punkte!");
			}
		}
		content = content.replaceAll("Bewertung:", "Bewertung: " + maxPoints);
		content = content.replaceAll("Kommentare:", "Kommentare: Alle Kommentare zu deiner Lösung stehen in der korrigierten pdf-Datei. Für jede Aufgabe gab es 6 Punkte.");
		
		FileUtils.write(f, content);
	}

}
