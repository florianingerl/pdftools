package splitandmerger;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;

public class RemovePdfPage {

	public static void main(String[] args) throws InvalidPasswordException, IOException {
		if(args.length != 3) {
			System.err.println("Arguments: pdfFile newFile pagenumber");
			System.exit(1);
		}
		
		File pdfFile = new File(args[0]);
		if(!pdfFile.exists() || !pdfFile.getName().endsWith(".pdf")) {
			System.err.println("File " + pdfFile + " does not exist or is not a pdf!");
			System.exit(1);
		}
		
		if( ! Pattern.compile("\\d+").matcher(args[2]).matches() ) {
			System.err.println(args[1] + " is not a number!");
			System.exit(1);
		}
		
		int pageToRemove = Integer.parseInt(args[2]);
		
		PDDocument document = PDDocument.load(pdfFile);
		int n = document.getNumberOfPages();
		
		if(pageToRemove < 1 || pageToRemove > n) {
			System.err.println(pageToRemove + " is not a page number in file " +  args[0]);
			System.exit(1);
		}
		
		Splitter splitter = new Splitter();
		List<PDDocument> documents = splitter.split(document);
		Iterator<PDDocument> it = documents.iterator();
		
		PDFMergerUtility pdfMerger = new PDFMergerUtility();
        PDDocument newDoc = new PDDocument();
        
        for(int i=1; i <= n; ++i ) {
        	if(i == pageToRemove) {
        		it.next();
        		continue;
        	}
        	pdfMerger.appendDocument(newDoc, it.next() );
        }
        
        newDoc.save(args[1]);
        newDoc.close();
        
        document.close();
        System.out.println("Finished!");
	}

}
