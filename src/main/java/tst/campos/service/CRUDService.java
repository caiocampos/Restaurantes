package tst.campos.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import tst.campos.helper.CRUDHelper;
import tst.campos.helper.model.CRUDRequest;
import tst.campos.util.BadRequestException;

/**
 * Serviço de controle de Dados, define métodos de salvar, apagar e listar
 * entidades
 *
 * @author Caio
 */
@RestController("crudService")
public class CRUDService {

	/**
	 * Helper de controle de Dados
	 */
	@Autowired
	private CRUDHelper crudHelper;

	/**
	 * Busca um registro pelo Id
	 *
	 * @param req requisição enviada ao sistema
	 * @return Registro encontrado no banco
	 * @throws tst.campos.util.BadRequestException
	 */
	@RequestMapping("/findone")
	public @ResponseBody
	ResponseEntity<Object> findOne(@RequestBody CRUDRequest req) throws BadRequestException {
		return new ResponseEntity<>(crudHelper.findOne(req), HttpStatus.OK);
	}

	/**
	 * Busca todos os registros
	 *
	 * @param req requisição enviada ao sistema
	 * @return Registros encontrados no banco
	 * @throws tst.campos.util.BadRequestException
	 */
	@RequestMapping("/findall")
	public @ResponseBody
	ResponseEntity<Object[]> findAll(@RequestBody CRUDRequest req) throws BadRequestException {
		return new ResponseEntity<>(crudHelper.findAll(req).toArray(), HttpStatus.OK);
	}

	/**
	 * Busca por algum critério os registros
	 *
	 * @param req requisição enviada ao sistema
	 * @return Registros encontrados no banco
	 * @throws tst.campos.util.BadRequestException
	 */
	@RequestMapping("/findspecial")
	public @ResponseBody
	ResponseEntity<Object[]> findByCriteria(@RequestBody CRUDRequest req) throws BadRequestException {
		return new ResponseEntity<>(crudHelper.findSpecial(req).toArray(), HttpStatus.OK);
	}

	/**
	 * Busca pelo nome os registros
	 *
	 * @param req requisição enviada ao sistema
	 * @return Registros encontrados no banco
	 * @throws tst.campos.util.BadRequestException
	 */
	@RequestMapping("/findnome")
	public @ResponseBody
	ResponseEntity<Object[]> findByNome(@RequestBody CRUDRequest req) throws BadRequestException {
		req.special = "nomeParcialSemCaixa";
		return new ResponseEntity<>(crudHelper.findSpecial(req).toArray(), HttpStatus.OK);
	}

	/**
	 * Persiste um registro no banco
	 *
	 * @param req requisição enviada ao sistema
	 * @return Id do elemento persistido
	 * @throws tst.campos.util.BadRequestException
	 */
	@RequestMapping("/save")
	public @ResponseBody
	ResponseEntity<String> save(@RequestBody CRUDRequest req) throws BadRequestException {
		return new ResponseEntity<>(crudHelper.save(req), HttpStatus.OK);
	}

	/**
	 * Apaga um registro pelo Id
	 *
	 * @param req requisição enviada ao sistema
	 * @return Id do elemento apagado
	 * @throws tst.campos.util.BadRequestException
	 */
	@RequestMapping("/delete")
	public @ResponseBody
	ResponseEntity<String> delete(@RequestBody CRUDRequest req) throws BadRequestException {
		return new ResponseEntity<>(crudHelper.delete(req), HttpStatus.OK);
	}
}
