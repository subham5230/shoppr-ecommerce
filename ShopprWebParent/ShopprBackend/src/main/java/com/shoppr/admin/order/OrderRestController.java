package com.shoppr.admin.order;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderRestController {

	@Autowired
	private OrderService service;

	@GetMapping("/orders_shipper/update/{id}/{status}")
	public void updateOrderStatus(@PathVariable("id") Integer orderId, @PathVariable("status") String status, HttpServletResponse response) throws IOException {
		service.updateStatus(orderId, status);
		response.sendRedirect("/admin/orders");
	}
}

class Response {
	private Integer orderId;
	private String status;

	public Response(Integer orderId, String status) {
		this.orderId = orderId;
		this.status = status;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
