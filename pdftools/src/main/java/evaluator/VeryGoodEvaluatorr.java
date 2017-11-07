package evaluator;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class VeryGoodEvaluatorr {

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
		content = content.replaceAll("Bewertung:", "Bewertung: 4");
		content = content.replaceAll("Kommentare:", "Kommentare: Alles sehr gut! Weiter so!");
		
		FileUtils.write(f, content);
	}

}
