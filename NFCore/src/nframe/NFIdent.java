/**
 * 
 */
package nframe;

/**
 * @author Xiong
 *
 */
public class NFIdent {
	private long head;
	private long data;
	
	public NFIdent() {
	}
	
	public NFIdent(long head, long data) {
		this.setHead(head);
		this.setData(data);
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == this){
			return true;
		}
		
		if (null == o || getClass() != o.getClass()){
			return false;
		}
		
		NFIdent rhs = (NFIdent)o;
		return this.getHead() == rhs.getHead() && this.getData() == rhs.getData();
	}
	
	/**
		[1]把某个非零常数值（一般取素数），例如17，保存在int变量result中；
		[2]对于对象中每一个关键域f（指equals方法中考虑的每一个域）：
			[2.1]boolean型，计算(f ? 0 : 1);
			[2.2]byte,char,short型，计算(int)f;
			[2.3]long型，计算(int) (f ^ (f>>>32));
			[2.4]float型，计算Float.floatToIntBits(afloat);
			[2.5]double型，计算Double.doubleToLongBits(adouble)得到一个long，再执行[2.3];
			[2.6]对象引用，递归调用它的hashCode方法;
			[2.7]数组域，对其中每个元素调用它的hashCode方法。
		[3]将上面计算得到的散列码保存到int变量c，然后执行 result=37*result+c;
		[4]返回result
	 */
	@Override
	public int hashCode() {
		int result = 17;
		result = 37 * result + (int) (head ^ (head >>> 32));
		result = 37 * result + (int) (data ^ (data >>> 32));
		return result;
	}
	
	public boolean isNull() {
		return 0 == data && 0 == head;
	}

	@Override
	public String toString() {
		return head + "-" + data;
	}
	
	public static boolean parse(String strData, NFIdent outId) {
		String[] strList = strData.split("-", 0);
		if (strList.length != 2){
			return false;
		}

		long head = 0;
		long data = 0;
		try{
			head = Long.parseLong(strList[0]);
			data = Long.parseLong(strList[1]);
		}catch (NumberFormatException e){
			return false;
		}
		
		outId.setHead(head);
		outId.setData(data);
		return true;
	}

	public long getHead() {
		return head;
	}

	public void setHead(long head) {
		this.head = head;
	}

	public long getData() {
		return data;
	}

	public void setData(long data) {
		this.data = data;
	}
}
