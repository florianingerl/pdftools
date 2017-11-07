package splitandmerger;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;


public class SplitAndMergePdf2 {

	private File directory;
	private File correctedPdf;

	private Iterator<PDDocument> it;

	public SplitAndMergePdf2(File dir, File correctedPdf) {
		this.directory = dir;
		this.correctedPdf = correctedPdf;
	}

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		if (args.length != 2) {
			System.err.println("Arguments: file-with-corrections  folder-with-students-solutions");
			System.exit(1);
		}

		File correctedPdf = new File(args[0]);

		if (!correctedPdf.exists() || !correctedPdf.getName().endsWith(".pdf")) {
			System.err.println("File " + correctedPdf + " does not exist or is not a pdf!");
			System.exit(1);
		}

		File dir = new File(args[1]);

		if (!dir.exists() || !dir.isDirectory()) {
			System.err.println("File " + dir + " does not exist or is not a directory!");
			System.exit(1);
		}

		SplitAndMergePdf2 sampdf = new SplitAndMergePdf2(dir, correctedPdf);
		sampdf.splitAndMerge();

		System.out.println("Finished!");
	}

	public void splitAndMerge() throws IOException {

		PDDocument document = PDDocument.load(correctedPdf);
		int n = document.getNumberOfPages();

		System.out.println("n = " + n);

		if (n != countPagesInDir(directory)) {
			System.err.println("Page numbers are not the same!");
			//System.exit(1);
		}
		
		Splitter splitter = new Splitter();
		List<PDDocument> documents = splitter.split(document);
		it = documents.iterator();

		processDir(directory);
		
		document.close();
	}

	private int countPagesInDir(File dir) throws IOException {
		int n = 0;
		for (File f : dir.listFiles()) {
			if (f.isDirectory())
				n += countPagesInDir(f);
			else if (f.getName().endsWith(".pdf"))
				n += countPagesInFile(f);
		}
		return n;
	}

	private int countPagesInFile(File f) throws IOException {
		PDDocument document = PDDocument.load(f);
		int n = document.getNumberOfPages();
		document.close();
		return n;
	}

	private void processDir(File dir) throws IOException {
		for (File f : dir.listFiles()) {
			if (f.isDirectory())
				processDir(f);
			else if (f.getName().endsWith(".pdf"))
				processFile(f);
		}
	}

	private void processFile(File f) throws IOException {
		System.out.println("Processing file " + f);
		
		int n = countPagesInFile(f);
		File newFile = new File( f.getAbsolutePath().substring(0, f.getAbsolutePath().length() - 4) + "corrected.pdf" );
		PDFMergerUtility pdfMerger = new PDFMergerUtility(); 
		
		PDDocument document = new PDDocument();
		int j = 0;
		while (j < n ) {
			pdfMerger.appendDocument(document, it.next() );
			++j;
		}
		
		document.save(newFile);
			
	}

}
