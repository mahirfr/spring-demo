package com.mahir.demo.service;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileService {

    @Value("${folder.upload}")
    private String folderUpload;

    public void uploadToLocalFileSystem(MultipartFile file, String fileName)
        throws IOException {

        Path storageDirectory = Paths.get(folderUpload);

        if (!Files.exists(storageDirectory)) {
            Files.createDirectories(storageDirectory);
        }

        Path destination = Paths.get(folderUpload + "\\" + fileName);
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
    }

    public byte[] getImageByName(String imageName) throws FileNotFoundException {
        Path destination = Paths.get(folderUpload + "/" + imageName); // Retrieve the image by its name

        try {
            return IOUtils.toByteArray(destination.toUri());
        } catch (IOException e) {
            throw new FileNotFoundException(e.getMessage());
        }

    }

    public HttpHeaders getHeaderImageType(String imageName) throws IOException {
        HttpHeaders header = new HttpHeaders();
        String mimeType = Files.probeContentType(new File(imageName).toPath());
        header.setContentType(MediaType.valueOf(mimeType));
        return header;
    }

}
