/**
 * 
 */
package nframe;

import java.util.ArrayList;
import java.util.List;

import nframe.NFIDataList.ValueType;

/**
 * @author Xiong
 * 属性类
 */
public class NFProperty extends NFIProperty {
	
	private NFIdent self;
	private String name;
	private NFIDataList value;
	private NFIDataList oldVar;
	private NFIDataList newVar;
	private List<NFIPropertyHandler> callbacks;
	
	public NFProperty(NFIdent self, String name, NFIDataList value){
		assert value.size() == 1;
		this.self = self;
		this.name = name;
		this.value = new NFDataList(value);
		this.oldVar = new NFDataList(value);
		this.newVar = new NFDataList(value);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public ValueType getType() {
		return value.getType(0);
	}

	@Override
	public void set(NFIDataList value) {
		this.value = value;
	}

	@Override
	public NFIDataList get() {
		return value;
	}

	@Override
	public long getInt() {
		return value.getInt(0);
	}

	@Override
	public double getFloat() {
		return value.getFloat(0);
	}

	@Override
	public String getString() {
		return value.getString(0);
	}

	@Override
	public NFIdent getObject() {
		return value.getObject(0);
	}

	@Override
	public void set(long value) {
		if (getType() != NFIDataList.ValueType.INT || getInt() != value){
			this.oldVar.set(this.value);
			this.value.set(0, value);
			this.newVar.set(0, value);
			
			if (null != callbacks){
				for (NFIPropertyHandler cb : callbacks){
					cb.handle(self, name, this.oldVar, this.newVar);
				}
			}
		}
	}

	@Override
	public void set(double value) {
		if (getType() != NFIDataList.ValueType.FLOAT || Double.compare(getFloat(), value) != 0){
			this.oldVar.set(this.value);
			this.value.set(0, value);
			this.newVar.set(0, value);
			
			if (null != callbacks){
				for (NFIPropertyHandler cb : callbacks){
					cb.handle(self, name, this.oldVar, this.newVar);
				}
			}
		}
	}

	@Override
	public void set(String value) {
		if (getType() != NFIDataList.ValueType.STRING || !getString().equals(value)){
			this.oldVar.set(this.value);
			this.value.set(0, value);
			this.newVar.set(0, value);
			
			if (null != callbacks){
				for (NFIPropertyHandler cb : callbacks){
					cb.handle(self, name, this.oldVar, this.newVar);
				}
			}
		}
	}

	@Override
	public void set(NFIdent value) {
		if (getType() != NFIDataList.ValueType.OBJECT || !getObject().equals(value)){
			this.oldVar.set(this.value);
			this.value.set(0, value);
			this.newVar.set(0, value);
			
			if (null != callbacks){
				for (NFIPropertyHandler cb : callbacks){
					cb.handle(self, name, this.oldVar, this.newVar);
				}
			}
		}
	}

	@Override
	public void addCallback(NFIPropertyHandler cb) {
		if (null == callbacks){
			callbacks = new ArrayList<NFIPropertyHandler>();
		}
		callbacks.add(cb);
	}
	
}
