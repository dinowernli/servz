package org.servz.http.component;

import io.netty.handler.codec.http.HttpResponseStatus;

public interface HttpResponse {
  HttpResponseStatus status();
  String body();
}
