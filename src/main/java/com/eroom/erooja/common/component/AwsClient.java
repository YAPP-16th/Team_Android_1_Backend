package com.eroom.erooja.common.component;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class AwsClient {
    private static final Logger logger = LoggerFactory.getLogger(AwsClient.class);

    private AmazonS3 s3client;

    @Value("${amazonProperties.endpointUrl}")
    private String endpointUrl;

    @Value("${amazonProperties.bucketName}")
    private String bucketName;

    @Value("${amazonProperties.accessKey}")
    private String accessKey;

    @Value("${amazonProperties.secretKey}")
    private String secretKey;

    @PostConstruct
    private void init() {
        AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
        this.s3client = new AmazonS3Client(credentials);
    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        if (file.getOriginalFilename() == null) {
            throw new IOException("MultipartFile 의 파일 이름이 Null 일 수 없습니다.");
        }

        File convertedFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convertedFile);
        fos.write(file.getBytes());
        fos.close();
        return convertedFile;
    }

    private String generateFileName(MultipartFile file) throws IOException {
        StringBuilder sb = new StringBuilder();

        sb.append(System.currentTimeMillis());
        sb.append('.');
        sb.append((file.getOriginalFilename() == null) ?
                    "unknown" : file.getOriginalFilename().replace(" ", "_"));

        return sb.toString();
    }

    private String uploadFileTos3bucket(String rootFolder, String fileName, File file) {
        String path = rootFolder + "/" + fileName;
        s3client.putObject(new PutObjectRequest(bucketName, path, file)
                .withCannedAcl(CannedAccessControlList.PublicRead));

        return bucketName + "/" + path;
    }

    public String uploadFile(String rootFolder, MultipartFile multipartFile) throws IOException {
        String fileUrl;

        try {
            File file = convertMultiPartToFile(multipartFile);
            String fileName = generateFileName(multipartFile);
            fileUrl = endpointUrl + "/" + uploadFileTos3bucket(rootFolder, fileName, file);

            if (file.delete()) {
                logger.warn("서버 내 업로드 임시 파일이 삭제되지 않았습니다.");
            }
        } catch (Exception e) {
            logger.error("S3 에 업로드 중 에러가 발생했습니다. {}", e);
            throw e;
        }

        return fileUrl;
    }

    public List<String> listUpFilePath(String rootFolder) {
        ObjectListing listing = s3client.listObjects(bucketName, rootFolder);
        List<String> objectNames = new ArrayList<>();
        for(S3ObjectSummary summary : listing.getObjectSummaries()) {
            objectNames.add(endpointUrl + "/" + summary.getKey());
        }
        return objectNames;
    }

    public void deleteFileFromS3Bucket(String fileUrl) {
        String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
        s3client.deleteObject(new DeleteObjectRequest(bucketName + "/", fileName));
    }
}
