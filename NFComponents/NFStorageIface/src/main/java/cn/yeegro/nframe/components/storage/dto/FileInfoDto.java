package cn.yeegro.nframe.components.storage.dto;

import cn.yeegro.nframe.common.base.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;

@Getter
@Setter
public class FileInfoDto implements Serializable {

    private Long id;

    private String md5;
    //  原始文件名
    private String name;
    //	是否图片
    private Boolean isImg;
    //	上传文件类型
    private String contentType;
    //	文件大小
    private long size;
    //  冗余字段
    private String path;
    //	oss访问路径 oss需要设置公共读
    private String url;
    //	FileType字段
    private String source;

    private Timestamp createTime;
    /**
     * 目录磁盘地址
     */
    private String pathDir;

}
