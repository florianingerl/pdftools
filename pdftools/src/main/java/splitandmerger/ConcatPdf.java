package splitandmerger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;

import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfCopy;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.SimpleBookmark;

/**
 * Tool that can be used to concatenate existing PDF files.
 * @since 2.1.1 (renamed to follow Java naming conventions)
 */
public class ConcatPdf {
    
    /**
     * This class can be used to concatenate existing PDF files.
     * (This was an example known as PdfCopy.java)
     * @param args the command line arguments
     * @throws IOException 
     * @throws InvalidPasswordException 
     */
    public static void main(String args[]) throws InvalidPasswordException, IOException {
        if (args.length < 2) {
            System.err.println("arguments: directory-with-pdfs-to-concat destFile");
            System.exit(1);
        }
        
        File dir = new File(args[0]);
        List<File> pdfFiles = new LinkedList<File>();
        
        collectFiles(dir, pdfFiles);
        File outFile = new File(args[1]);
        
        PDFMergerUtility pdfMerger = new PDFMergerUtility();
        PDDocument document = new PDDocument();
        
        for(File f : pdfFiles) {
        	PDDocument toMerge = PDDocument.load(f);
        	pdfMerger.appendDocument(document, toMerge );
        	toMerge.close();
        }
        
        document.save(outFile);
        System.out.println("Finished!");
        
    }

	private static void collectFiles(File dir, List<File> pdfFiles) {
		for(File f : dir.listFiles() ) {
			if(f.isDirectory() )
				collectFiles(f, pdfFiles);
			else if( f.getName().endsWith(".pdf"))
				pdfFiles.add(f);
		}
	}
}