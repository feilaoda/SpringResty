package org.springresty.framework.validation;

import org.springresty.exception.EmptyParameterException;
import org.springresty.exception.RestyException;

public interface Validator {
	public String getName();
	public void validate(String value) throws RestyException;
}
