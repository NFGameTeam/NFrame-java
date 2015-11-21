/**
 * 
 */
package nframe;

/**
 * @author lvsheng.huang
 * 框架核心,逻辑类
 */

import java.util.List;
import java.util.ArrayList;

public class NFLogicClass extends NFILogicClass{
	
	private NFIPropertyManager propertyManager;
	//private NFIRecordManager recordManager;

	private NFILogicClass parentClass;
	private String typeName;
	private String className;
	private String instancePath;

	private List<String> configNameList = new ArrayList<String>();
	private List<String> fileList = new ArrayList<String>();

	@Override
	public NFIPropertyManager getPropertyManager(){
		return propertyManager;
	}
	
	//public NFIRecordManager GetRecordManager();
	@Override
	public void setParentClass(NFILogicClass parentClass){
		this.parentClass = parentClass;
	}
	
	@Override
	public NFILogicClass getParentClass(){
		return this.parentClass;
	}
	
	@Override
	public void setTypeName(String type){
		this.typeName = type;
	}
	
	@Override
	public String getTypeName(){
		return this.typeName;
	}
	
	@Override
	public String getClassName(){
		return this.className;
	}
	
	@Override
	public boolean addConfigName(String configName){
		this.configNameList.add(configName);
		return true;
	}
	
	@Override
	public List<String> getConfigNameList(){
		return this.configNameList;
	}
	
	@Override
	public void clearConfigNameList(){
		this.configNameList.clear();
	}
	
	@Override
	public void setInstancePath(String instancePath){
		this.instancePath = instancePath;
	}
	
	@Override
	public String getInstancePath(){
		return this.instancePath;
	}
	@Override
	public boolean addFile(String file){
		return this.fileList.add(file);
	}
	@Override
	public List<String> getFileList(){
		return this.fileList;
	}
	
	@Override
	public void init(){
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterInit(){
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeShut(){
		// TODO Auto-generated method stub
		
	}

	@Override
	public void shut(){
		// TODO Auto-generated method stub
		
	}

	@Override
	public void execute(){
		// TODO Auto-generated method stub
		
	}
}
