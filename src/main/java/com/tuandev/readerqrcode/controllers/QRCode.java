package com.tuandev.readerqrcode.controllers;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.imageio.ImageIO;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

public class QRCode {
    public QRCode() {
    }

    public static String readQRCodeFromImage(File imageFile) throws IOException, NotFoundException {
        BufferedImage image = ImageIO.read(imageFile);
        if (image == null) {
            throw new IOException("Could not read the image");
        } else {
            BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(image)));
            Map<DecodeHintType, Object> hints = new HashMap();
            hints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
            hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");
            MultiFormatReader reader = new MultiFormatReader();
            Result result = reader.decode(binaryBitmap, hints);
            return result.getText();
        }
    }

    public static List<File> convertImageFromPDF(File pdfFile) throws IOException {
        List<File> imageFiles = new ArrayList();
        PDDocument document = Loader.loadPDF(pdfFile);

        try {
            String var10000 = pdfFile.getParent();
            String outDir = var10000 + File.separator + String.valueOf(UUID.randomUUID());
            File outFile = new File(outDir);
            if (!outFile.exists()) outFile.mkdirs();

            PDFRenderer pdfRenderer = new PDFRenderer(document);

            for(int i = 0; i < document.getNumberOfPages(); ++i) {
                BufferedImage bufferedImage = pdfRenderer.renderImageWithDPI(i, 500.0F, ImageType.RGB);
                String fileName = outDir + "/page_" + i + ".png";
                File image = new File(fileName);
                ImageIO.write(bufferedImage, "png", image);
                imageFiles.add(image);
            }
        } catch (Throwable var11) {
            if (document != null) {
                try {
                    document.close();
                } catch (Throwable var10) {
                    var11.addSuppressed(var10);
                }
            }

            throw var11;
        }

        document.close();

        return imageFiles;
    }
}
