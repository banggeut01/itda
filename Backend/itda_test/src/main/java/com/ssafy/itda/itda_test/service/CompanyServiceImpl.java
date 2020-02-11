package com.ssafy.itda.itda_test.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.itda.itda_test.dao.CompanyDao;
import com.ssafy.itda.itda_test.model.Company;

@Service
public class CompanyServiceImpl implements ICompanyService {

	@Autowired
	private CompanyDao companyDao;
	
	@Override
	public void createCompany(Company model) {
		companyDao.createCompany(model);
	}
	
	@Override
	public void deleteCompany(String cid) {
		companyDao.deleteCompany(cid);
	}
	
	@Override
	public void updateCompany(Company model) {
		companyDao.updateCompany(model);
	}

	@Override
	public List<Company> getAllCompanys() {
		return companyDao.getAllCompanys();
	}
}
