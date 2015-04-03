package javax.imageio;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;

public class ImageIO {

	public static Bitmap read(File input) throws IOException {
		return BitmapFactory.decodeFile(input.toString());
	}
	
	public static Bitmap read(InputStream input) throws IOException {
        if (input == null) {
            throw new IllegalArgumentException("input == null!");
        }
        BufferedInputStream bufferedInputStream = new BufferedInputStream(input);
        return  BitmapFactory.decodeStream(bufferedInputStream);
    }

	public static boolean write(Bitmap im, String formatName, OutputStream output) throws IOException {
		if (im == null ) {
			throw new IllegalArgumentException("im == null!");
		}
		if (formatName == null) {
			throw new IllegalArgumentException("formatName == null!");
		}
		if (output == null) {
			throw new IllegalArgumentException("output == null!");
		}
		
		formatName = formatName.toLowerCase();
		if(formatName.equals("png")){
			im.compress(CompressFormat.PNG, 100, output);
			output.close();
			return true;
		}
		else if(formatName.equals("jpeg") || formatName.equals("jpg")){
			im.compress(CompressFormat.JPEG, 100, output);
			output.close();
			return true;
		}
		else throw new IllegalArgumentException("formatName == unsupported!");
		
	}

}
