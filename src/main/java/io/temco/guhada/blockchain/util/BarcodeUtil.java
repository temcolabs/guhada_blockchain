package io.temco.guhada.blockchain.util;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Component
public class BarcodeUtil {

    @Value("${spring.aws.s3config.clientRegion}")
    private String clientRegion;
    @Value("${spring.aws.s3config.bucketName}")
    private String bucketName;
    @Value("${spring.aws.s3config.accessKey}")
    private String accessKey;
    @Value("${spring.aws.s3config.secretKey}")
    private String secretKey;
    @Value("${spring.aws.s3config.folderName}")
    private String folderName;
    @Value("${spring.aws.s3config.fileFormat}")
    private String fileFormat;

    public String generateQRCodeImageToS3Url(String text, String fileName , int width, int height) throws WriterException, IOException {

        String fileKey = folderName + "/" + fileName + "." + fileFormat ;
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);
            BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, fileFormat, os);
            InputStream inputStream = new ByteArrayInputStream(os.toByteArray());

            AWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withRegion(clientRegion)
                    .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                    .build();

            // Upload a text string as a new object.

            // Upload a file as a new object with ContentType and title specified.
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType("plain/text");
            metadata.addUserMetadata("x-amz-meta-title", "test");
            PutObjectRequest request = new PutObjectRequest(bucketName, fileKey, inputStream,metadata);
            s3Client.putObject(request);
            return s3Client.getUrl(bucketName, fileKey).toString();

        }
        catch(AmazonServiceException e) {
            // The call was transmitted successfully, but Amazon S3 couldn't process
            // it, so it returned an error response.
            e.printStackTrace();
            return e.getMessage();

        }
        catch(SdkClientException e) {
            // Amazon S3 couldn't be contacted for a response, or the client
            // couldn't parse the response from Amazon S3.
            e.printStackTrace();
            return e.getMessage();
        }

    }
}
