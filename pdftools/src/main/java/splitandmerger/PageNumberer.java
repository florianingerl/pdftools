package splitandmerger;

import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageContentStream.AppendMode;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

public class PageNumberer {

	public static void main(String[] args) throws InvalidPasswordException, IOException {
		if (args.length != 2) {
			System.err.println("Usage: inPdfFile outPdfFile");
			System.exit(1);
		}
		
		File outPdfFile = new File(args[1]);

		File inPdfFile = new File(args[0]);
		PDDocument inPdfDoc = PDDocument.load(inPdfFile);

		int n = inPdfDoc.getNumberOfPages();

		for (int i = 0; i < n; ++i) {
			PDPage page = inPdfDoc.getPage(i);
			
			float height = page.getBBox().getHeight();
			PDPageContentStream contentStream = new PDPageContentStream(inPdfDoc, page, AppendMode.APPEND, true);

			contentStream.beginText();
			// Setting the font to the Content stream
			contentStream.setFont(PDType1Font.TIMES_ROMAN, 15);

			// Setting the position for the line
			contentStream.newLineAtOffset(3, height-18);

			String text = "" + (i+1);

			// Adding text in the form of string
			contentStream.showText(text);

			// Ending the content stream
			contentStream.endText();

			System.out.println("Content added");

			// Closing the content stream
			contentStream.close();
			
			
		}
		
		inPdfDoc.save(outPdfFile);
		
		inPdfDoc.close();
		
		System.out.println("Finished!");
	}

}
