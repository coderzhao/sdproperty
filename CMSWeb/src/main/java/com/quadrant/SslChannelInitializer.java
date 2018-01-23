package com.quadrant;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;

import javax.net.ssl.SSLEngine;

public class SslChannelInitializer extends ChannelInitializer<Channel> {
	
	private SslContext context;
	private boolean startTls;

	public SslChannelInitializer(SslContext context, boolean startTls) {
		this.context = context;
		this.startTls = startTls;
//		SslContextBuilder.forServer(keyCertChainFile, keyFile)
	}

	@Override
	protected void initChannel(Channel ch) throws Exception {
		SSLEngine engine = context.newEngine(ch.alloc());
		ch.pipeline().addFirst("ssl", new SslHandler(engine, startTls));
	}

}
