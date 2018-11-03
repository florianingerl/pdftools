package evaluator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;

public class PointEvaluator {

	private static int maxPoints = -1;
	
	private static ArrayList<Integer> points = new ArrayList<Integer>();
	private static int pointsPos = 0;
	
	public static void main(String[] args) throws IOException {
		if(args.length != 2) {
			System.err.println("Arguments: folder-with-student-solutions  file-with-points");
			System.exit(1);
		}
		
		File pointsFile = new File(args[1]);
		readPointsFile(pointsFile);
		
		
		File dir = new File(args[0]);
		if(!dir.exists() || !dir.isDirectory() ) {
			System.err.println(args[0] + " does not exist or is not a directory!");
			System.exit(1);
		}
		
		processDirectory(dir);
		
		System.out.println("Finsihed!");

	}
	
	private static Pattern p = Pattern.compile("(.*?)(\\d+)");
	private static void readPointsFile(File file) {
		if (!file.canRead() || !file.isFile())
            System.exit(0);

            BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(file));
            String zeile = null;
            while ((zeile = in.readLine()) != null) {
                System.out.println("Gelesene Zeile: " + zeile);
                Matcher m = p.matcher(zeile);
                if(!m.matches()) { System.err.println("Das sollte nicht passieren!"); System.exit(1); }
                points.add(Integer.parseInt(m.group(2)) );
                System.out.println(m.group(1) + " has got " + m.group(2) + " points");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null)
                try {
                    in.close();
                } catch (IOException e) {
                }
        } 
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
		//content = content.replaceAll("Bewertung:", "Bewertung: " + maxPoints);
		content = content.replaceAll("Bewertung:(\\d+)?", "Bewertung: " + points.get(pointsPos++));
		content = content.replaceAll("Kommentare:", "Kommentare: Alle Kommentare zu deiner Lösung stehen in der korrigierten pdf-Datei.");
		
		FileUtils.write(f, content);
	}

}
