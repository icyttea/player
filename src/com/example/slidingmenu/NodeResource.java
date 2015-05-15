package com.example.slidingmenu;
public class NodeResource {
	protected String parentId;
	protected String curId;
	protected String name;
	protected String cameraId;
	protected int iconId;
	protected int port;

	public NodeResource(String parentId, String curId, String name,
			String cameraID, int iconId) {
		super();
		this.curId = curId;
		this.parentId = parentId;
		this.name = name;
		this.cameraId = cameraId;
		this.iconId = iconId;
		this.port = Integer.parseInt(curId);
	}

	public int getPort(){
		return port;
	}
	
	public String getParentId() {
		return parentId;
	}

	public String getName() {
		return name;
	}

	public String getCameraId() {
		return cameraId;
	}

	public int getIconId() {
		return iconId;
	}

	public String getCurId() {
		return curId;
	}

}
