package com.essay.zaloapp.services.impl;

import com.essay.zaloapp.config.FileStorageProperties;
import com.essay.zaloapp.services.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

@Service
public class FileStorageServiceImpl implements FileStorageService {
    private Path fileStorageLocation;

    public FileStorageServiceImpl() {
    }

    @Autowired
    public void FileStorageService(FileStorageProperties fileStorageProperties) {
        this.fileStorageLocation = Paths.get(System.getProperty("user.dir") + fileStorageProperties.getUploadDir()).normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Invalid");
        }
    }

//    @Override
//    public String storeFile(MultipartFile file) {
//        // Normalize file name
//        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
//
//        try {
//            // Check if the file's name contains invalid characters
//            if (fileName.contains(".."))
//                throw new RuntimeException("Invalid");
//
//            // Copy file to the target location (Replacing existing file with the same name)
//            Path targetLocation = this.fileStorageLocation.resolve(fileName);
////            System.out.println(this.fileStorageLocation);
//            String filePath = this.fileStorageLocation + "\\" + fileName;
//            ((MultipartFile) file).transferTo(new File(filePath));
//            System.out.println(filePath);
//            return fileName;
//        } catch (IOException ex) {
//            System.out.println(ex.getMessage());
//            throw new RuntimeException("Invalid");
//        }
//    }

    @Override
    public String storeFile(MultipartFile file) {
        String originalFileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String fileName = originalFileName;

        try {
            if (fileName.contains("..")) {
                throw new RuntimeException("Invalid file path");
            }

            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            int counter = 0;

            while (Files.exists(targetLocation)) {
                counter++;
                String name = originalFileName.substring(0, originalFileName.lastIndexOf('.'));
                String extension = originalFileName.substring(originalFileName.lastIndexOf('.'));
                fileName = name + "(" + counter + ")" + extension;
                targetLocation = this.fileStorageLocation.resolve(fileName);
            }

            String filePath = targetLocation.toString();
            ((MultipartFile) file).transferTo(new File(filePath));
            System.out.println("File stored at: " + filePath);
            return fileName;
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            throw new RuntimeException("Could not store file. Please try again!", ex);
        }
    }

    @Override
    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {
                return resource;
            } else {
                throw new RuntimeException("Not found!!!");
            }
        } catch (MalformedURLException ex) {
            throw new RuntimeException("Not found!!!");
        }
    }
}

