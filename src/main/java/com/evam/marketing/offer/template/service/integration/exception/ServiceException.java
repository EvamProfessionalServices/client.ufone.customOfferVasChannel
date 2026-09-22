package com.evam.marketing.offer.template.service.integration.exception;

import org.springframework.core.NestedRuntimeException;

public class ServiceException extends NestedRuntimeException {

  public ServiceException(String msg) {
    super(msg);
  }
}
