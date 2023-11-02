package me.zhengjie.util;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class PdfUtil {
    // 定义静态后缀
    public static final String SUFFIX_PDF = ".pdf";

    /**
     * PDF 转 文字，传递文件输入流
     * @param inputStream
     * @return
     */
    public static String readPdf(InputStream inputStream) {
        PDDocument document = null;
        String content = "";
        try {
            document = PDDocument.load(inputStream);
            int pageSize = document.getNumberOfPages();
            // 一页一页读取
            for (int i = 0; i < pageSize; i++) {
                // 文本内容
                PDFTextStripper stripper = new PDFTextStripper();
                // 设置按顺序输出
                stripper.setSortByPosition(true);
                stripper.setStartPage(i + 1);
                stripper.setEndPage(i + 1);
                String text = stripper.getText(document);
                content += text.trim() + "\n";
            }
            return content;
        } catch (InvalidPasswordException e) {
        } catch (IOException e) {
        } finally {
            try {
                if (document != null) {
                    document.close();
                }
            } catch (IOException e) {
            }
        }
        return null;
    }
}

