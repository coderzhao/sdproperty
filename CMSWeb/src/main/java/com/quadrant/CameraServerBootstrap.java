package com.quadrant;

import com.note.cms.handler.WsMessStore;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.oio.OioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.oio.OioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

//import java.nio.channels.SocketChannel;

@Component
public class CameraServerBootstrap {
	
	@Autowired
	private CameraChannelHandler cameraServerHandler;
	private int port = 8100;
	private static final int CHANNEL_IDLE_TIMEOUT_SECOND = 120;
	private ServerBootstrap bootstrap = null; 
	private EventLoopGroup group = null;
	public CameraServerBootstrap() {}

	@PostConstruct
	public void init() {
		WsMessStore.getInstance().startPushMessThread();
//		group = new OioEventLoopGroup();
		group = new NioEventLoopGroup();
		try {
			bootstrap = new ServerBootstrap();
			bootstrap.group(group)
					.channel(NioServerSocketChannel.class)
//					.channel(OioServerSocketChannel.class)
					.localAddress(new InetSocketAddress(port))
					.childHandler(new ChannelInitializer<SocketChannel>() {
						@Override
						public void initChannel(SocketChannel channel) throws Exception {
							ChannelPipeline pipeline = channel.pipeline();
							pipeline.addLast(new IdleStateHandler(0, 0, CHANNEL_IDLE_TIMEOUT_SECOND, TimeUnit.SECONDS))
									.addLast(new CameraByteToMessageDecoder())
									.addLast(cameraServerHandler);
							
						}
					});
			
			 bootstrap.bind().sync();
			 System.out.println("[[ Camera Server Started ]]");
//			ChannelFuture f = bootstrap.bind().sync();
//			f.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} 
		
	}
	
	@PreDestroy
	public void stop() {
		try {
			System.out.println("[[ Camera Server shutdowing ]]");
			group.shutdownGracefully().sync();
			System.out.println("[[ Camera Server Stoped ]]");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
//	public static void main(String[] args) {
//		CameraServerBootstrap b = new CameraServerBootstrap();
//		b.init();
//	}
	
}
