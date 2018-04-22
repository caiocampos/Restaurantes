package tst.campos.util.annotation;

/**
 *
 * @author Caio
 */
public @interface FKInfo {

	String entity();

	String search(); // nome da query para busca

	String param(); // campo para busca
}
