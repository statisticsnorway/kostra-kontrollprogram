package no.ssb.kostra.control.barnevern.s15;

import java.util.regex.Pattern;

/**
 * Provides a simple wrapper for a value which is read from an untyped context
 * like HTTP parameters. It supports elegant <code>null</code> handling and type
 * conversions.
 */
public class Value {
	private Object data;

	/**
	 * Determines if the wrapped value is null.
	 */
	public boolean isNull() {
		return data == null;
	}

	private static final Pattern NUMBER = Pattern.compile("\\d+(\\.\\d+)?");

	/**
	 * Checks if the current value is numeric (integer or double).
	 */
	public boolean isNumeric() {
		return data instanceof Number
				|| NUMBER.matcher(asString("")).matches();
	}

	/**
	 * Returns the raw data.
	 */
	public Object get() {
		return data;
	}

	/**
	 * Returns the internal data or defaultValue if <code>null</code>.
	 */
	public Object get(Object defaultValue) {
		return data == null ? defaultValue : data;
	}

	/**
	 * Reads a value with a given type.
	 */
	@SuppressWarnings("unchecked")
	public <V> V get(Class<V> clazz, V defaultValue) {
		Object result = get(defaultValue);
		if (result == null || !clazz.isAssignableFrom(result.getClass())) {
			return defaultValue;
		}
		return (V) result;
	}

	/**
	 * Returns the data casted to a string, or <code>null</code> if the original
	 * data was null.
	 */
	public String getString() {
		return isNull() ? null : asString();
	}

	/**
	 * Returns the converted string. Returns "" instead of <code>null</code>.
	 */
	public String asString() {
		return data == null ? "" : data.toString();
	}

	/**
	 * Returns the value as string, or the default value if the given one was
	 * null.
	 */
	public String asString(String defaultValue) {
		return isNull() ? defaultValue : asString();
	}

	public int asInt(int defaultValue) {
		try {
			if (isNull()) {
				return defaultValue;
			}
			if (data instanceof Integer) {
				return (Integer) data;
			}
			return Integer.parseInt(String.valueOf(data));
		} catch (NumberFormatException e) {
			return defaultValue;
		}
	}

	public Integer getInteger() {
		try {
			if (isNull()) {
				return null;
			}
			if (data instanceof Integer) {
				return (Integer) data;
			}
			return Integer.parseInt(String.valueOf(data));
		} catch (NumberFormatException e) {
			return null;
		}
	}

	public Long getLong() {
		try {
			if (isNull()) {
				return null;
			}
			if (data instanceof Long) {
				return (Long) data;
			}
			return Long.parseLong(String.valueOf(data));
		} catch (NumberFormatException e) {
			return null;
		}
	}

	/**
	 * Creates a new wrapper for the given data.
	 */
	public static Value of(Object data) {
		Value val = new Value();
		val.data = data;
		return val;
	}

	@Override
	public String toString() {
		return asString();
	}

	/**
	 * Type and null safe implementation of the "Left" function for strings.
	 * This will return substring(0, length) and handle <code>null</code> values
	 * and shorter strings gracefully.
	 */
	public String left(int length) {
		String value = asString();
		if (value == null) {
			return null;
		}
		if (length < 0) {
			length = length * -1;
			if (value.length() < length) {
				return "";
			}
			return value.substring(length);
		} else {
			if (value.length() < length) {
				return value;
			}
			return value.substring(0, length);
		}
	}

	/**
	 * Type, range and null-safe implementation of the substring function.
	 */
	public String substring(int start, int length) {
		String value = asString();
		if (value == null) {
			return null;
		}
		if (start > value.length()) {
			return "";
		}
		return value.substring(start, Math.min(value.length(), length));
	}

	/**
	 * Type-safe method the get the length of the values' string representation.
	 */
	public int length() {
		String value = asString();
		if (value == null) {
			return 0;
		}
		return value.length();
	}

	/**
	 * Checks if the value implements the given class.
	 */
	public boolean is(Class<?> clazz) {
		return get() != null && clazz.isAssignableFrom(get().getClass());
	}
}
