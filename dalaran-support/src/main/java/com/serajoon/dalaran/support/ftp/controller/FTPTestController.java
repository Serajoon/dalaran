package com.serajoon.dalaran.support.ftp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

// http://localhost:8000/em/ftps/upload
@Controller
public class FTPTestController {
    @RequestMapping(value = "/ftps/upload", method = RequestMethod.GET)
    public String goUploadImg() {
        return "/upload/upload.html";
    }

}
