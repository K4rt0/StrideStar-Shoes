package com.stores.stridestar.services;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.stores.stridestar.access.AWSProperties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class AmazonS3Service {

    @Autowired
    private AmazonS3 amazonS3;

    @Autowired
    private AWSProperties awsProperties;

    public String uploadFile(MultipartFile multipartFile, String seoUrl) {
        File file = convertMultiPartFileToFile(multipartFile);
        
        String fileName = new Date().getTime() + "_" + seoUrl;

        PutObjectRequest putObjectRequest = new PutObjectRequest(awsProperties.getBucket(), fileName, file);

        amazonS3.putObject(putObjectRequest);
        file.delete();

        URL url = amazonS3.getUrl(awsProperties.getBucket(), fileName);
        return url.toString();
    }

    private File convertMultiPartFileToFile(MultipartFile file) {
        File convertedFile = new File(file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(file.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return convertedFile;
    }
    public S3ObjectInputStream getFile(String fileName) {
        S3Object s3Object = amazonS3.getObject(new GetObjectRequest(awsProperties.getBucket(), fileName));
        return s3Object.getObjectContent();
    }
    public String deleteFile(String fileName) {
        amazonS3.deleteObject(new DeleteObjectRequest(awsProperties.getBucket(), fileName));
        return "File deleted successfully";
    }
    public List<String> listFiles() {
        ListObjectsV2Result result = amazonS3.listObjectsV2(awsProperties.getBucket());
        List<S3ObjectSummary> objects = result.getObjectSummaries();
        List<String> fileNames = new ArrayList<>();
        for (S3ObjectSummary os : objects) {
            fileNames.add(os.getKey());
        }
        return fileNames;
    }
    public String getFileName(String url) {
        String[] parts = url.split("https://java-sb-stridestar.s3.ap-southeast-2.amazonaws.com/");
        return parts[parts.length - 1];
    }
}