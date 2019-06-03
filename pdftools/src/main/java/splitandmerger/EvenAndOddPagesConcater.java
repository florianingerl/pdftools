package splitandmerger;

import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;

public class EvenAndOddPagesConcater {

	public static void main(String[] args) throws InvalidPasswordException, IOException {
		// TODO Auto-generated method stub
		if(args.length != 3) {
			System.err.println("arguments: pdffilewithoddpages pdffilewithevenpages outfile");
			System.exit(1);
		}
		File outFile = new File(args[2]);
		File oddPagesFile = new File(args[0]);
		PDDocument oddPagesDoc = PDDocument.load(oddPagesFile);
		File evenPagesFile = new File(args[1]);
		PDDocument evenPagesDoc = PDDocument.load(evenPagesFile);

		int n = oddPagesDoc.getNumberOfPages();
		int m = evenPagesDoc.getNumberOfPages();
		
		if(!(n==m || n==m+1)) {
			System.err.println("The file with the odd number of pages must have zero or one page more than the other file!");
			System.exit(1);
		}
		
		PDFMergerUtility pdfMerger = new PDFMergerUtility();
		PDDocument document = new PDDocument();

		Splitter splitter = new Splitter();
		
		for (int i=0; i < n; ++i) {
			pdfMerger.appendDocument(document, splitter.split(oddPagesDoc).get(i));
			if(i < m) {
				pdfMerger.appendDocument(document, splitter.split(evenPagesDoc).get(i));
			}
		}

		document.save(outFile);
		document.close();
		
		evenPagesDoc.close();
		oddPagesDoc.close();
		System.out.println("Finished!");
	}

}
