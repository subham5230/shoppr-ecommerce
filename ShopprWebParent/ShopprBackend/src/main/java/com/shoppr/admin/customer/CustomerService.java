package com.shoppr.admin.customer;

import java.util.List;
import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.shoppr.admin.paging.PagingAndSortingHelper;
import com.shoppr.admin.setting.country.CountryRepository;
import com.shoppr.common.entity.Country;
import com.shoppr.common.entity.Customer;
import com.shoppr.common.exception.CustomerNotFoundException;

@Service
@Transactional
public class CustomerService {

	@Autowired private CustomerRepository customerRepo;
	@Autowired private CountryRepository countryRepo;
	@Autowired private BCryptPasswordEncoder passwordEncoder;
	
	
	public final static Integer CUSTOMER_PER_PAGE = 10;
	
	public void listByPage(int pageNum, PagingAndSortingHelper helper) {
		helper.listEntities(pageNum, CUSTOMER_PER_PAGE, customerRepo);
	}
	
	public void save(Customer customerInForm) {
		
		Customer customerInDB = customerRepo.findById(customerInForm.getId()).get();
				
		if(!customerInForm.getPassword().isEmpty()) {
			String encodedPassword = passwordEncoder.encode(customerInForm.getPassword());
			customerInForm.setPassword(encodedPassword);
		}
		else {
			customerInForm.setPassword(customerInDB.getPassword());
		}
		
		
		customerInForm.setEnabled(customerInDB.isEnabled());
		customerInForm.setCreatedTime(customerInDB.getCreatedTime());
		customerInForm.setVerificationCode(customerInDB.getVerificationCode());
		customerInForm.setAuthenticationType(customerInDB.getAuthenticationType());
		customerInForm.setResetPasswordToken(customerInDB.getResetPasswordToken());
		
		customerRepo.save(customerInForm);
	}
	
	public void updateCustomerEnabledStatus(Integer id, boolean enabled) {
		customerRepo.updateEnabledStatus(id, enabled);
	}
	
	public boolean isEmailUnique(Integer id, String email) {
		Customer user = customerRepo.findByEmail(email);
		
		if(user != null && user.getId() != id) {
			//found another customer with same email
			return false;
		}
		
		return true;
	}

	public Customer get(Integer id) throws CustomerNotFoundException {
		try {
			return customerRepo.findById(id).get();
		}catch(NoSuchElementException e) {
			throw new CustomerNotFoundException("Could not find any customer with ID: " + id);
		}
	}
	
	public List<Country> listAllCountries(){
		return countryRepo.findAllByOrderByNameAsc();
	}
	
	public void delete (Integer id) throws CustomerNotFoundException {
		Long countById = customerRepo.countById(id);
		
		if(countById == null || countById == 0)
			throw new CustomerNotFoundException("Could not find any customer with ID: " + id);
		
		customerRepo.deleteById(id);
	}

}
