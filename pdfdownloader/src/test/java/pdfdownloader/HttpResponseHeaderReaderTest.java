package pdfdownloader;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import com.florianingerl.uni.pdfdownloader.HttpResponseHeaderReader;

class HttpResponseHeaderReaderTest {

	@Test
	void isUrlToPdfFile_shouldReturnTrue_ifItsAUrlToPdfFile()  {
		assertTrue(HttpResponseHeaderReader.isUrlToPdfFile("http://www.mathematik.uni-muenchen.de/~zenk/ss19/dtblatt1.pdf"));
	}
	
	@Test
	void isUrlToPdfFile_shouldReturnFalse_ifItsNotAUrlToPdfFile() {
		assertFalse(HttpResponseHeaderReader.isUrlToPdfFile("http://www.mathematik.uni-muenchen.de/~zenk/ss19/dglt.php"));
	}

}
