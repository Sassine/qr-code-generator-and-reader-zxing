package dev.sassine.qrcode;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Objects;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeWriter;

public class QRCodeReader {
	
	private static final String PNG_FORMAT = "PNG";
	private static final String QR_CODE_IMAGE_PATH = "./QRCode.png";

    private static String decodeQRCode(File qrCodeimage) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(qrCodeimage);
        LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

        try {
            Result result = new MultiFormatReader().decode(bitmap);
            return result.getText();
        } catch (NotFoundException e) {
            System.out.println("Não há código QR na imagem");
            return null;
        }
    }
    
    private static void generateQRCodeImage(String text, int width, int height, String filePath)
            throws WriterException, IOException {
    	
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

        Path path = FileSystems.getDefault().getPath(filePath);
        MatrixToImageWriter.writeToPath(bitMatrix, PNG_FORMAT, path);
    }

    public static void main(String[] args) throws IOException, WriterException {
            generateQRCodeImage("https://sassine.dev", 350, 350, QR_CODE_IMAGE_PATH);
    	
            File file = new File(QR_CODE_IMAGE_PATH);
            String decodedText = decodeQRCode(file);
            
            if(Objects.isNull(decodedText) || decodedText.trim().isEmpty()) {
                System.out.println("Nenhum código QR encontrado na imagem");
            } else {
                System.out.println("Código QR Encontrado: "+decodedText);
            }
        
    }
}