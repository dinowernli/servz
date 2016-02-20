package org.servz.examples.requestscope;

import java.net.URI;
import java.util.UUID;

import org.servz.handler.Annotations.HandlerModule;
import org.servz.handler.Annotations.Handles;
import org.servz.http.Annotations.Get;
import org.servz.http.Annotations.HttpParams;
import org.servz.http.Annotations.HttpRequest;
import org.servz.http.component.HttpResponse;

import com.google.common.collect.ImmutableMap;
import com.google.common.escape.Escaper;
import com.google.common.html.HtmlEscapers;

@HandlerModule
public class HelloWorldHandlerModule {
  @Handles
  @HttpRequest
  @Get(path = "/example/hello")
  public HttpResponse execute(
      URI requestUri,
      HttpResponseUtil responseUtil,
      UUID requestId,
      @HttpParams ImmutableMap<String, String> params) {
    Escaper htmlEscaper = HtmlEscapers.htmlEscaper();
    String escapedParams = htmlEscaper.escape(params.toString());
    String escapedUri = htmlEscaper.escape(requestUri.toString());
    return responseUtil.responseWithText("Hello world, uri: " + escapedUri + ", uuid: "
        + requestId.toString() + "\n\n" + escapedParams);
  }
}
