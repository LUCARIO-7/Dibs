package org.me.dibs.service;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

public interface ImageService {
    byte[] extractBytes(MultipartFile file) throws IOException;
}
