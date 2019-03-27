package com.xxx.upload;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:config/upload-dev.properties")
@ConfigurationProperties(prefix = "upload")
public class ConstantByProperties {

	public static String basePath;

	public static int uploadSpeed;

	public String getBasePath() {
		return basePath;
	}

	public void setBasePath(String basePath) {
		ConstantByProperties.basePath = basePath;
	}

	public static int getUploadSpeed() {
		return uploadSpeed;
	}

	public static void setUploadSpeed(int uploadSpeed) {
		ConstantByProperties.uploadSpeed = uploadSpeed;
	}

}
