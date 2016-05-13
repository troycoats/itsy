package com.itsy.dataaccess.user;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.itsy.dataaccess.base.BaseVO;
import com.itsy.util.TextUtil;

public class UserVO extends BaseVO {
	// ------------------------------------------- USER_PROFILE
	private int userId;
	private String firstName;
	private String middleName;
	private String lastName;
	private String addr1;
	private String addr2;
	private String addrCity;
	private String addrState;
	private String addrZip;
	private String phone;
	private String emailAddress;
	private Date updateTimestamp;
	
	public int getUserId() {
		return userId;
	}
	public String getUserIdAsString() {
		return Integer.toString(userId);
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getMiddleName() {
		return middleName;
	}
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getFullName() {
		return getFirstName() + " " + getLastName();
	}
	public String getAddr1() {
		return addr1;
	}
	public void setAddr1(String addr1) {
		this.addr1 = addr1;
	}
	public String getAddr2() {
		return addr2;
	}
	public void setAddr2(String addr2) {
		this.addr2 = addr2;
	}
	public String getAddrCity() {
		return addrCity;
	}
	public void setAddrCity(String addrCity) {
		this.addrCity = addrCity;
	}
	public String getAddrState() {
		return addrState;
	}
	public void setAddrState(String addrState) {
		this.addrState = addrState;
	}
	public String getAddrZip() {
		return addrZip;
	}
	public void setAddrZip(String addrZip) {
		this.addrZip = addrZip;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	public Date getUpdateTimestamp() {
		return updateTimestamp;
	}
	public void setUpdateTimestamp(Date updateTimestamp) {
		this.updateTimestamp = updateTimestamp;
	}
	public String getMailingAddress() {
		StringBuilder sb = new StringBuilder();
		if (!TextUtil.isEmpty(getAddr1()))
			sb.append(getAddr1() + "<br>");
		if (!TextUtil.isEmpty(getAddr2())) 
			sb.append(getAddr2() + "<br>");
		sb.append(TextUtil.print(getAddrCity()) + " " + TextUtil.print(getAddrState()) + " " + TextUtil.print(getAddrZip()) + "<br>");
		return sb.toString();
	}
	// ------------------------------------------- USER_PROFILE
	
	// ------------------------------------------- USER_INFO
	private String username;
	private boolean isActive;
	private Date loginDate;
	private Date lastLoginDate;
	private Date datePassChanged;
	private int loginAttempts;
	private String password;
	
	public Date getDatePassChanged() {
		return datePassChanged;
	}

	public void setDatePassChanged(Date datePassChanged) {
		this.datePassChanged = datePassChanged;
	}

	public boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}

	public int getLoginAttempts() {
		return loginAttempts;
	}

	public void setLoginAttempts(int loginAttempts) {
		this.loginAttempts = loginAttempts;
	}

	public Date getLoginDate() {
		return loginDate;
	}

	public void setLoginDate(Date loginDate) {
		this.loginDate = loginDate;
	}

	public Date getLastLoginDate() {
		return lastLoginDate;
	}

	public Date getLastLoginDateOnlyNoTime() {
		try {
			DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			return formatter.parse(formatter.format(lastLoginDate));
		} catch (Exception e) {
			return lastLoginDate;
		}
	}

	public void setLastLoginDate(Date lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	// ------------------------------------------- USER_INFO
	
	// ------------------------------------------- ROLES
	private List<Integer> roles = null;
	public List<Integer> getRoles() {
		return roles;
	}
	public void setRoles(List<Integer> roles) {
		this.roles = roles;
	}
	public void setRoles(HashMap<Integer, String> personnelRoles) {
		List<Integer> roles = new ArrayList<Integer>();
		if (personnelRoles != null && personnelRoles.size() > 0) {
			for (Map.Entry<Integer, String> entry : personnelRoles.entrySet()) {
	            roles.add(entry.getKey());
	        }
		}
		this.roles = roles;
		
		roles = null;
	}
	// ------------------------------------------- ROLES
}
