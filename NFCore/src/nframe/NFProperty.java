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
public class NFProperty implements NFIProperty {
	
	private NFGUID guid;
	private String name;
	private NFIData var;
	private NFIData oldVar;//待优化，看实际项目是在乎内存还是在乎CPU
	private NFIData newVar;//待优化，看实际项目是在乎内存还是在乎CPU
	private List<NFIPropertyHandler> callbacks;
	
	public NFProperty(NFGUID guid, String name){
		this.guid = guid;
		this.name = name;
		this.var = new NFData();
		this.oldVar = new NFData();
		this.newVar = new NFData();
	}
	
	public NFProperty(NFGUID guid, String name, long var){
		this.guid = guid;
		this.name = name;
		this.var = new NFData(var);
		this.oldVar = new NFData(var);
		this.newVar = new NFData(var);
	}
	
	public NFProperty(NFGUID guid, String name, double var){
		this.guid = guid;
		this.name = name;
		this.var = new NFData(var);
		this.oldVar = new NFData(var);
		this.newVar = new NFData(var);
	}
	
	public NFProperty(NFGUID guid, String name, String var){
		this.guid = guid;
		this.name = name;
		this.var = new NFData(var);
		this.oldVar = new NFData(var);
		this.newVar = new NFData(var);
	}
	
	public NFProperty(NFGUID guid, String name, NFGUID var){
		this.guid = guid;
		this.name = name;
		this.var = new NFData(var);
		this.oldVar = new NFData(var);
		this.newVar = new NFData(var);
	}
	
	public NFProperty(NFGUID guid, String name, NFIData var){
		this.guid = guid;
		this.name = name;
		this.var = new NFData(var);
		this.oldVar = new NFData(var);
		this.newVar = new NFData(var);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public NFIData.Type getType() {
		return var.getType();
	}

	@Override
	public void set(NFIData other) {
		this.oldVar = new NFData(other);
		this.var = new NFData(other);
		this.newVar = new NFData(other);
	}

	@Override
	public NFIData get() {
		return var;
	}

	@Override
	public long getInt() {
		return var.getInt();
	}

	@Override
	public double getFloat() {
		return var.getFloat();
	}

	@Override
	public String getString() {
		return var.getString();
	}

	@Override
	public NFGUID getObject() {
		return var.getObject();
	}

	@Override
	public void set(long var) {
		assert getType() == NFIData.Type.INT;
		if (getInt() != var){
			this.oldVar.set(this.var);
			this.var.set(var);
			this.newVar.set(var);
			
			if (callbacks != null){
				for (NFIPropertyHandler cb : callbacks){
					cb.handle(guid, name, this.oldVar, this.newVar);
				}
			}
		}
	}

	@Override
	public void set(double var) {
		assert getType() == NFIData.Type.FLOAT;
		if (Double.compare(getFloat(), var) != 0){
			this.oldVar.set(this.var);
			this.var.set(var);
			this.newVar.set(var);
			
			if (callbacks != null){
				for (NFIPropertyHandler cb : callbacks){
					cb.handle(guid, name, this.oldVar, this.newVar);
				}
			}
		}
	}

	@Override
	public void set(String var) {
		assert getType() == NFIData.Type.STRING;
		assert var != null;
		if (!getString().equals(var)){
			this.oldVar.set(this.var);
			this.var.set(var);
			this.newVar.set(var);
			
			if (callbacks != null){
				for (NFIPropertyHandler cb : callbacks){
					cb.handle(guid, name, this.oldVar, this.newVar);
				}
			}
		}
	}

	@Override
	public void set(NFGUID var) {
		assert getType() == NFIData.Type.OBJECT;
		assert var != null;
		if (!getObject().equals(var)){
			this.oldVar.set(this.var);
			this.var.set(var);
			this.newVar.set(var);
			
			if (callbacks != null){
				for (NFIPropertyHandler cb : callbacks){
					cb.handle(guid, name, this.oldVar, this.newVar);
				}
			}
		}
	}
	
	@Override
	public boolean isEmpty(){
		return var.isNull();
	}

	@Override
	public void addCallback(NFIPropertyHandler cb) {
		if (callbacks == null){
			callbacks = new ArrayList<NFIPropertyHandler>();
		}
		callbacks.add(cb);
	}
	
}
