package com.anudeep.eventmanager.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;

import android.os.Environment;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

public class ITextPDFHelper {

	public static void generatePDF(String pdfFileName,String pdfContent,String qrCodeImgAbsPath){
		try{
			File folder = new File(Environment.getExternalStorageDirectory(), QRCodeHelper.SDCARD_FOLDER);
			File barcodeFilePdf = new File(folder,pdfFileName + ".pdf");
			Document document = new Document();
			PdfWriter.getInstance(document,
					new FileOutputStream(barcodeFilePdf));
			document.open();

			Paragraph p = new Paragraph(pdfContent);
			p.setAlignment(Element.ALIGN_LEFT);
			document.add(p);

			Image image = Image.getInstance(qrCodeImgAbsPath);
			image.setAlignment(Element.ALIGN_CENTER);
			//image.setAbsolutePosition(500f, 650f);
			document.add(image);

			document.close();
		} catch (BadElementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
