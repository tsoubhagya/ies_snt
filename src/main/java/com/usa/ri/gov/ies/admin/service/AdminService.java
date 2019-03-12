package com.usa.ri.gov.ies.admin.service;

import java.util.List;

import com.usa.ri.gov.ies.admin.model.AppAccount;

public interface AdminService {

	public boolean registerAccount(AppAccount appAccount);

	public String findByEmail(String emailId);
	
	public List<AppAccount> findAllAppAccounts();
}
