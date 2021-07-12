package cn.yeegro.nframe.common.selector;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 *
 */
public class ApiIdempotentImportSelector implements ImportSelector {
    /**
     * Select and return the names of which class(es) should be imported based on
     * the {@link AnnotationMetadata} of the importing @{@link Configuration} class.
     *
     * @param importingClassMetadata
     */
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        return new String[]{"cn.yeegro.nframe.common.config.ApiIdempotentConfig"};
    }
}
