package com.stores.stridestar.extensions;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.Normalizer;
import java.util.regex.Pattern;

import org.springframework.core.io.ClassPathResource;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

public class CommonFunction {
    public static String SEOUrl(String fileName) {
        String normalized = Normalizer.normalize(fileName, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        String seoFileName = pattern.matcher(normalized).replaceAll("");
        seoFileName = seoFileName.toLowerCase();
        seoFileName = seoFileName.replaceAll("[^a-z0-9\\-]", "-");
        seoFileName = seoFileName.replaceAll("-+", "-"); // Loại bỏ dấu gạch ngang thừa
        return seoFileName;
    }
    public static String saveFile(String seoUrl, String directory, MultipartFile file) throws IOException {
        File saveFile = new File("src/main/resources/static/images");
        Path uploadPath =  Paths.get(saveFile + directory);

        try {
            if (Files.notExists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            try (InputStream inputStream = file.getInputStream()) {
                String extension = "";
                String fileName = file.getOriginalFilename();

                int i = fileName.lastIndexOf('.');
                if (i > 0) {
                    extension = fileName.substring(i+1);
                }
                seoUrl = seoUrl + "." + extension;
                Path filePath = uploadPath.resolve(seoUrl);
                Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
                return seoUrl;
            } catch (IOException e) {
                throw new IOException("Could not save uploaded file: " + seoUrl, e);
            }
        } catch (IOException e) {
            throw new IOException("Could not create directory: " + uploadPath, e);
        }
    }
}
