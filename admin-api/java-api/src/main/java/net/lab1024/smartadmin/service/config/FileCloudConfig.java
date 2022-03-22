package net.lab1024.smartadmin.service.config;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @author 卓大
 */
@Data
@Configuration
@ConditionalOnProperty(prefix = "file.storage", name = {"mode"}, havingValue = "cloud")
public class FileCloudConfig {

    @Value("${file.storage.cloud.region}")
    private String region;

    @Value("${file.storage.cloud.endpoint}")
    private String endpoint;

    @Value("${file.storage.cloud.bucket-name}")
    private String bucketName;

    @Value("${file.storage.cloud.access-key}")
    private String accessKey;

    @Value("${file.storage.cloud.secret-key}")
    private String secretKey;

    @Value("${file.storage.cloud.url.expire}")
    private Long urlExpire;

    @Value("${file.storage.cloud.url.public}")
    private String publicUrl;

    /**
     * 初始化 云oss client 配置
     *
     * @return
     */
    @Bean
    public AmazonS3 initAmazonS3() {
        ClientConfiguration clientConfig = new ClientConfiguration();
        clientConfig.setProtocol(Protocol.HTTPS);
        AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
                .withClientConfiguration(clientConfig)
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endpoint, region))
                .withPathStyleAccessEnabled(false)
                .build();
        return s3Client;
    }

}
