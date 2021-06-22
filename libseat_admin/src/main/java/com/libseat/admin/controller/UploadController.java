package com.libseat.admin.controller;

import com.libseat.utils.file.FileUploadUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin
@RequestMapping("/api/upload")
@Controller
public class UploadController {

    @PostMapping("/image")
    @ResponseBody
    public String fileUpload(@RequestBody MultipartFile file) {
        String imgUrl = FileUploadUtil.uploadImage(file);
        return imgUrl;
    }
}
