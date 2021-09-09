package com.hoaxify.hoaxify.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.tika.Tika;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.hoaxify.hoaxify.configuration.AppConfiguration;

@Service
@EnableScheduling
public class FileService {

	AppConfiguration appConfiguration;
	Tika tika;

	FileAttachmentRepository fileAttachmentRepository;

	public FileService(AppConfiguration appConfiguration, FileAttachmentRepository fileAttachmentRepository) {
		super();
		this.appConfiguration = appConfiguration;
		this.fileAttachmentRepository = fileAttachmentRepository;
		this.tika = new Tika();
	}

	public String saveProfileImage(String base64Image) throws IOException {
		String imageName = getRandomName();
		byte[] decodedBytes = Base64.getDecoder().decode(base64Image);
		File target = new File(appConfiguration.getFullProfileImagePath() + "/" + imageName);
		FileUtils.writeByteArrayToFile(target, decodedBytes);
		return imageName;
	}

	private String getRandomName() {
		return UUID.randomUUID().toString().replace("-", "");
	}

	public String detectType(byte[] fileArr) {
		return tika.detect(fileArr);
	}

	public void deleteProfileImage(String image) {
		try {
			Files.deleteIfExists(Paths.get(appConfiguration.getFullProfileImagePath() + "/" + image));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public FileAttachment saveAttachment(MultipartFile file) {
		FileAttachment fileAttachment = new FileAttachment();
		fileAttachment.setDate(new Date());
		String randomName = getRandomName();
		fileAttachment.setName(randomName);

		File target = new File(appConfiguration.getFullAttachmentsPath() + "/" + randomName);
		try {
			byte[] fileAsByte = file.getBytes();
			FileUtils.writeByteArrayToFile(target, fileAsByte);
			fileAttachment.setFileType(detectType(fileAsByte));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return fileAttachmentRepository.save(fileAttachment);
	}

	@Scheduled(fixedRate = 60*60*1000)
	public void cleanupStorage() {
		Date oneHourAgo = new Date(System.currentTimeMillis() - (60*60*1000));
		List<FileAttachment> oldFiles = fileAttachmentRepository.findByDateBeforeAndHoaxIsNull(oneHourAgo);
		for(FileAttachment file: oldFiles) {
			deleteAttachmentImage(file.getName());
			fileAttachmentRepository.deleteById(file.getId());
		}
	}

	private void deleteAttachmentImage(String image) {
		try {
			Files.deleteIfExists(Paths.get(appConfiguration.getFullAttachmentsPath() + "/" + image));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
