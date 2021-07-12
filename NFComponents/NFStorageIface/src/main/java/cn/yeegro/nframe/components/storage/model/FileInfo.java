package cn.yeegro.nframe.components.storage.model;

import cn.yeegro.nframe.common.base.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * @author 作者 owen
 * @version 创建时间：2017年11月12日 上午22:57:51
 * file实体类
 */
@Data
@Entity
@Table(name = "file_info")
public class FileInfo implements Serializable {


    @Id
    @Column(name = "id")
    @NotNull(groups = BaseEntity.Update.class)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "ID", hidden = true)
    private Long id;


    @ApiModelProperty(value = "md5", hidden = true)
    private String md5;
    //  原始文件名
    @ApiModelProperty(value = "原始文件名")
    private String name;
    //	是否图片
    @ApiModelProperty(value = "是否图片")
    private Boolean isImg;
    //	上传文件类型
    @ApiModelProperty(value = "上传文件类型")
    private String contentType;
    //	文件大小
    @ApiModelProperty(value = "文件大小")
    private long size;
    //  冗余字段
    @ApiModelProperty(value = "冗余字段")
    private String path;
    //	oss访问路径 oss需要设置公共读
    @ApiModelProperty(value = "oss访问路径")
    private String url;
    //	FileType字段
    @ApiModelProperty(value = "FileType字段")
    private String source;

	@CreationTimestamp
    @ApiModelProperty(value = "创建时间")
    private Timestamp createTime;
    /**
     * 目录磁盘地址
     */
    @ApiModelProperty(value = "目录磁盘地址")
    private String pathDir;
}
