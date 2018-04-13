package tst.campos.util;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;

/**
 * Exceção padrão para o Projeto
 *
 * @author Caio
 */
public class BadRequestException extends Exception {
	private static final long	serialVersionUID	= 1L;
	public String				extras[]			= {};

	public BadRequestException(String message) {
		super(message);
	}

	public BadRequestException(String message, String... extras) {
		super(message);
		this.extras = extras;
	}

	// verifica se o 'o' é null, se for lança exceção
	public static void assertNotNull(Object o, Class<?> classe, String mensagem) throws BadRequestException {
		if (o == null) {
			throw new BadRequestException(mensagem);
		}
	}

	// verifica se o 'o' é null, se não for lança exceção
	public static void assertNull(Object o, Class<?> classe, String mensagem) throws BadRequestException {
		if (o != null) {
			throw new BadRequestException(mensagem);
		}
	}

	// verifica se o 'o' é vazio, se for lança exceção
	public static void assertNotEmpty(Object o, Class<?> classe, String mensagem) throws BadRequestException {
		// o == null
		if (o == null) {
			throw new BadRequestException(mensagem);
		} else {
			if (o instanceof String) {
				// o == ""
				if (((String) o).isEmpty()) {
					throw new BadRequestException(mensagem);
				}
				return;
			} else if (o instanceof BigDecimal) {
				// o == 0
				if (((BigDecimal) o).compareTo(BigDecimal.ZERO) == 0) {
					throw new BadRequestException(mensagem);
				}
				return;
			} else if (o instanceof Number) {
				// o == 0
				double d = ((Number) o).doubleValue();
				if (d < 0.0001d && d > -0.0001d) {
					throw new BadRequestException(mensagem);
				}
				return;
			} else if (o instanceof Collection) {
				// o == {}
				if (((Collection<?>) o).isEmpty()) {
					throw new BadRequestException(mensagem);
				}
				return;
			} else if (o.getClass().isArray()) {
				// o == {}
				if (((Object[]) o).length == 0) {
					throw new BadRequestException(mensagem);
				}
				return;
			}
		}
	}

	// verifica se o 'o' é vazio, se não for lança exceção
	public static void assertEmpty(Object o, Class<?> classe, String mensagem) throws BadRequestException {
		if (o != null) {
			if (o instanceof String) {
				// o != ""
				if (!((String) o).isEmpty()) {
					throw new BadRequestException(mensagem);
				}
				return;
			} else if (o instanceof BigDecimal) {
				// o != 0
				if (((BigDecimal) o).compareTo(BigDecimal.ZERO) != 0) {
					throw new BadRequestException(mensagem);
				}
				return;
			} else if (o instanceof Number) {
				// o != 0
				double d = ((Number) o).doubleValue();
				if (d >= 0.0001d || d <= -0.0001d) {
					throw new BadRequestException(mensagem);
				}
				return;
			} else if (o instanceof Collection) {
				// o != {}
				if (!((Collection<?>) o).isEmpty()) {
					throw new BadRequestException(mensagem);
				}
				return;
			} else if (o.getClass().isArray()) {
				// o == {}
				if (((Object[]) o).length != 0) {
					throw new BadRequestException(mensagem);
				}
				return;
			}
			throw new BadRequestException(mensagem);
		}
	}

	// Verifica se o 'o' é diferente de 'value', se não for lança exceção
	public static void assertNotEquals(Object o, Object value, Class<?> classe, String mensagem) throws BadRequestException {
		if (o == null) {
			// o == null && value == null
			if (value == null) {
				throw new BadRequestException(mensagem);
			}
			// o == null && value != null
			return;
		} else {
			// o != null && value == null
			if (value == null) {
				return;
			}
		}

		if (o instanceof String && value instanceof String) {
			if (o.equals(value)) {
				throw new BadRequestException(mensagem);
			}
			return;
		} else if (o instanceof BigDecimal && value instanceof BigDecimal) {
			if (((BigDecimal) o).compareTo((BigDecimal) value) == 0) {
				throw new BadRequestException(mensagem);
			}
			return;
		} else if (o instanceof Number && value instanceof Number) {
			if (((Number) o).doubleValue() == ((Number) o).doubleValue()) {
				throw new BadRequestException(mensagem);
			}
			return;
		} else if (o instanceof Timestamp && value instanceof Timestamp) {
			if (((Timestamp) o).compareTo((Timestamp) value) == 0) {
				throw new BadRequestException(mensagem);
			}
			return;
		} else if (o instanceof Date && value instanceof Date) {
			if (((Date) o).compareTo((Date) value) == 0) {
				throw new BadRequestException(mensagem);
			}
			return;
		} else if (o instanceof Comparable && value instanceof Comparable) {
			if (((Comparable) o).compareTo((Comparable) value) == 0) {
				throw new BadRequestException(mensagem);
			}
			return;
		} else if (o.equals(value)) {
			throw new BadRequestException(mensagem);
		}
	}

	// Verifica se o 'o' é igual a 'value', se não for lança exceção
	public static void assertEquals(Object o, Object value, Class<?> classe, String mensagem) throws BadRequestException {
		if (o == null) {
			// o == null && value != null
			if (value != null) {
				throw new BadRequestException(mensagem);
			}
			// o == null && value == null
			return;
		} else {
			// o != null && value == null
			if (value == null) {
				throw new BadRequestException(mensagem);
			}
		}

		if (o instanceof String && value instanceof String) {
			if (!o.equals(value)) {
				throw new BadRequestException(mensagem);
			}
			return;
		} else if (o instanceof BigDecimal && value instanceof BigDecimal) {
			if (((BigDecimal) o).compareTo((BigDecimal) value) != 0) {
				throw new BadRequestException(mensagem);
			}
			return;
		} else if (o instanceof Number && value instanceof Number) {
			if (((Number) o).doubleValue() != ((Number) o).doubleValue()) {
				throw new BadRequestException(mensagem);
			}
			return;
		} else if (o instanceof Timestamp && value instanceof Timestamp) {
			if (((Timestamp) o).compareTo((Timestamp) value) != 0) {
				throw new BadRequestException(mensagem);
			}
			return;
		} else if (o instanceof Date && value instanceof Date) {
			if (((Date) o).compareTo((Date) value) != 0) {
				throw new BadRequestException(mensagem);
			}
			return;
		} else if (o instanceof Comparable && value instanceof Comparable) {
			if (((Comparable) o).compareTo((Comparable) value) != 0) {
				throw new BadRequestException(mensagem);
			}
			return;
		} else if (!o.equals(value)) {
			throw new BadRequestException(mensagem);
		}
	}
}
