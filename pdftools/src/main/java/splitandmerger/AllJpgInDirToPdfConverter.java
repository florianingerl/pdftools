package splitandmerger;

import java.io.File;
import java.util.Arrays;

public class AllJpgInDirToPdfConverter {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		if(args.length != 1) {
			System.err.println("Usage: alljpgindirtopdfconverter directory");
			System.exit(1);
		}
		
		File dir = new File(args[0]);
		convertalljpgindirtopdf(dir);
		
		System.out.println("Finished!");
		
	}
	
	public static void convertalljpgindirtopdf(File dir) {
		File [] files = dir.listFiles();
		Arrays.sort( files, ConcatPdfCreationDateSorting.creationDateComparator );
		for(File f : files ) {
			if(f.isDirectory() )
				convertalljpgindirtopdf(f);
			else if(f.getName().endsWith(".jpg") || f.getName().endsWith(".JPG") || f.getName().endsWith(".jpeg")) {
				JpgToPdfConverter.convertJpgToPdf(f);
			}
		}
	}

}
