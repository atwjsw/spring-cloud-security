package org.atwjsw.security.order;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/orders")
@Slf4j
public class OrderController {

	// private RestTemplate restTemplate = new RestTemplate();

	@PostMapping
	// public OrderInfo create(@RequestBody OrderInfo info, @AuthenticationPrincipal User user) {
	// public OrderInfo create(@RequestBody OrderInfo info, @AuthenticationPrincipal(expression = "#this.id") Long id) {
	public OrderInfo create(@RequestBody OrderInfo info, @RequestHeader String username, HttpServletRequest request) {
		// log.info("username is {}, id is {}", user.getUsername(), user.getId());
		log.info("username is {}", username);
		log.info("userInfo: {}", request.getAttribute("userInfo"));
		// PriceInfo price = restTemplate.getForObject("http://localhost:9060/prices/" + info.getProductId(),
		// PriceInfo.class);
		// log.info("price is {}", price.getPrice());
		return info;
	}

	@GetMapping("/{id}")
	public OrderInfo getUserInfo(@PathVariable Long id, @RequestHeader String username) {
		log.info("user is {}", username);
		log.info("orderId is {}", id);

		OrderInfo info = new OrderInfo();
		info.setId(id);
		info.setProductId(id * 5);
		return info;
	}

}
