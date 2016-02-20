package org.servz.examples.http;

import org.servz.handler.Annotations.HandlerModule;
import org.servz.handler.Annotations.Handles;
import org.servz.http.Annotations.Get;
import org.servz.http.Annotations.HttpParams;
import org.servz.http.Annotations.HttpRequest;
import org.servz.http.component.HttpResponse;

import com.google.common.collect.ImmutableMap;
import com.google.common.escape.Escaper;
import com.google.common.html.HtmlEscapers;

import io.netty.handler.codec.http.HttpResponseStatus;

@HandlerModule
class HelloHttpHandlerModule {
  private static final String HTTP_PATH = "/hello";
  private static final String HTTP_PARAM_USER = "u";

  @Handles
  @HttpRequest
  @Get(path = HTTP_PATH)
  HttpResponse handleGet(final @HttpParams ImmutableMap<String, String> params) {
    final Escaper htmlEscaper = HtmlEscapers.htmlEscaper();
    return new HttpResponse() {
      @Override
      public HttpResponseStatus status() {
        return HttpResponseStatus.OK;
      }

      @Override
      public String body() {
        String subject = htmlEscaper.escape(params.getOrDefault(HTTP_PARAM_USER, "world"));
        return String.format("<html><body>Hello, %s!</body></head>", subject);
      }
    };
  }
}
