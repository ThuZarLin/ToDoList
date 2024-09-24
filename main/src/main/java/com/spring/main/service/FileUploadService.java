package com.spring.main.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.spring.main.form.UserProfileEditForm;

import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class FileUploadService {
    @Autowired
    private AmazonS3 s3Client;

    @Value("${cloud.aws.region.static}")    
    private String region;

    @Value("${aws.s3.bucketName}")    
    private String bucketName;

    private String fileName;

    public String uploadImage(final MultipartFile multiPartToFile, final String folderName){
        try{
            final File file = convertMultiPartFileToFile(multiPartToFile);

            if(!doesBucketExist(bucketName)){
                throw new RuntimeException("Bucket " + bucketName + " doesn't exist.");
            }

            fileName = uploadFileToS3Bucket(bucketName, folderName, file);
            System.out.println("Uploaded file name: " + fileName);
            file.delete();
        }catch(final AmazonServiceException e){
            System.out.println("Amazon Service Error: " + e.getErrorMessage());
        }catch(final Exception e){
            System.out.println("General Error: " + e.getMessage());
        }
        return fileName;
    }

    private String uploadFileToS3Bucket(final String bucketName, final String folderName, final File file) {
        fileName = folderName + "/" + System.currentTimeMillis() + "" + file.getName().replaceAll("\\s+", "");
        final PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, fileName, file);
        s3Client.putObject(putObjectRequest);
        return fileName;
    }

    private boolean doesBucketExist(String bucketName2) {
        return s3Client.doesBucketExistV2(bucketName2);
    }

    private File convertMultiPartFileToFile(final MultipartFile multiPartToFile) {
        File file = new File(multiPartToFile.getOriginalFilename());
        try(final FileOutputStream fos = new FileOutputStream(file)){
            fos.write(multiPartToFile.getBytes());
        }catch(final IOException e){
            System.out.println("Error Message: " + e.getMessage());
        }
        return file;
    }
}