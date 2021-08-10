package br.campos.restaurantes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

import br.campos.restaurantes.service.CRUDService;
import br.campos.restaurantes.service.model.CRUDRequest;
import br.campos.restaurantes.util.BadRequestException;

/**
 * Componente de controle de Dados, define métodos de salvar, apagar e listar
 * entidades
 *
 * @author Caio
 */
@CrossOrigin
@RestController("crudController")
public class CRUDController {

	/**
	 * Helper de controle de Dados
	 */
	@Autowired
	private CRUDService crudService;

	/**
	 * Busca um registro pelo Id
	 *
	 * @param req requisição enviada ao sistema
	 * @return Registro encontrado no banco
	 * @throws br.campos.restaurantes.util.BadRequestException
	 */
	@RequestMapping(method = RequestMethod.POST, path = "/findone")
	public @ResponseBody
	ResponseEntity<Object> findOne(@RequestBody CRUDRequest req) throws BadRequestException {
		return new ResponseEntity<>(crudService.findOne(req), HttpStatus.OK);
	}

	/**
	 * Busca todos os registros
	 *
	 * @param req requisição enviada ao sistema
	 * @return Registros encontrados no banco
	 * @throws br.campos.restaurantes.util.BadRequestException
	 */
	@RequestMapping(method = RequestMethod.POST, path = "/findall")
	public @ResponseBody
	ResponseEntity<Object[]> findAll(@RequestBody CRUDRequest req) throws BadRequestException {
		return new ResponseEntity<>(crudService.findAll(req).toArray(), HttpStatus.OK);
	}

	/**
	 * Busca por algum critério os registros
	 *
	 * @param req requisição enviada ao sistema
	 * @return Registros encontrados no banco
	 * @throws br.campos.restaurantes.util.BadRequestException
	 */
	@RequestMapping(method = RequestMethod.POST, path = "/findspecial")
	public @ResponseBody
	ResponseEntity<Object[]> findByCriteria(@RequestBody CRUDRequest req) throws BadRequestException {
		return new ResponseEntity<>(crudService.findSpecial(req).toArray(), HttpStatus.OK);
	}

	/**
	 * Busca pelo nome os registros
	 *
	 * @param req requisição enviada ao sistema
	 * @return Registros encontrados no banco
	 * @throws br.campos.restaurantes.util.BadRequestException
	 */
	@RequestMapping(method = RequestMethod.POST, path = "/findnome")
	public @ResponseBody
	ResponseEntity<Object[]> findByNome(@RequestBody CRUDRequest req) throws BadRequestException {
		req.special = "nomeParcialSemCaixa";
		return new ResponseEntity<>(crudService.findSpecial(req).toArray(), HttpStatus.OK);
	}

	/**
	 * Persiste um registro no banco
	 *
	 * @param req requisição enviada ao sistema
	 * @return Id do elemento persistido
	 * @throws br.campos.restaurantes.util.BadRequestException
	 */
	@RequestMapping(method = RequestMethod.POST, path = "/save")
	public @ResponseBody
	ResponseEntity<Object> save(@RequestBody CRUDRequest req) throws BadRequestException {
		return new ResponseEntity<>(crudService.save(req), HttpStatus.OK);
	}

	/**
	 * Apaga um registro pelo Id
	 *
	 * @param req requisição enviada ao sistema
	 * @return Id do elemento apagado
	 * @throws br.campos.restaurantes.util.BadRequestException
	 */
	@RequestMapping(method = RequestMethod.POST, path = "/delete")
	public @ResponseBody
	ResponseEntity<Object> delete(@RequestBody CRUDRequest req) throws BadRequestException {
		return new ResponseEntity<>(crudService.delete(req), HttpStatus.OK);
	}
}
