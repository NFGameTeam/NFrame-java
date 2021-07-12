package cn.yeegro.nframe.plugin.usercenter.logic.feign;

import cn.yeegro.nframe.common.feign.FeignExceptionConfig;
import cn.yeegro.nframe.plugin.usercenter.logic.feign.fallback.StorageFeignClientFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author 作者 owen
 * @version 创建时间：2017年11月12日 上午22:57:51
 * 调用用户中心中的userdetail对象，用户oauth中的登录
 * 获取的用户与页面输入的密码 进行BCryptPasswordEncoder匹配
 */
@FeignClient(value = "storage-center", configuration = FeignExceptionConfig.class, fallbackFactory = StorageFeignClientFallbackFactory.class, decode404 = true)
public interface StorageFeignClient {

    /**
     * feign rpc访问远程/api-file/files-anon接口
     *
     * @param file
     * @return
     */
    @PostMapping(value = "/files-anon", params = "file",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<Object> upload(@RequestPart("file") MultipartFile file);


}
