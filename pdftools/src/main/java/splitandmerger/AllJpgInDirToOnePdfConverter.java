package splitandmerger;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;

public class AllJpgInDirToOnePdfConverter {

	public static void main(String[] args) throws InvalidPasswordException, IOException {
		// TODO Auto-generated method stub
		if(args.length !=2) {
			System.err.println("Usage: alljpgindirtopdfconverter directory destfile");
			System.exit(1);
		}
		
		File dir = new File(args[0]);
		
		File [] files = dir.listFiles();
		Arrays.sort( files, ConcatPdfCreationDateSorting.creationDateComparator );
		for(File f : files) {
			if(f.getName().endsWith(".jpg") || f.getName().endsWith(".JPG") || f.getName().endsWith(".jpeg")) {
				JpgToPdfConverter.convertJpgToPdf(f);
			}
		}
		
		ConcatPdfCreationDateSorting.concatPdfs(args[0], args[1]);
		
		System.out.println("Finished!");
	}

}
