package com.serajoon.dalaran.support.ftp.service;

import com.serajoon.dalaran.common.web.response.ResponseResult;
import com.serajoon.dalaran.support.ftp.dto.FtpDTO;
import com.serajoon.dalaran.support.ftp.model.FileBusiness;
import com.serajoon.dalaran.support.ftp.model.Fileupload;

import java.util.List;

public interface IFTPBusinessService {

    int insert(FileBusiness record);

    int insertList(List<FileBusiness> list);

    Boolean insertOrUpdate(String bussinesId, String bussinessType, List<String> fileIdList);

    int delete(FtpDTO ftpDTO);

    List<String> findFileIdByBussinessId(String businessId, String bussinessType);

    ResponseResult deleteAllFileInfos(String bussinessId, String bussinessType);

    Fileupload selectFileById(String id);
}
