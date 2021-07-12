package cn.yeegro.nframe.components.storage.utils;

import java.io.*;
import java.nio.channels.FileChannel;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 句柄关闭工具类. 避免在代码中有过多的 try ... catch ...
 * @author yuanms
 */
@SuppressWarnings("deprecation")
public class IOCloseUtil {
  
  /**
   * 安全关闭, 解决 IO 关闭时需要 try ... catch ... 造成代码太麻烦的问题<br>
   * 支持如下对象的关闭：<br><b>
   *    BufferedInputStream, BufferedOutputStream;<br>
   *    BufferedReader, BufferedWriter;<br>
   *    ByteArrayInputStream, ByteArrayOutputStream;<br>
   *    CharArrayReader, CharArrayWriter;<br>
   *    DataInputStream, DataOutputStream;<br>
   *    File, FileDescriptor, FileInputStream, FileOutputStream, FilePermission, FileReader, FileWriter, FilterInputStream, FilterOutputStream, FilterReader, FilterWriter;<br> 
   *    InputStream, InputStreamReader;<br>
   *    LineNumberReader;<br>    
   *    ObjectInputStream, ObjectOutputStream, ObjectStreamClass, ObjectStreamField, OutputStream, OutputStreamWriter;<br>
   *    PipedInputStream, PipedOutputStream, PipedReader, PipedWriter, PrintStream, PrintWriter, PushbackInputStream, PushbackReader;<br>
   *    RandomAccessFile, Reader;<br>
   *    SequenceInputStream, SerializablePermission, StreamTokenizer, StringBufferInputStream, StringReader, StringWriter;<br>
   *    Writer;<br>
   *    FileChannel
   *<b/>
   * @param args 可变参数.
   */
  public static void close(Object...args){
    if(null == args || args.length <= 0)
      return;
    for( Object item : args){
      if( null == item )
        continue;
      if( item instanceof BufferedInputStream){
        try {
          ((BufferedInputStream) item).close();
        } catch (IOException e) {

          Logger.getLogger(IOCloseUtil.class.getName()).log(Level.SEVERE, "BufferedInputStream close exception:", e);

        }
      }
      
      else if( item instanceof BufferedOutputStream){
        try {
          ((BufferedOutputStream) item).close();
        } catch (IOException e) {
          Logger.getLogger(IOCloseUtil.class.getName()).log(Level.SEVERE, "BufferedOutputStream close exception:", e);
        }
      }
      
      else if( item instanceof OutputStream){
        try {
          ((OutputStream) item).close();
        } catch (IOException e) {

          Logger.getLogger(IOCloseUtil.class.getName()).log(Level.SEVERE, "OutputStream close exception:", e);

        }
      }
      
      else if( item instanceof InputStream){
        try {
          ((InputStream) item).close();
        } catch (IOException e) {
          Logger.getLogger(IOCloseUtil.class.getName()).log(Level.SEVERE, "InputStream close exception:", e);
        }
      }
      
      else if( item instanceof BufferedReader){
        try {
          ((BufferedReader) item).close();
        } catch (IOException e) {
          Logger.getLogger(IOCloseUtil.class.getName()).log(Level.SEVERE, "BufferedReader close exception:", e);
        }
      }
      
      else if( item instanceof BufferedWriter){
        try {
          ((BufferedWriter) item).close();
        } catch (IOException e) {
          Logger.getLogger(IOCloseUtil.class.getName()).log(Level.SEVERE, "BufferedWriter close exception:", e);
        }
      }
      
      else if( item instanceof FileChannel){
        try {
          ((FileChannel) item).close();
        } catch (IOException e) {
          Logger.getLogger(IOCloseUtil.class.getName()).log(Level.SEVERE, "FileChannel close exception:", e);
        }
      }
      
      else if( item instanceof FileInputStream){
        try {
          ((FileInputStream) item).close();
        } catch (IOException e) {
          Logger.getLogger(IOCloseUtil.class.getName()).log(Level.SEVERE, "FileInputStream close exception:", e);
        }
      }
      
      else if( item instanceof FileOutputStream){
        try {
          ((FileOutputStream) item).close();
        } catch (IOException e) {
          Logger.getLogger(IOCloseUtil.class.getName()).log(Level.SEVERE, "FileOutputStream close exception:", e);
        }
      }
      
      else if( item instanceof InputStreamReader){
        try {
          ((InputStreamReader) item).close();
        } catch (IOException e) {
          Logger.getLogger(IOCloseUtil.class.getName()).log(Level.SEVERE, "InputStreamReader close exception:", e);
        }
      }
      
      else if( item instanceof ByteArrayInputStream){
        try {
          ((ByteArrayInputStream) item).close();
        } catch (IOException e) {
          Logger.getLogger(IOCloseUtil.class.getName()).log(Level.SEVERE, "ByteArrayInputStream close exception:", e);
        }
      }
      
      else if( item instanceof ByteArrayOutputStream){
        try {
          ((ByteArrayOutputStream) item).close();
        } catch (IOException e) {
          Logger.getLogger(IOCloseUtil.class.getName()).log(Level.SEVERE, "ByteArrayOutputStream close exception:", e);
        }
      }
      
      else if( item instanceof CharArrayReader){
        ((CharArrayReader) item).close();
      }
      
      else if( item instanceof CharArrayWriter){
        ((CharArrayWriter) item).close();
      }
      
      else if( item instanceof DataInputStream){
        try {
          ((DataInputStream) item).close();
        } catch (IOException e) {
          Logger.getLogger(IOCloseUtil.class.getName()).log(Level.SEVERE, "DataInputStream close exception:", e);
        }
      }
      
      else if( item instanceof DataOutputStream){
        try {
          ((DataOutputStream) item).close();
        } catch (IOException e) {
          Logger.getLogger(IOCloseUtil.class.getName()).log(Level.SEVERE, "DataOutputStream close exception:", e);
        }
      }
      
      else if( item instanceof FileReader){
        try {
          ((FileReader) item).close();
        } catch (IOException e) {
          Logger.getLogger(IOCloseUtil.class.getName()).log(Level.SEVERE, "FileReader close exception:", e);
        }
      }
      
      else if( item instanceof FileWriter){
        try {
          ((FileWriter) item).close();
        } catch (IOException e) {
          Logger.getLogger(IOCloseUtil.class.getName()).log(Level.SEVERE, "FileWriter close exception:", e);
        }
      }
      
      else if( item instanceof FilterInputStream){
        try {
          ((FilterInputStream) item).close();
        } catch (IOException e) {
          Logger.getLogger(IOCloseUtil.class.getName()).log(Level.SEVERE, "FilterInputStream close exception:", e);
        }
      }
      
      else if( item instanceof FilterOutputStream){
        try {
          ((FilterOutputStream) item).close();
        } catch (IOException e) {
          Logger.getLogger(IOCloseUtil.class.getName()).log(Level.SEVERE, "FilterOutputStream close exception:", e);
        }
      }
      
      else if( item instanceof FilterReader){
        try {
          ((FilterReader) item).close();
        } catch (IOException e) {
          Logger.getLogger(IOCloseUtil.class.getName()).log(Level.SEVERE, "FilterReader close exception:", e);
        }
      }
      
      else if( item instanceof FilterWriter){
        try {
          ((FilterWriter) item).close();
        } catch (IOException e) {
          Logger.getLogger(IOCloseUtil.class.getName()).log(Level.SEVERE, "FilterWriter close exception:", e);
        }
      }
      
      else if( item instanceof LineNumberReader){
        try {
          ((LineNumberReader) item).close();
        } catch (IOException e) {
          Logger.getLogger(IOCloseUtil.class.getName()).log(Level.SEVERE, "LineNumberReader close exception:", e);
        }
      }
      
      else if( item instanceof ObjectInputStream){
        try {
          ((ObjectInputStream) item).close();
        } catch (IOException e) {
          Logger.getLogger(IOCloseUtil.class.getName()).log(Level.SEVERE, "ObjectInputStream close exception:", e);
        }
      }
      
      else if( item instanceof ObjectOutputStream){
        try {
          ((ObjectOutputStream) item).close();
        } catch (IOException e) {
          Logger.getLogger(IOCloseUtil.class.getName()).log(Level.SEVERE, "ObjectOutputStream close exception:", e);
        }
      }
      
      else if( item instanceof OutputStreamWriter){
        try {
          ((OutputStreamWriter) item).close();
        } catch (IOException e) {
      Logger.getLogger(IOCloseUtil.class.getName()).log(Level.SEVERE, "OutputStreamWriter close exception:",e);
        }
      }
      
      else if( item instanceof PipedInputStream){
        try {
          ((PipedInputStream) item).close();
        } catch (IOException e) {
      Logger.getLogger(IOCloseUtil.class.getName()).log(Level.SEVERE, "PipedInputStream close exception:",e);
        }
      }
      
      else if( item instanceof PipedOutputStream){
        try {
          ((PipedOutputStream) item).close();
        } catch (IOException e) {
      Logger.getLogger(IOCloseUtil.class.getName()).log(Level.SEVERE, "PipedOutputStream close exception",e);
        }
      }
      
      else if( item instanceof PipedReader){
        try {
          ((PipedReader) item).close();
        } catch (IOException e) {
      Logger.getLogger(IOCloseUtil.class.getName()).log(Level.SEVERE, "PipedReader close exception:",e);
        }
      }
      
      else if( item instanceof PrintStream){
        ((PrintStream) item).close();
      }
      
      else if( item instanceof PrintWriter){
        ((PrintWriter) item).close();
      }
      
      else if( item instanceof PushbackInputStream){
        try {
          ((PushbackInputStream) item).close();
        } catch (IOException e) {
      Logger.getLogger(IOCloseUtil.class.getName()).log(Level.SEVERE, "PushbackInputStream close exception:",e);
        }
      }
      
      else if( item instanceof PushbackReader){
        try {
          ((PushbackReader) item).close();
        } catch (IOException e) {
      Logger.getLogger(IOCloseUtil.class.getName()).log(Level.SEVERE, "PushbackReader close exception:",e);
        }
      }
      
      else if( item instanceof RandomAccessFile){
        try {
          ((RandomAccessFile) item).close();
        } catch (IOException e) {
      Logger.getLogger(IOCloseUtil.class.getName()).log(Level.SEVERE, "RandomAccessFile close exception:",e);
        }
      }
      
      else if( item instanceof Reader){
        try {
          ((Reader) item).close();
        } catch (IOException e) {
      Logger.getLogger(IOCloseUtil.class.getName()).log(Level.SEVERE, "Reader close exception:",e);
        }
      }
      
      else if( item instanceof SequenceInputStream){
        try {
          ((SequenceInputStream) item).close();
        } catch (IOException e) {
      Logger.getLogger(IOCloseUtil.class.getName()).log(Level.SEVERE, "SequenceInputStream close exception:",e);
        }
      }
      
      else if( item instanceof StringBufferInputStream){
        try {
          ((StringBufferInputStream) item).close();
        } catch (IOException e) {
      Logger.getLogger(IOCloseUtil.class.getName()).log(Level.SEVERE, "StringBufferInputStream close exception:",e);
        }
      }
      
      else if( item instanceof StringReader){
        ((StringReader) item).close();
      }
      
      else if( item instanceof StringWriter){
        try {
          ((StringWriter) item).close();
        } catch (IOException e) {
      Logger.getLogger(IOCloseUtil.class.getName()).log(Level.SEVERE, "StringWriter close exception:",e);
        }
      }
      
      else if( item instanceof StringWriter){
        try {
          ((StringWriter) item).close();
        } catch (IOException e) {
      Logger.getLogger(IOCloseUtil.class.getName()).log(Level.SEVERE, "StringWriter close exception:",e);
        }
      }
      
      else;
      
      item = null;
    }
  }   
  
}
