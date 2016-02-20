package org.servz.examples.requestscope;

import javax.inject.Inject;

import org.servz.http.component.HttpResponse;

import io.netty.handler.codec.http.HttpResponseStatus;

public class HttpResponseUtil {
  @Inject
  HttpResponseUtil() {
  }

  public HttpResponse responseWithText(final String text) {
    return new HttpResponse() {
      @Override
      public HttpResponseStatus status() {
        return HttpResponseStatus.OK;
      }

      @Override
      public String body() {
        return text;
      }
    };
  }
}
