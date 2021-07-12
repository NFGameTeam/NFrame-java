package cn.yeegro.nframe.tools.file;

import java.util.Arrays;

public class ByteUtil {

	private int size;
	private byte[] buffer;

	public byte[] getBuffer() {
		return buffer;
	}
	
	public byte[] getBuffer(int nStar,int nCount)
	{
		byte[] buf=new byte[nCount];
		
		System.arraycopy(buffer,nStar,buf,0,nCount);
		
		return buf;
	}
	
	public byte getBuffer(int nStar)
	{
		byte buf=buffer[nStar];
		
		return buf;
	}

	private final int kBufferSizeIncrease = 30;
	private final int kDefaultBufferSize = 30;

	public ByteUtil(byte[] _buffer) {
		buffer = _buffer;
		size = 0;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public ByteUtil append(byte[] buf, int length) {

		if (size + length > buffer.length) {
			buffer = Arrays.copyOf(buffer, buffer.length + kBufferSizeIncrease);
		}
		System.arraycopy(buf, 0, buffer, size, length);
		size += length;
		return this;
	}

	public void erase(int begin, int count) {
		byte[] newbuffer=new byte[buffer.length]; 
		
		//赋值0-start的数据
		System.arraycopy(buffer,0,newbuffer,0,begin);
		//赋值给新的数组
		System.arraycopy(buffer, begin+count, newbuffer, begin, buffer.length-begin-count);
		
		buffer=newbuffer;
		
		size -= count;
	}

	public void clear() {
		buffer = new byte[kDefaultBufferSize];
		size = 0;
	}
}
