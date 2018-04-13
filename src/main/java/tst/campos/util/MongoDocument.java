package tst.campos.util;

import java.io.Serializable;

/**
 * Define uma interface para acessar os Ids do Mongo
 *
 * @author Caio
 */
public interface MongoDocument extends Serializable {

	String getId();

	void setId(String id);
}
