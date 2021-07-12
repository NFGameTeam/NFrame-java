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

@Data
@Entity
@Table(name = "file_info_extend")
public class FileExtend implements  Serializable{

    @Id
    @Column(name = "id")
    @NotNull(groups = BaseEntity.Update.class)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "ID")
    private Long id;

    //  md5字段
    @ApiModelProperty(value = "md5")
    private String md5;
    // 文件分片id
    @ApiModelProperty(value = "文件分片id")
    private String guid;
    //  原始文件名
    @ApiModelProperty(value = "原始文件名")
    private String name;
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
}
