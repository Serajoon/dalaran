package com.serajoon.dalaran.support.ftp.service.impl;

import com.serajoon.dalaran.common.generator.IdGenerator;
import com.serajoon.dalaran.common.util.MyCollectionUtils;
import com.serajoon.dalaran.common.web.response.ResponseResult;
import com.serajoon.dalaran.support.ftp.config.UpDownLoadProperties;
import com.serajoon.dalaran.support.ftp.dao.FileBusinessDao;
import com.serajoon.dalaran.support.ftp.dao.FileuploadDao;
import com.serajoon.dalaran.support.ftp.dto.FtpDTO;
import com.serajoon.dalaran.support.ftp.model.FileBusiness;
import com.serajoon.dalaran.support.ftp.model.Fileupload;
import com.serajoon.dalaran.support.ftp.service.IFTPBusinessService;
import com.serajoon.dalaran.support.ftp.service.IFTPService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
/**
 * FTP业务关系service
 * @author  hanmeng
 * @since  2019/3/28 18:54
 */
@Service
public class FTPBusinessServiceImpl implements IFTPBusinessService {

    @Resource
    private FileBusinessDao fileBusinessDao;

    @Resource
    private FileuploadDao fileuploadDao;

    @Resource
    private UpDownLoadProperties upDownLoadProperties;

    @Resource
    private IFTPService ftpService;

    @Override
    public int insert(FileBusiness record) {
        return fileBusinessDao.insert(record);
    }

    @Override
    public int insertList(List<FileBusiness> list) {
        int result = 0;
        if (!CollectionUtils.isEmpty(list)) {
            result = fileBusinessDao.insertList(list);
        }
        return result;
    }

    /**
     * 业务逻辑保存时调用,保存业务和附件信息到关联表
     *
     * @param bussinessType       业务类型
     * @param bussinesId 业务id
     * @param fileIdList 附件Id列表
     * @author hanmeng1
     * @since 2019/3/14 11:11
     */
    @Override
    public Boolean insertOrUpdate(String bussinesId, String bussinessType, List<String> fileIdList) {
        boolean result = false;
        try {
            //获得配置的业务类型名称
            String bussinessName = upDownLoadProperties.getBusinessMap().get(bussinessType);
            // 根据id和type查询数据库中原始的附件id
            List<String> fileListDB = fileBusinessDao.findFileIdByBussinessId(bussinesId, bussinessType);
            // 根据前端传入的附件id集合和数据库中查询的附件id集合 取差集 得出需要新增的附件集合
            List<String> addList = MyCollectionUtils.diff(fileIdList, fileListDB);
            if (!CollectionUtils.isEmpty(addList)) {
                List<FileBusiness> list = addList.parallelStream().map(t -> {
                    FileBusiness fileBusiness = new FileBusiness();
                    fileBusiness.setId(IdGenerator.randomUUID());
                    fileBusiness.setBusinessType(bussinessType);
                    fileBusiness.setBusinessName(bussinessName);
                    fileBusiness.setBusinessId(bussinesId);
                    fileBusiness.setFileId(t);
                    return fileBusiness;
                }).collect(Collectors.toList());
                insertList(list);
            }
            List<String> deleteList = MyCollectionUtils.diff(fileListDB, fileIdList);
            if(!CollectionUtils.isEmpty(deleteList)){
                FtpDTO ftpDTO = FtpDTO.builder().fileIdList(deleteList).build();
                fileBusinessDao.delete(ftpDTO);
            }
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public int delete(FtpDTO ftpDTO) {
        return fileBusinessDao.delete(ftpDTO);
    }

    @Override
    public List<String> findFileIdByBussinessId(String businessId, String bussinessType) {
        return fileBusinessDao.findFileIdByBussinessId(businessId, bussinessType);
    }

    /**
     * 业务表在删除的时候调用改方法,删除附件的所有信息
     *
     * @param bussinessId 业务表主键
     * @param bussinessType   业务类型
     * @return ResponseResult ResponseResult
     */
    @Override
    public ResponseResult deleteAllFileInfos(String bussinessId, String bussinessType) {
        ResponseResult responseResult = null;
        try {
            //根据业务id查询出业务绑定的所有file_id
            List<String> bussinessIdList = findFileIdByBussinessId(bussinessId, bussinessType);
            //根据业务表id 删除附件和业务关联表信息
            FtpDTO ftpDTO = FtpDTO.builder().businessId(bussinessId).businessType(bussinessType).build();
            delete(ftpDTO);
            bussinessIdList.forEach(t -> {
                Fileupload fileupload = fileuploadDao.findById(t);
                /// 判断
                Optional.ofNullable(fileupload).ifPresent(p -> {
                    if (Objects.nonNull(fileupload.getId()) && Objects.nonNull(fileupload.getLocation()) && Objects.nonNull(fileupload.getFileNewName())) {
                        ftpService.deleteFile(fileupload.getId(), fileupload.getLocation(), fileupload.getFileNewName());
                    }
                });
            });
            responseResult = ResponseResult.build().success("", "删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            responseResult = ResponseResult.build().failed("删除文件失败");
        }
        return responseResult;
    }

    @Override
    public Fileupload selectFileById(String id) {
        return fileuploadDao.findById(id);
    }


}
