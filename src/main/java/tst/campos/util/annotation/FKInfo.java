package tst.campos.util.annotation;

/**
 * Anotação que engloba todas as informações de Campos do tipo 'FOREIGN'
 *
 * @author Caio
 */
public @interface FKInfo {

	String entity();

	String search(); // nome da query para busca

	String param(); // campo para busca
}
