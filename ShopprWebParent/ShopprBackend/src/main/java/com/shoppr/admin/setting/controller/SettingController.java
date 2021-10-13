package com.shoppr.admin.setting.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shoppr.admin.setting.CurrencyRepository;
import com.shoppr.admin.setting.GeneralSettingBag;
import com.shoppr.admin.setting.SettingService;
import com.shoppr.common.entity.Currency;
import com.shoppr.common.entity.setting.Setting;



@Controller
public class SettingController {

	@Autowired private SettingService service;
	
	@Autowired private CurrencyRepository currencyRepo;
	
	@GetMapping("/settings")
	public String listAll(Model model) {
		List<Setting> listSettings = service.listAllSettings();
		List<Currency> listCurrencies = currencyRepo.findAllByOrderByNameAsc();
		
		for (Setting setting : listSettings) {
			model.addAttribute(setting.getKey(), setting.getValue());
		}
		
		model.addAttribute("listCurrencies", listCurrencies);
		
		return "settings/settings";
	}
	
	@PostMapping("/settings/save_general")
	public String saveGeneralSettings(
			HttpServletRequest request, RedirectAttributes ra) throws IOException {
		GeneralSettingBag settingBag = service.getGeneralSettings();
		
		saveCurrencySymbol(request, settingBag);
		
		updateSettingValuesFromForm(request, settingBag.list());
		
		ra.addFlashAttribute("message", "General settings have been saved.");
		
		return "redirect:/settings";
	}
	
	
	private void saveCurrencySymbol(HttpServletRequest request, GeneralSettingBag settingBag) {
		Integer currencyId = Integer.parseInt(request.getParameter("CURRENCY_ID"));
		Optional<Currency> findByIdResult = currencyRepo.findById(currencyId);
		
		if (findByIdResult.isPresent()) {
			Currency currency = findByIdResult.get();
			settingBag.updateCurrencySymbol(currency.getSymbol());
		}
	}
	
	private void updateSettingValuesFromForm(HttpServletRequest request, List<Setting> listSettings) {
		for (Setting setting : listSettings) {
			String value = request.getParameter(setting.getKey());
			System.out.println(value);
			if (value != null) {
				setting.setValue(value);
			}
		}
		
		service.saveAll(listSettings);
	}
	
	@PostMapping("/settings/save_mail_server")
	public String saveMailServerSetttings(HttpServletRequest request, RedirectAttributes ra) {
		List<Setting> mailServerSettings = service.getMailServerSettings();
		updateSettingValuesFromForm(request, mailServerSettings);
		
		ra.addFlashAttribute("message", "Mail server settings have been saved");
		
		return "redirect:/settings#mailServer";
	}
	
	@PostMapping("/settings/save_mail_templates")
	public String saveMailTemplateSetttings(HttpServletRequest request, RedirectAttributes ra) {
		List<Setting> mailTemplateSettings = service.getMailTemplateSettings();
		updateSettingValuesFromForm(request, mailTemplateSettings);
		
		ra.addFlashAttribute("message", "Mail template settings have been saved");
		
		return "redirect:/settings#mailTemplates";
	}
	
	@PostMapping("/settings/save_payment")
	public String savePaymentSetttings(HttpServletRequest request, RedirectAttributes ra) {
		List<Setting> paymentSettings = service.getPaymentSettings();
		updateSettingValuesFromForm(request, paymentSettings);
		
		ra.addFlashAttribute("message", "Payment settings have been saved");
		
		return "redirect:/settings#payment";
	}		
}