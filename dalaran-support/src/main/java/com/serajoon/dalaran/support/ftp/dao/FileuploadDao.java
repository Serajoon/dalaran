package com.serajoon.dalaran.support.ftp.dao;


import com.serajoon.dalaran.support.ftp.model.Fileupload;
import org.apache.ibatis.annotations.Mapper;

/**
 * 附件dao
 *
 * @author hanmeng
 * @since 2019/1/22 10:07
 */
@Mapper
public interface FileuploadDao {

    /**
     * 根据附件id删除附件
     *
     * @param id 附件id
     * @return 删除的附件数
     * @author hanmeng1
     * @since 2019/1/22 10:08
     */
    int deleteById(String id);

    /**
     * 插入
     *
     * @param record record
     * @return 成功插入的附件数
     * @author hanmeng1
     * @since 2019/1/22 10:08
     */
    int insert(Fileupload record);

    /**
     * 根据附件id查询附件信息
     *
     * @param id 附件id
     * @return Fileupload对象
     * @author hanmeng1
     * @since 2019/1/22 10:08
     */
    Fileupload findById(String id);

}