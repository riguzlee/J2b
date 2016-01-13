package com.riguz.jb.web.ext.qr;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.jfinal.render.Render;

public class QrCodeRender extends Render {

    String                   content = "";
    int                      width   = 100;
    int                      height  = 100;

    private static final int BLACK   = 0xFF000000;
    private static final int WHITE   = 0xFFFFFFFF;

    public QrCodeRender(String content, int width, int height) {
        this.content = content;
        this.width = width;
        this.height = height;
    }

    @Override
    public void render() {
        BufferedImage image = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_RGB);
        try {
            BitMatrix matrix = QrCodeRender.toMatrix(this.content, this.width, this.height);
            image = QrCodeRender.toBufferedImage(matrix);
        }
        catch (WriterException ex) {
            ex.printStackTrace();
        }

        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");

        ServletOutputStream sos = null;
        try {
            sos = response.getOutputStream();
            ImageIO.write(image, "jpeg", sos);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
        finally {
            if (sos != null)
                try {
                    sos.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
        }

    }

    public static BitMatrix toMatrix(String content, int width, int height) throws WriterException {
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();

        Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        BitMatrix bitMatrix = multiFormatWriter.encode(content, BarcodeFormat.QR_CODE, width, height, hints);
        return bitMatrix;
    }

    public static BufferedImage toBufferedImage(BitMatrix matrix) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, matrix.get(x, y) ? BLACK : WHITE);
            }
        }
        return image;
    }

}
