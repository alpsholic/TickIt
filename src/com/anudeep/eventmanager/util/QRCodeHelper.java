package com.anudeep.eventmanager.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.os.Environment;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.google.zxing.qrcode.QRCodeWriter;

public class QRCodeHelper {

	private static QRCodeReader reader;
	private static QRCodeWriter writer;

	private static final int WHITE = 0xFFFFFFFF;
	private static final int BLACK = 0xFF000000;

	private static final int DEFAULT_WIDTH = 125;
	private static final int DEFAULT_HEIGHT = 125;

	public static final String SDCARD_FOLDER = "eventmanager";
	//private static final String TAG = GenerateTickets.class.getSimpleName();

	static{
		reader = new QRCodeReader();
		writer = new QRCodeWriter();
	}

	public static String save(Bitmap bitmap,String fileName){
		File folder = new File(Environment.getExternalStorageDirectory(), SDCARD_FOLDER);
		if (!folder.exists() && !folder.mkdirs()) {
			//Log.w(TAG, "Couldn't make dir " + barcodesRoot);
			//showErrorMessage(R.string.msg_unmount_usb);
			folder = new File(Environment.getDataDirectory(),SDCARD_FOLDER);
		}
		File barcodeFile = new File(folder,fileName+".png");
		barcodeFile.delete();
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(barcodeFile);
			bitmap.compress(Bitmap.CompressFormat.PNG, 0, fos);
			return barcodeFile.getAbsolutePath();
		} catch (FileNotFoundException fnfe) {
			//Log.w(TAG, "Couldn't access file " + barcodeFile + " due to " + fnfe);
			//showErrorMessage(R.string.msg_unmount_usb);
			return "";
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException ioe) {
					// do nothing
				}
			}
		}
	}
	public static Bitmap encode(String text){
		try {
			BitMatrix result = writer.encode(text, BarcodeFormat.QR_CODE, DEFAULT_WIDTH, DEFAULT_HEIGHT);
			int width = result.getWidth();
			int height = result.getHeight();
			int[] pixels = new int[width * height];
			for (int y = 0; y < height; y++) {
				int offset = y * width;
				for (int x = 0; x < width; x++) {
					pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
				}
			}
			Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
			bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
			return bitmap;
			// MediaStore.Images.Media.insertImage(getContentResolver(),file.getAbsolutePath(),file.getName(),file.getName());
		} catch (WriterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	public static String decode(String absImageFilePath){
		try {
			RGBLuminanceSource source = new RGBLuminanceSource(absImageFilePath);
			BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
			Result rawResult = reader.decode(bitmap);
			return rawResult.getText();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ReaderException re) {
			// continue
		} finally {
			reader.reset();
		}
		return "";		
	}

}
