package com.shoppr.shoppingcart;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.shoppr.Utility;
import com.shoppr.address.AddressService;
import com.shoppr.common.entity.Address;
import com.shoppr.common.entity.CartItem;
import com.shoppr.common.entity.Customer;
import com.shoppr.common.entity.ShippingRate;
import com.shoppr.customer.CustomerService;
import com.shoppr.shipping.ShippingRateService;

@Controller
public class ShoppingCartController {
	@Autowired private CustomerService customerService;
	@Autowired private ShoppingCartService cartService;
	@Autowired private AddressService addressService;
	@Autowired private ShippingRateService shipService;
	
	@GetMapping("/cart")
	public String viewCart(Model model, HttpServletRequest request) {
		Customer customer = getAuthenticatedCustomer(request);
		List<CartItem> cartItems = cartService.listCartItems(customer);
		
		float estimatedTotal = 0.0F;
		
		for (CartItem item : cartItems) {
			estimatedTotal += item.getSubtotal();
		}
		
		Address defaultAddress = addressService.getDefaultAddress(customer);
		ShippingRate shippingRate = null;
		boolean usePrimaryAddressAsDefault = false;
		
		if (defaultAddress != null) {
			shippingRate = shipService.getShippingRateForAddress(defaultAddress);
		} else {
			usePrimaryAddressAsDefault = true;
			shippingRate = shipService.getShippingRateForCustomer(customer);
		}
		
		model.addAttribute("usePrimaryAddressAsDefault", usePrimaryAddressAsDefault);
		model.addAttribute("shippingSupported", shippingRate != null);
		model.addAttribute("cartItems", cartItems);
		model.addAttribute("estimatedTotal", estimatedTotal);
		
		return "cart/shopping_cart";
	}
	
	private Customer getAuthenticatedCustomer(HttpServletRequest request) {
		String email = Utility.getEmailOfAuthenticatedCustomer(request);				
		return customerService.getCustomerByEmail(email);
	}	
}
