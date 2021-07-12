package cn.yeegro.nframe.plugin.oauthcenter.logic;

import cn.yeegro.nframe.comm.code.iface.NFIPluginManager;
import cn.yeegro.nframe.plugin.oauthcenter.logic.module.NFIKaptchaConfigerModule;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.pf4j.Extension;

import java.util.Properties;

@Extension
public class NFKaptchaConfigerModule implements NFIKaptchaConfigerModule {

    private static final String KAPTCHA_BORDER = "kaptcha.border";
    private static final String KAPTCHA_TEXTPRODUCER_FONT_COLOR = "kaptcha.textproducer.font.color";
    private static final String KAPTCHA_TEXTPRODUCER_CHAR_SPACE = "kaptcha.textproducer.char.space";
    private static final String KAPTCHA_IMAGE_WIDTH = "kaptcha.image.width";
    private static final String KAPTCHA_IMAGE_HEIGHT = "kaptcha.image.height";
    private static final String KAPTCHA_TEXTPRODUCER_CHAR_LENGTH = "kaptcha.textproducer.char.length";
    private static final Object KAPTCHA_IMAGE_FONT_SIZE = "kaptcha.textproducer.font.size";


    private NFIPluginManager pPluginManager;

    private static NFKaptchaConfigerModule SingletonPtr=null;

    public static NFKaptchaConfigerModule GetSingletonPtr()
    {
        if (null==SingletonPtr) {
            SingletonPtr=new NFKaptchaConfigerModule();
            return SingletonPtr;
        }
        return SingletonPtr;
    }

    @Override
    public DefaultKaptcha KaptchaConfiger() {
        Properties properties = new Properties();
        properties.put(KAPTCHA_BORDER, "no");
        properties.put(KAPTCHA_TEXTPRODUCER_FONT_COLOR, "blue");
        properties.put(KAPTCHA_TEXTPRODUCER_CHAR_SPACE, 5);
        properties.put(KAPTCHA_IMAGE_WIDTH, "100");
        properties.put(KAPTCHA_IMAGE_HEIGHT, "35");
        properties.put(KAPTCHA_IMAGE_FONT_SIZE, "30");
        properties.put(KAPTCHA_TEXTPRODUCER_CHAR_LENGTH, "4");
        Config config = new Config(properties);
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;
    }

    @Override
    public boolean Awake() {
        return false;
    }

    @Override
    public boolean Init() {
        return false;
    }

    @Override
    public boolean AfterInit() {
        return false;
    }

    @Override
    public boolean CheckConfig() {
        return false;
    }

    @Override
    public boolean ReadyExecute() {
        return false;
    }

    @Override
    public boolean Execute() {
        return false;
    }

    @Override
    public boolean BeforeShut() {
        return false;
    }

    @Override
    public boolean Shut() {
        return false;
    }

    @Override
    public boolean Finalize() {
        return false;
    }

    @Override
    public boolean OnReloadPlugin() {
        return false;
    }
}
