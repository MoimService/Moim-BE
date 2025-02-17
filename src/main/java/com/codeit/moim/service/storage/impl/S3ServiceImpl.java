package com.codeit.moim.service.storage.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.codeit.moim.common.exception.ApplicationException;
import com.codeit.moim.common.exception.payload.ErrorStatus;
import com.codeit.moim.service.storage.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.Base64;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3ServiceImpl implements StorageService {
    private final AmazonS3 amazonS3;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;

    private static final int BAD_REQUEST = 400;
    @Override
    public String uploadFile(String fileEncodedBase64, String fileName) {
        if (Objects.isNull(fileName) || fileName.isEmpty()) {
            throw new ApplicationException(
                    ErrorStatus.toErrorStatus("File name is empty", BAD_REQUEST)
            );
        }

        try{
            String base64;

            //if file has prefix
            if (fileEncodedBase64.contains(",")) {
                base64 = fileEncodedBase64.split(",")[1];
            } else {
                base64 = fileEncodedBase64;
            }

            byte[] decodedBytes= Base64.getDecoder().decode(base64);

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(decodedBytes.length);
            metadata.setContentType(determineType(fileName));

            String uploadFileName = UUID.randomUUID() + "_" + fileName;
            ByteArrayInputStream input = new ByteArrayInputStream(decodedBytes);

            amazonS3.putObject(bucket, uploadFileName, input, metadata);
            return getPublicUrl(uploadFileName);
        }catch(IllegalArgumentException e){
            throw new ApplicationException(
                    ErrorStatus.toErrorStatus("File decoding fail", BAD_REQUEST)
            );
        }catch(Exception e){
            throw new ApplicationException(
                    ErrorStatus.toErrorStatus("Error while S3 upload: " + e.getMessage(), BAD_REQUEST)
            );
        }


}


    private String determineType(String fileName) {
        if (fileName.contains(".")){
            String extension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();

            return switch(extension){
                case "png" -> "png";
                case "jpeg", "jpg" -> "jpeg";
                default -> "application/octet-stream";
            };
        }

        throw new ApplicationException(
                ErrorStatus.toErrorStatus("File name is invlaid", BAD_REQUEST)
        );
    }

    private String getPublicUrl(String uploadFileName) {
        return String.format("https://%s.s3.%s.amazonaws.com/%s", bucket, amazonS3.getRegionName(), uploadFileName);
    }

}
