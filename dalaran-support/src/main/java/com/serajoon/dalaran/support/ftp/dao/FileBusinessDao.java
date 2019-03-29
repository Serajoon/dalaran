package com.serajoon.dalaran.support.ftp.dao;

import com.serajoon.dalaran.support.ftp.dto.FtpDTO;
import com.serajoon.dalaran.support.ftp.model.FileBusiness;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author hanmeng
 * @since 2019/1/22 10:04
 */
@Mapper
public interface FileBusinessDao {

    /**
     * 单个插入
     *
     * @param record FileBusiness对象
     * @return 成功插入的数据条数
     */
    int insert(FileBusiness record);

    /**
     * 批量插入
     *
     * @param list 数据list
     * @return 成功插入的数据条数
     */
    int insertList(List<FileBusiness> list);

    /**
     * 根据传入的FtpDTO删除
     *
     * @param ftpDTO ftpDTO
     * @return 成功删除的数据数量
     */
    int delete(FtpDTO ftpDTO);

    /**
     * 根据业务主键查找对应的附件Id
     * @param businessId 业务主键
     * @param type       业务类型类型
     * @return 附件和业务关联表对应的附件主键列表集合
     */
    List<String> findFileIdByBussinessId(@Param("businessId") String businessId, @Param("type") String type);

}