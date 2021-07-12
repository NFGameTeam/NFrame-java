package cn.yeegro.nframe.storage.task;

import java.io.*;
import java.util.concurrent.Callable;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * @author pm 1280415703@qq.com
 * @date 2019/8/11 16:30
 */

public class TaskUnZipCall implements Callable<Boolean> {
    ZipEntry entry;
    private ZipFile zip;
    //磁盘路径
    private String descDir;

    public TaskUnZipCall(ZipFile zip, ZipEntry entry, String descDir) {
        this.entry = entry;
        this.zip = zip;
        this.descDir = descDir;
    }

    @Override
    public Boolean call() {

        //线程执行任务
        try ( InputStream in = zip.getInputStream(entry)){
            String outPath = (descDir + entry.getName()).replace("/", File.separator);
            //判断路径是否存在,不存在则创建文件路径
            File file = new File(outPath.substring(0, outPath.lastIndexOf(File.separator)));
            if (!file.exists()) {
                file.mkdirs();
            }
//            判断文件全路径是否为文件夹,如果是上面已经上传,不需要解压
            if (new File(outPath).isDirectory()) {
                return true;
            }
            try(   OutputStream out = new FileOutputStream(outPath)){
            	 byte[] buf1 = new byte[1048576];//1073741824
                 int len;
                 while ((len = in.read(buf1)) > 0) {
                     out.write(buf1, 0, len);
                 }
            }
            
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
