package org.servz.http.netty;

import javax.inject.Inject;

import org.servz.http.component.HttpRequest;
import org.servz.http.component.HttpResponse;
import org.servz.logging.Logging;
import org.slf4j.Logger;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;

/** A Netty HTTP handler which wraps our HTTP handler. */
class NettyRequestHandler
    extends SimpleChannelInboundHandler<io.netty.handler.codec.http.HttpRequest> {
  private static final Logger logger = Logging.getLoggerForClass();
  private final HttpRequestProcessor httpRequestProcessor;

  @Inject
  NettyRequestHandler(HttpRequestProcessor httpRequestProcessor) {
    this.httpRequestProcessor = httpRequestProcessor;
  }

  @Override
  protected void channelRead0(
      ChannelHandlerContext context,
      io.netty.handler.codec.http.HttpRequest nettyRequest) throws Exception {
    HttpRequest httpRequest = new NettyHttpRequestAdapter(nettyRequest);
    HttpResponse httpResponse = httpRequestProcessor.processRequest(httpRequest);
    logger.info("HTTP " + httpRequest.method()
        + " " + httpRequest.uri() + " " + httpResponse.status());

    context.write(nettyHttpResponse(httpResponse));
    context.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
  }

  private static io.netty.handler.codec.http.HttpResponse nettyHttpResponse(
      HttpResponse httpResponse) {
    return new DefaultFullHttpResponse(
        HttpVersion.HTTP_1_0,
        httpResponse.status(),
        Unpooled.copiedBuffer(httpResponse.body(), CharsetUtil.UTF_8));
  }
}