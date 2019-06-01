package br.campos.restaurantes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.campos.restaurantes.service.EntityInfoService;

/**
 * Componente que retorna as informações
 *
 * @author Caio
 */
@RestController("infoController")
public class InfoController {

	/**
	 * Serviço de controle de informações de Entidades
	 */
	@Autowired
	EntityInfoService entityInfoService;

	/**
	 * Retorna o Usuário que está acessando o Sistema
	 * @return Usuário que está acessando o Sistema
	 */
	@RequestMapping("/user")
	@ResponseBody
	public Object user() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return authentication == null ? null : authentication.getPrincipal();
	}

	/**
	 * Retorna as Informações das Entidades do Sistema
	 * @return Lista com Informações de Todas as Entidades do Sistema
	 */
	@RequestMapping("/entity/list")
	@ResponseBody
	public ResponseEntity<Object[]> entityList() {
		return new ResponseEntity<>(entityInfoService.listEntityInfo().toArray(), HttpStatus.OK);
	}
}
