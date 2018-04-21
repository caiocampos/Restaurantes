package tst.campos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import tst.campos.service.EntityInfoService;
import tst.campos.util.BadRequestException;

@RestController("infoController")
public class InfoController {

	@Autowired
	EntityInfoService entityInfoService;

	@RequestMapping("/user")
	@ResponseBody
	public Object user() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return authentication == null ? null : authentication.getPrincipal();
	}

	@RequestMapping("/entity/list")
	@ResponseBody
	public ResponseEntity<Object[]> findAll() throws BadRequestException {
		return new ResponseEntity<>(entityInfoService.listEntityInfo().toArray(), HttpStatus.OK);
	}
}
