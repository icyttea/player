package com.example.slidingmenu;
import java.util.ArrayList;
import java.util.List;

public class Node {
private Node parent = null; // ���ڵ�
private List<Node> childrens = new ArrayList<Node>();//�ӽڵ�
private String name;//�ڵ���ʾ����
private String cameraID;//�ڵ���ʾֵ
private int port;//�˿�ֵ
private int icon = -1; //icon(R.drawable��id)
private boolean isChecked = false; //�Ƿ�ѡ��
private boolean isExpand = true;//�Ƿ�����չ״̬
private boolean hasCheckBox = true;//�Ƿ��и�ѡ��
private String parentId = null;
private String curId = null;
private boolean isVisiable = true;

	/**
	 * ���ýڵ�ֵ
	 * 
	 * @param parentId
	 *            TODO
	 * @param curId
	 *            TODO
	 */
public Node(String name,String cameraID, String parentId,String curId, int iconId, int port) {
	// TODO Auto-generated constructor stub
	this.name = name;
	this.cameraID = cameraID;
	this.parentId = parentId;
	this.icon = iconId;
	this.curId = curId;
	this.port = port;
}
/**
 * �õ����ڵ�
 * @return
 *
 */
public Node getParent() {
	return parent;
}
/**
 * ���ø��ڵ�
 * @param parent
 *
 */
public void setParent(Node parent) {
	this.parent = parent;
}
/**
 * �õ��ӽڵ�
 * @return
 *
 */
public List<Node> getChildrens() {
	return childrens;
}
/**
 * �Ƿ���ڵ�
 * @return
 *
 */
public boolean isRoot(){
	return parent ==null?true:false;
}
/**
 * �Ƿ�����ͼ��
 * @return
 *
 */
public int getIcon() {
	return icon;
}
/**
 * ����ͼ��
 * @param icon
 *
 */
public void setIcon(int icon) {
	this.icon = icon;
}

/**
 * �Ƿ���չ��״̬
 * @return
 *
 */
public boolean isExplaned() {
	return isExpand;
}
/**
 * ����չ��״̬
 * @param isExplaned
 *
 */
public void setExplaned(boolean isExplaned) {
	this.isExpand = isExplaned;
}

/**
 * �õ��˿�ֵ
 * @return
 *
 */
public int getPort(){
	return port;
}
/**
 * ���ö˿�ֵ
 * @return
 *
 */
public void setPort(int port){
	this.port = port;
}
/**
 * �õ��ڵ����
 * @return
 *
 */
public String getName() {
	return name;
}
/**
 * ���ýڵ����
 * @param title
 *
 */
public void setName(String name) {
	this.name = name;
}
/**
 * �õ��ڵ�ֵ
 * @return
 *
 */
public String getCameraID() {
	return cameraID;
}
/**
 * ���ýڵ�ֵ
 * @param value
 *
 */
public void setCameraID(String cameraID) {
	this.cameraID = cameraID;
}
/**
 * ����һ���ӽڵ�
 * @param node
 *
 */
public void addNode(Node node){
	if(!childrens.contains(node)){
		childrens.add(node);
	}
}
/**
 * �Ƴ�һ���ӽڵ�
 * @param node
 *
 */
public void removeNode(Node node){
	if(childrens.contains(node))
		childrens.remove(node);
}
/**
 * �Ƴ�ָ��λ�õ��ӽڵ�
 * @param location
 *
 */
public void removeNode(int location){
	childrens.remove(location);
}
/**
 * ��������ӽڵ�
 *
 */
public void clears(){
	childrens.clear();
}
/**
 * �жϸ����Ľڵ��Ƿ�ǰ�ڵ�ĸ��ڵ�
 * @param node
 * @return
 *
 */
public boolean isParent(Node node){
	if(parent == null)return false;
	if(parent.equals(node))return true;
	return parent.isParent(node);
}
/**
 * �ݹ��ȡ��ǰ�ڵ㼶��
 * @return
 *
 */
public int getLevel(){
	return parent ==null?0:parent.getLevel()+1;
}
/**
 * ���ڵ��Ƿ����۵���״̬
 * @return
 *
 */
public boolean isParentCollapsed(){
	if(parent ==null)return false;
	if(!parent.isExplaned())return true;
	return parent.isParentCollapsed();
}
/**
 * �Ƿ�Ҷ�ڵ㣨û��չ���¼��ļ��㣩
       * @return
       *
 */
public boolean isLeaf(){
       return childrens.size()<1?true:false;
}
 /**
  * �����Լ���id
 * @return
 **/
public String getCurId() {
	// TODO Auto-generated method stub
	return curId;
}
/**
 * ���صĸ�id
* @return
**/
public String getParentId() {
	// TODO Auto-generated method stub
	return parentId;
}
}
