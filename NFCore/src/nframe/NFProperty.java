/**
 * 
 */
package nframe;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Xiong
 * 属性类
 */
public class NFProperty extends NFIProperty {
	
	private NFIdent oid;
	private String name;
	private NFIData value;
	private NFIData oldVar;
	private NFIData newVar;
	private List<NFIPropertyHandler> callbacks;
	
	public NFProperty(NFIdent oid, String name, long var){
		this.oid = oid;
		this.name = name;
		this.value = new NFData(var);
		this.oldVar = new NFData(var);
		this.newVar = new NFData(var);
	}
	
	public NFProperty(NFIdent oid, String name, double var){
		this.oid = oid;
		this.name = name;
		this.value = new NFData(var);
		this.oldVar = new NFData(var);
		this.newVar = new NFData(var);
	}
	
	public NFProperty(NFIdent oid, String name, String var){
		this.oid = oid;
		this.name = name;
		this.value = new NFData(var);
		this.oldVar = new NFData(var);
		this.newVar = new NFData(var);
	}
	
	public NFProperty(NFIdent oid, String name, NFIdent var){
		this.oid = oid;
		this.name = name;
		this.value = new NFData(var);
		this.oldVar = new NFData(var);
		this.newVar = new NFData(var);
	}
	
	public NFProperty(NFIdent oid, String name, NFIData var){
		this.oid = oid;
		this.name = name;
		this.value = new NFData(var);
		this.oldVar = new NFData(var);
		this.newVar = new NFData(var);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public NFIData.Type getType() {
		return value.getType();
	}

	@Override
	public void set(NFIData value) {
		this.value = new NFData(value);
	}

	@Override
	public NFIData get() {
		return value;
	}

	@Override
	public long getInt() {
		return value.getInt();
	}

	@Override
	public double getFloat() {
		return value.getFloat();
	}

	@Override
	public String getString() {
		return value.getString();
	}

	@Override
	public NFIdent getObject() {
		return value.getObject();
	}

	@Override
	public void set(long value) {
		if (getType() != NFIData.Type.INT || getInt() != value){
			this.oldVar.set(this.value);
			this.value.set(value);
			this.newVar.set(value);
			
			if (null != callbacks){
				for (NFIPropertyHandler cb : callbacks){
					cb.handle(oid, name, this.oldVar, this.newVar);
				}
			}
		}
	}

	@Override
	public void set(double value) {
		if (getType() != NFIData.Type.FLOAT || Double.compare(getFloat(), value) != 0){
			this.oldVar.set(this.value);
			this.value.set(value);
			this.newVar.set(value);
			
			if (null != callbacks){
				for (NFIPropertyHandler cb : callbacks){
					cb.handle(oid, name, this.oldVar, this.newVar);
				}
			}
		}
	}

	@Override
	public void set(String value) {
		if (getType() != NFIData.Type.STRING || !getString().equals(value)){
			this.oldVar.set(this.value);
			this.value.set(value);
			this.newVar.set(value);
			
			if (null != callbacks){
				for (NFIPropertyHandler cb : callbacks){
					cb.handle(oid, name, this.oldVar, this.newVar);
				}
			}
		}
	}

	@Override
	public void set(NFIdent value) {
		if (getType() != NFIData.Type.OBJECT || !getObject().equals(value)){
			this.oldVar.set(this.value);
			this.value.set(value);
			this.newVar.set(value);
			
			if (null != callbacks){
				for (NFIPropertyHandler cb : callbacks){
					cb.handle(oid, name, this.oldVar, this.newVar);
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
