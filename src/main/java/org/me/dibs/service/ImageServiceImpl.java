package org.me.dibs.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@Service
public class ImageServiceImpl implements ImageService {
    @Override
    public byte[] extractBytes(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            return new byte[0];
        }
        return file.getBytes();
    }
}
