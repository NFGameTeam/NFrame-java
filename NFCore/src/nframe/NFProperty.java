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
	
	private NFGUID oid;
	private String name;
	private NFIData var;
	private NFIData oldVar;
	private NFIData newVar;
	private List<NFIPropertyHandler> callbacks;
	
	public NFProperty(NFGUID oid, String name){
		this.oid = oid;
		this.name = name;
		this.var = new NFData();
		this.oldVar = new NFData();
		this.newVar = new NFData();
	}
	
	public NFProperty(NFGUID oid, String name, long var){
		this.oid = oid;
		this.name = name;
		this.var = new NFData(var);
		this.oldVar = new NFData(var);
		this.newVar = new NFData(var);
	}
	
	public NFProperty(NFGUID oid, String name, double var){
		this.oid = oid;
		this.name = name;
		this.var = new NFData(var);
		this.oldVar = new NFData(var);
		this.newVar = new NFData(var);
	}
	
	public NFProperty(NFGUID oid, String name, String var){
		this.oid = oid;
		this.name = name;
		this.var = new NFData(var);
		this.oldVar = new NFData(var);
		this.newVar = new NFData(var);
	}
	
	public NFProperty(NFGUID oid, String name, NFGUID var){
		this.oid = oid;
		this.name = name;
		this.var = new NFData(var);
		this.oldVar = new NFData(var);
		this.newVar = new NFData(var);
	}
	
	public NFProperty(NFGUID oid, String name, NFIData var){
		this.oid = oid;
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
		this.var = new NFData(other);
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
		if (getType() != NFIData.Type.INT || getInt() != var){
			this.oldVar.set(this.var);
			this.var.set(var);
			this.newVar.set(var);
			
			if (callbacks != null){
				for (NFIPropertyHandler cb : callbacks){
					cb.handle(oid, name, this.oldVar, this.newVar);
				}
			}
		}
	}

	@Override
	public void set(double var) {
		if (getType() != NFIData.Type.FLOAT || Double.compare(getFloat(), var) != 0){
			this.oldVar.set(this.var);
			this.var.set(var);
			this.newVar.set(var);
			
			if (callbacks != null){
				for (NFIPropertyHandler cb : callbacks){
					cb.handle(oid, name, this.oldVar, this.newVar);
				}
			}
		}
	}

	@Override
	public void set(String var) {
		assert var != null;
		if (getType() != NFIData.Type.STRING || !getString().equals(var)){
			this.oldVar.set(this.var);
			this.var.set(var);
			this.newVar.set(var);
			
			if (callbacks != null){
				for (NFIPropertyHandler cb : callbacks){
					cb.handle(oid, name, this.oldVar, this.newVar);
				}
			}
		}
	}

	@Override
	public void set(NFGUID var) {
		assert var != null;
		if (getType() != NFIData.Type.OBJECT || !getObject().equals(var)){
			this.oldVar.set(this.var);
			this.var.set(var);
			this.newVar.set(var);
			
			if (callbacks != null){
				for (NFIPropertyHandler cb : callbacks){
					cb.handle(oid, name, this.oldVar, this.newVar);
				}
			}
		}
	}

	@Override
	public void set() {
		if (getType() != NFIData.Type.NULL){
			this.oldVar.set(this.var);
			this.var.dispose();
			this.newVar.dispose();
			
			if (callbacks != null){
				for (NFIPropertyHandler cb : callbacks){
					cb.handle(oid, name, this.oldVar, this.newVar);
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
