package com.serajoon.dalaran.support.ftp.dao;


import com.serajoon.dalaran.support.ftp.model.Fileupload;

public interface FileuploadDao {

    int deleteById(String id);

    int insert(Fileupload record);

    Fileupload findById(String id);

}