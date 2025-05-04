### 使用 Homebrew 安装

```
brew install minio/stable/minio
```

创建数据目录：
```
mkdir -p ~/minio_data
```


### 使用plist文件让minio开机自启，且常驻后台

创建plist文件 `~/Library/LaunchAgents`

```
nano ~/Library/LaunchAgents/io.minio.server.plist
```

写入以下内容

```
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE plist PUBLIC "-//Apple Computer//DTD PLIST 1.0//EN"
 "http://www.apple.com/DTDs/PropertyList-1.0.dtd">
<plist version="1.0">
<dict>
    <key>Label</key>
    <string>io.minio.server</string>

    <key>ProgramArguments</key>
    <array>
        <string>/opt/homebrew/bin/minio</string>
        <string>server</string>
        <string>/Users/wangyingxin/minio_data</string>
        <string>--console-address</string>
        <string>:9001</string>
    </array>

    <key>RunAtLoad</key>
    <true/>
    <key>KeepAlive</key>
    <true/>
    <key>StandardOutPath</key>
    <string>/tmp/minio.log</string>
    <key>StandardErrorPath</key>
    <string>/tmp/minio.err</string>
</dict>
</plist>
```

保存后退出（如果用 nano，按 Ctrl + O，然后 Enter，再按 Ctrl + X）。

然后启动后台服务：

```
launchctl load ~/Library/LaunchAgents/io.minio.server.plist
```

### 启动 MinIO

```
minio server ~/minio_data --console-address ":9001"
```

控制台界面：`http://localhost:9001`

API 接口：`http://localhost:9000`

默认账号密码： `minioadmin`



# 整合到 Spring Boot 项目
## 1. 添加依赖
```xml
    <dependency>
        <groupId>io.minio</groupId>
        <artifactId>minio</artifactId>
        <version>8.5.17</version>
    </dependency>
```
## 2. application.yml 配置文件
application-dev.yml
```yaml
minio:
  endpoint: http://127.0.0.1:9000
  access-key: qYkUmcvhl64D1r5iVp6X
  secret-key: 85PtgFJ9LpCKXp47z74KI0L340VZecnRAoWndNx7
  bucket: sky-local-oss
```
application.yml
```yaml
minio:
  endpoint: ${minio.endpoint}
  access-key: ${minio.access-key}
  secret-key: ${minio.secret-key}
  bucket: ${minio.bucket}
```

## 3. 创建配置类
```java
package com.sky.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "minio")
public class MinioProperties {
    private String endpoint;
    private String accessKey;
    private String secretKey;
    private String bucket;
}
```
## 4. 创建 MinioUtil 工具类
```java
package com.sky.utils;

import com.sky.properties.MinioProperties;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Component
@RequiredArgsConstructor
public class MinioUtil {
    
    private final MinioProperties minioProperties;
    
    private MinioClient getClient() {
        return MinioClient.builder()
                .endpoint(minioProperties.getEndpoint())
                .credentials(minioProperties.getAccessKey(), minioProperties.getSecretKey())
                .build();
    }
    
    public void uploadFile(String objectName, InputStream inputStream, String contentType) {
        try {
            MinioClient client = getClient();
            
            // 自动创建桶
            boolean found = client.bucketExists(BucketExistsArgs.builder().bucket(minioProperties.getBucket()).build());
            if (!found) {
                client.makeBucket(MakeBucketArgs.builder().bucket(minioProperties.getBucket()).build());
            }
            
            client.putObject(
                    PutObjectArgs.builder()
                            .bucket(minioProperties.getBucket())
                            .object(objectName)
                            .stream(inputStream, -1, 10485760)
                            .contentType(contentType)
                            .build()
            );
        
        } catch (Exception e) {
            throw new RuntimeException("上传文件失败：" + e.getMessage(), e);
        }
    }
    
    public String getFileUrl(String objectName) {
        return String.format("%s/%s/%s", minioProperties.getEndpoint(), minioProperties.getBucket(), objectName);
    }
}
```
## 5. 使用示例
```java
package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.utils.MinioUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/admin/common")
@Slf4j
@RequiredArgsConstructor
public class CommonController {
    
    private final MinioUtil minioUtil;
    
    /**
     * 文件上传到minio
     *
     * @param file 这个是SpringMVC提供的
     * @return
     */
    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID() + "-" + file.getOriginalFilename();
        
        minioUtil.uploadFile(fileName, file.getInputStream(), file.getContentType());
        return Result.success(minioUtil.getFileUrl(fileName));
    }
}
```
