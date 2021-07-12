package cn.yeegro.nframe.ribbon.core.context;

import java.util.Map;

/**
 * Ribbon discovery filter context, stores the attributes based on which the server matching will be performed.
 *
 * @author Jakub Narloch
 */
public interface RibbonFilterContext {

    /**
     * Adds the context attribute.
     *
     * @param key   the attribute key
     * @param value the attribute value
     * @return the context instance
     */
    RibbonFilterContext add(String key, String value);

    /**
     * Retrieves the context attribute.
     *
     * @param key the attribute key
     * @return the attribute value
     */
    String get(String key);

    /**
     * Removes the context attribute.
     *
     * @param key the context attribute
     * @return the context instance
     */
    RibbonFilterContext remove(String key);

    /**
     * Retrieves the attributes.
     *
     * @return the attributes
     */
    Map<String, String> getAttributes();
}
