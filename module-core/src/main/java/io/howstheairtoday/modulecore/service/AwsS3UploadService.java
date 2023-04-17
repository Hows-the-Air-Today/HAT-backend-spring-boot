package io.howstheairtoday.modulecore.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AwsS3UploadService {

    @Value("${s3.bucket}")
    private String bucket;

    @Value("${s3.url}")
    private String s3Url;

    private final AmazonS3 amazonS3Client;

    public String uploadImages(MultipartFile file, String type) throws IOException {

        String imageUrl = "";

        try {

            String fileName = generateFileName(file, type);
            InputStream inputStream = file.getInputStream();
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());

            PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, fileName, inputStream,
                metadata).withCannedAcl(CannedAccessControlList.PublicRead);

            amazonS3Client.putObject(putObjectRequest);

            imageUrl = s3Url + fileName;

        } catch (AmazonServiceException ex) {
            // Amazon S3 서비스 예외 처리
            ex.printStackTrace();
            // 예외 처리 로직 추가
        } catch (IOException ex) {
            // I/O 예외 처리
            ex.printStackTrace();
            // 예외 처리 로직 추가
        }

        return imageUrl;
    }

    private String generateFileName(MultipartFile file, String type) {
        String extension = "";
        String originalFileName = file.getOriginalFilename();
        int dotIndex = originalFileName.lastIndexOf(".");
        if (dotIndex > 0) {
            extension = originalFileName.substring(dotIndex);
        }
        // 파일명에 현재 시간 정보 추가
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String fileName = "";
        if (type.equals("게시판")) {
            fileName = "post/" + originalFileName + "_" + timeStamp + extension;
        }
        return fileName;
    }

}

