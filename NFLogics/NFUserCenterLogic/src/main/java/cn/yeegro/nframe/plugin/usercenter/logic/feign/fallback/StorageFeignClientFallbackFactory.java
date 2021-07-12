package cn.yeegro.nframe.plugin.usercenter.logic.feign.fallback;

import cn.yeegro.nframe.plugin.usercenter.logic.feign.StorageFeignClient;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Component
public class StorageFeignClientFallbackFactory implements FallbackFactory<StorageFeignClient> {
    @Override
    public StorageFeignClient create(Throwable throwable) {
        return new StorageFeignClient() {
            @Override
            public ResponseEntity<Object> upload(MultipartFile file) {
                log.error("上传文件失败:{}", file.getName(), throwable);
                return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
            }
        };
    }
}
