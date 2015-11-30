/**
 * 
 */
package nframe;

/**
 * @author Xiong
 * 框架基础对象类
 */
public class NFObject extends NFIObject {
	
	private NFIPropertyManager propertyManager;
	private NFIRecordManager recordManager;
	
	public NFObject(NFGUID guid){
		super.setId(guid);
		this.propertyManager = new NFPropertyManager(guid);
		this.recordManager = new NFRecordManager(guid);
	}

	@Override
	public boolean hasProperty(String name){
		return propertyManager.hasProperty(name);
	}

	@Override
	public long getPropertyInt(String name){
		return propertyManager.getPropertyInt(name);
	}

	@Override
	public double getPropertyFloat(String name){
		return propertyManager.getPropertyFloat(name);
	}

	@Override
	public String getPropertyString(String name){
		return propertyManager.getPropertyString(name);
	}

	@Override
	public NFGUID getPropertyObject(String name){
		return propertyManager.getPropertyObject(name);
	}

	@Override
	public boolean setProperty(String name, long var){
		return propertyManager.setProperty(name, var);
	}

	@Override
	public boolean setProperty(String name, double var){
		return propertyManager.setProperty(name, var);
	}

	@Override
	public boolean setProperty(String name, String var){
		return propertyManager.setProperty(name, var);
	}

	@Override
	public boolean setProperty(String name, NFGUID var){
		return propertyManager.setProperty(name, var);
	}

	@Override
	public boolean setProperty(String name, NFIData var){
		return propertyManager.setProperty(name, var);
	}

	@Override
	public NFIProperty getProperty(String name){
		return propertyManager.getProperty(name);
	}

	@Override
	public NFIPropertyManager getPropertyManager(){
		return propertyManager;
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

	@Override
	public boolean hasRecord(String recordName) {
		return recordManager.getRecord(recordName) != null;
	}

	@Override
	public long getRecordInt(String recordName, int row, int column) {
		return recordManager.getRecordInt(recordName, row, column);
	}

	@Override
	public double getRecordFloat(String recordName, int row, int column) {
		return recordManager.getRecordFloat(recordName, row, column);
	}

	@Override
	public String getRecordString(String recordName, int row, int column) {
		return recordManager.getRecordString(recordName, row, column);
	}

	@Override
	public NFGUID getRecordObject(String recordName, int row, int column) {
		return recordManager.getRecordObject(recordName, row, column);
	}

	@Override
	public long getRecordInt(String recordName, int row, String colTag) {
		return recordManager.getRecordInt(recordName, row, colTag);
	}

	@Override
	public double getRecordFloat(String recordName, int row, String colTag) {
		return recordManager.getRecordFloat(recordName, row, colTag);
	}

	@Override
	public String getRecordString(String recordName, int row, String colTag) {
		return recordManager.getRecordString(recordName, row, colTag);
	}

	@Override
	public NFGUID getRecordObject(String recordName, int row, String colTag) {
		return recordManager.getRecordObject(recordName, row, colTag);
	}

	@Override
	public boolean setRecord(String recordName, int row, int column, long var) {
		return recordManager.setRecord(recordName, row, column, var);
	}

	@Override
	public boolean setRecord(String recordName, int row, int column, double var) {
		return recordManager.setRecord(recordName, row, column, var);
	}

	@Override
	public boolean setRecord(String recordName, int row, int column, String var) {
		return recordManager.setRecord(recordName, row, column, var);
	}

	@Override
	public boolean setRecord(String recordName, int row, int column, NFGUID var) {
		return recordManager.setRecord(recordName, row, column, var);
	}

	@Override
	public boolean setRecord(String recordName, int row, String colTag, long var) {
		return recordManager.setRecord(recordName, row, colTag, var);
	}

	@Override
	public boolean setRecord(String recordName, int row, String colTag, double var) {
		return recordManager.setRecord(recordName, row, colTag, var);
	}

	@Override
	public boolean setRecord(String recordName, int row, String colTag, String var) {
		return recordManager.setRecord(recordName, row, colTag, var);
	}

	@Override
	public boolean setRecord(String recordName, int row, String colTag, NFGUID var) {
		return recordManager.setRecord(recordName, row, colTag, var);
	}

	@Override
	public NFIRecordManager getRecordManager() {
		return recordManager;
	}

}
