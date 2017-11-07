package splitandmerger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;

public class SplitAndMergePdf {

	private File directory;
	private File correctedPdf;

	private int i = 0;
	private PdfReader reader;

	public SplitAndMergePdf(File dir, File correctedPdf) {
		this.directory = dir;
		this.correctedPdf = correctedPdf;
	}

	public static void main(String[] args) throws IOException, DocumentException {
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

		SplitAndMergePdf sampdf = new SplitAndMergePdf(dir, correctedPdf);
		sampdf.splitAndMerge();

		System.out.println("Finished!");
	}

	public void splitAndMerge() throws IOException, DocumentException {

		reader = new PdfReader(correctedPdf.getAbsolutePath());
		int n = reader.getNumberOfPages();

		System.out.println("n = " + n);

		if (n != countPagesInDir(directory)) {
			System.err.println("Page numbers are not the same!");
			//System.exit(1);
		}

		processDir(directory);
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
		System.out.println(f.getName());
		PdfReader reader = new PdfReader(f.getAbsolutePath());
		int n = reader.getNumberOfPages();
		reader.close();
		return n;
	}

	private void processDir(File dir) throws IOException, DocumentException {
		for (File f : dir.listFiles()) {
			if (f.isDirectory())
				processDir(f);
			else if (f.getName().endsWith(".pdf"))
				processFile(f);
		}
	}

	private void processFile(File f) throws IOException, DocumentException {
		System.out.println("Processing file " + f);

		
			PdfReader currentReader = new PdfReader(f.getAbsolutePath());
			int n = currentReader.getNumberOfPages();
			currentReader.close();
		

		Document document = new Document(reader.getPageSizeWithRotation(i+1));
		String filename = f.getAbsolutePath().substring(0, f.getAbsolutePath().length() - 4) + "_corrected.pdf";
		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filename));

		document.open();
		PdfContentByte cb = writer.getDirectContent();

		PdfImportedPage page;
		int rotation;

		int j = 0;
		while (j < n) {
			++j;
			++i;
			System.out.println(i);
			document.setPageSize(reader.getPageSizeWithRotation(i));
			document.newPage();
			page = writer.getImportedPage(reader, i);
			rotation = reader.getPageRotation(i);
			rotation = reader.getPageRotation(i);
			System.out.println("Rotation = "+ rotation);
			rotation = reader.getPageRotation(i);
			double rot = rotation * Math.PI / 180;
			rot = rot + Math.PI;
			Rectangle rect = reader.getPageSizeWithRotation(i);
			System.out.println(rect +" "+ rect.getHeight() );
			if (rotation == 90 || rotation == 270) {
				cb.addTemplate(page, (float) Math.cos(rot), (float) Math.sin(rot), (float)-Math.sin(rot), (float) Math.cos(rot), 0, reader.getPageSizeWithRotation(i).getWidth());
			}
			else {
				cb.addTemplate(page, 1f, 0, 0, 1f, 0, 0);
			}
		}

		document.close();
	}

}
