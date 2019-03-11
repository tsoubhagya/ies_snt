package com.usa.ri.gov.ies.admin.service;

import java.io.BufferedReader;
import java.io.FileReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.usa.ri.gov.ies.admin.entity.AppAccountEntity;
import com.usa.ri.gov.ies.admin.model.AppAccount;
import com.usa.ri.gov.ies.admin.repository.AppAccountRepository;
import com.usa.ri.gov.ies.constants.AppConstants;
import com.usa.ri.gov.ies.properties.AppProperties;
import com.usa.ri.gov.ies.util.EmailUtils;
import com.usa.ri.gov.ies.util.PasswordUtils;

/**
 * This class is used to handler all business operations related to admin module
 * 
 * @author admin
 *
 */
@Service("adminService")
public class AdminServiceImpl implements AdminService {

	private static Logger logger = LoggerFactory.getLogger(AdminServiceImpl.class);

	@Autowired(required = true)
	private AppAccountRepository appAccRepository;

	@Autowired(required = true)
	private EmailUtils emailUtils;

	@Autowired(required = true)
	private AppProperties appProperties;

	/**
	 * This method is used to Register cw/admin
	 */
	@Override
	public boolean registerAccount(AppAccount appAcc) {
		logger.debug("User Registration started");

		// Convert model data to Entity data
		AppAccountEntity entity = new AppAccountEntity();
		BeanUtils.copyProperties(appAcc, entity);

		// Encrypt Password
		String encryptedPwd = PasswordUtils.encrypt(appAcc.getPassword());

		// Set Encrypted password to Entity obj
		entity.setPassword(encryptedPwd);

		// set Status as Active
		entity.setActiveSw(AppConstants.ACTIVE_SW);

		// Call Repository method
		entity = appAccRepository.save(entity);

		// sending Registration Email
		try {
			String mailSub = appProperties.getProperties().get(AppConstants.REG_EMAIL_SUBJECT);
			String mailBody = getRegEmailBodyContent(appAcc);
			emailUtils.sendEmail(entity.getEmail(), mailSub, mailBody);
		} catch (Exception e) {
			logger.error("User Registration failed : " + e.getMessage());
		}
		logger.debug("User Registration completed");
		logger.info("AdminServiceImpl::registerAccount() completed");
		return (entity.getAccId() > 0) ? true : false;
	}

	/**
	 * This method is used to reg email body from a file
	 * 
	 * @param accModel
	 * @return String
	 * @throws Exception
	 */
	public String getRegEmailBodyContent(AppAccount accModel) throws Exception {
		logger.debug("Reading Reg Email content started");
		String fileName = appProperties.getProperties().get(AppConstants.REG_EMAIL_FILE_NAME);
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		StringBuffer body = new StringBuffer();
		String line = br.readLine();
		while (line != null) {
			if (line != null && !"".equals(line) && !"<br/>".equals(line)) {
				// process
				if (line.contains("USER_NAME")) {
					line = line.replace("USER_NAME", accModel.getFirstName() + " " + accModel.getLastName());
				}
				if (line.contains("APP_URL")) {
					line = line.replace("APP_URL", "<a href='http://localhost:7070/IES/'>IES URL</a>");
				}
				if (line.contains("APP_USER_EMAIL")) {
					line = line.replace("APP_USER_EMAIL", accModel.getEmail());
				}
				if (line.contains("APP_USER_PWD")) {
					line = line.replace("APP_USER_PWD", accModel.getPassword());
				}
				// Adding processed line to SB body
				body.append(line);
			}
			// read next line
			line = br.readLine();
		}
		// closing br
		br.close();
		logger.debug("Reading Reg Email content Ended");
		logger.info("Reg Email body parsing completed");
		return body.toString();
	}

	/**
	 * This method is used to check unique email
	 */
	@Override
	public String findByEmail(String emailId) {
		AppAccountEntity entity = appAccRepository.findByEmail(emailId);
		return (entity == null) ? "Unique" : "Duplicate";
	}

}
