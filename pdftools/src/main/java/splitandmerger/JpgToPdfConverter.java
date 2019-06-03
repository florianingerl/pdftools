package splitandmerger;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

public class JpgToPdfConverter {
	
	public static void main(String [] args) {
		if(args.length == 0) {
			System.err.println("Usage: jpgtopdfconverter jpgFile");
			System.exit(1);
		}
		
		convertJpgToPdf(new File(args[0]) );
		
		System.out.println("Finished!");
	}

	public static void convertJpgToPdf(File jpgFile) {
		try (PDDocument doc = new PDDocument())
        {
			InputStream in = new FileInputStream(jpgFile);
			BufferedImage bimg = ImageIO.read(in);
			
			PDImageXObject imageObject = LosslessFactory.createFromImage(doc, bimg);
            
			float width = bimg.getWidth();
			float height = bimg.getHeight();
			PDPage page = new PDPage(new PDRectangle(width, height));
			doc.addPage(page); 
			
			PDPageContentStream contentStream = new PDPageContentStream(doc, page);
			contentStream.drawImage(imageObject, 0, 0);
			contentStream.close();
			in.close();

			
            String s = jpgFile.getAbsolutePath();
            String pdfPath = s.substring(0, s.length()-4) + ".pdf";
            doc.save(pdfPath);
            doc.close();
        } catch (IOException e) {
			e.printStackTrace();
		}
	}
}
