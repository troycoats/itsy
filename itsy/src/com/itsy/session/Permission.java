package com.itsy.session;

public class Permission {
	private boolean read;
	private boolean update;
	private boolean access;
	private boolean insert;
	private boolean delete; 
	
	public Permission(boolean r, boolean u, boolean a, boolean i, boolean d) {
		super();
		
		read = r;
		update = u;
		access = a;
		insert = i;
		delete = d;	
	}

	public boolean hasAccess() {
		return access;
	}

	public boolean isReadOnly() {
		return read && (!update && !insert && !delete);
	}

	public void setAccess(boolean access) {
		this.access = access;
	}

	public boolean hasDelete() {
		return delete;
	}

	public void setDelete(boolean delete) {
		this.delete = delete;
	}

	public boolean hasInsert() {
		return insert;
	}

	public void setInsert(boolean insert) {
		this.insert = insert;
	}

	public boolean hasRead() {
		return read;
	}

	public void setRead(boolean read) {
		this.read = read;
	}

	public boolean hasUpdate() {
		return update;
	}

	public void setUpdate(boolean update) {
		this.update = update;
	}	
}
