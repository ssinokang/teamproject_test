package ksmart.mybatis.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {
	
	@GetMapping("/admin0123123")
	public String getAdmin() {
		return "admin/admin01";
	}
	
	public String asdf() {
		return "";
	}
}