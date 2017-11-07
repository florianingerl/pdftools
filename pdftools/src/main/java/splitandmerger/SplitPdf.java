package splitandmerger;

import java.io.FileOutputStream;
import com.lowagie.text.error_messages.MessageLocalization;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;

/**
 * This class demonstrates how to split a PDF file using iText.
 * @author Bruno Lowagie
 * @since 2.1.1 (renamed to follow Java naming conventions)
 */
public class SplitPdf {
        /**
     * This class can be used to split an existing PDF file.
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        if (args.length != 4) {
            System.err.println("arguments: srcfile destfile1 destfile2 pagenumber");
        }
        else {
            try {
				int pagenumber = Integer.parseInt(args[3]);
                
				// we create a reader for a certain document
				PdfReader reader = new PdfReader(args[0]);
				// we retrieve the total number of pages
				int n = reader.getNumberOfPages();
				System.out.println("There are " + n + " pages in the original file.");
                
				if (pagenumber < 2 || pagenumber > n) {
					throw new DocumentException(MessageLocalization.getComposedMessage("you.can.t.split.this.document.at.page.1.there.is.no.such.page", pagenumber));
				}
                
				// step 1: creation of a document-object
				Document document1 = new Document(reader.getPageSizeWithRotation(1));
				Document document2 = new Document(reader.getPageSizeWithRotation(pagenumber));
				// step 2: we create a writer that listens to the document
				PdfWriter writer1 = PdfWriter.getInstance(document1, new FileOutputStream(args[1]));
				PdfWriter writer2 = PdfWriter.getInstance(document2, new FileOutputStream(args[2]));
				// step 3: we open the document
				document1.open();
				PdfContentByte cb1 = writer1.getDirectContent();
				document2.open();
				PdfContentByte cb2 = writer2.getDirectContent();
				PdfImportedPage page;
				int rotation;
				int i = 0;
				// step 4: we add content
				while (i < pagenumber - 1) {
					i++;
					document1.setPageSize(reader.getPageSizeWithRotation(i));
					document1.newPage();
					page = writer1.getImportedPage(reader, i);
					rotation = reader.getPageRotation(i);
					if (rotation == 90 || rotation == 270) {
						cb1.addTemplate(page, 0, -1f, 1f, 0, 0, reader.getPageSizeWithRotation(i).getHeight());
					}
					else {
						cb1.addTemplate(page, 1f, 0, 0, 1f, 0, 0);
					}
				}
				while (i < n) {
					i++;
					document2.setPageSize(reader.getPageSizeWithRotation(i));
					document2.newPage();
					page = writer2.getImportedPage(reader, i);
					rotation = reader.getPageRotation(i);
					if (rotation == 90 || rotation == 270) {
						cb2.addTemplate(page, 0, -1f, 1f, 0, 0, reader.getPageSizeWithRotation(i).getHeight());
					}
					else {
						cb2.addTemplate(page, 1f, 0, 0, 1f, 0, 0);
					}
					System.out.println("Processed page " + i);
				}
				// step 5: we close the document
				document1.close();
				document2.close();
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
}