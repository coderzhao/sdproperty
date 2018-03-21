package com.quadrant;

import com.note.cms.handler.WsMessStore;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
@Sharable
public class CameraChannelHandler extends ChannelInboundHandlerAdapter {
	private static final ByteBuf HEARTBEAT_SEQUENCE = 	Unpooled.unreleasableBuffer(Unpooled.copiedBuffer("1", CharsetUtil.ISO_8859_1));
	private static final Logger logger = LoggerFactory.getLogger(CameraChannelHandler.class);
	
	@Autowired
	private FDCameraDataHandler FDCameraDataHandler;
	
	public CameraChannelHandler() {
	}
	
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		logger.info(ctx.channel().remoteAddress() + " connected");		
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		String clientIp = getCameraIp(ctx);
		FDCameraDataHandler.setCameraSessionOffline(clientIp);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if(msg != null && msg instanceof FDCameraData) {
			String clientIp = getCameraIp(ctx);
			FDCameraData data = (FDCameraData)msg;
			FDCameraDataHandler.OnCameraData(clientIp, data);
			String camMac = data.mStrMac;
//			logger.info("Mac:"+camMac);
			Map<String,List<WebSocketSession>> macsession = WsMessStore.getMacsession();
			if(macsession==null) {
				logger.info("没有指定要播放的摄像头mac地址");
				return;
			}
			if(macsession.containsKey(camMac)) {
			    String s=null;
                if(data.mFaceNum>0) {
                    s = Base64.getEncoder().encodeToString(drawFaceBox(data).toByteArray());
//					 BufferedOutputStream fw = new BufferedOutputStream(
//					 new FileOutputStream("/home/n-tech-admin/"+ new Date()));
//					 fw.write(drawFaceBox(data).toByteArray());
//					 fw.flush();
//					 fw.close();
                }else{
                    s = Base64.getEncoder().encodeToString(data.mJpgData);
                }
//                s = Base64.getEncoder().encodeToString(data.mJpgData);
				WsMessStore.getInstance().addMessage(s,camMac);
			}
		}
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		if (evt instanceof IdleStateEvent) {
			String clientIp = getCameraIp(ctx);
			ctx.writeAndFlush(HEARTBEAT_SEQUENCE.duplicate()).addListener(new ChannelFutureListener() {
				@Override
				public void operationComplete(ChannelFuture future) {
					if (!future.isSuccess()) {
						future.channel().close();
						FDCameraDataHandler.setCameraSessionOffline(clientIp);
					}
				}
			});

		} else {
			super.userEventTriggered(ctx, evt);
		}
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		String clientIp = getCameraIp(ctx);
		logger.error(cause.getMessage() , cause);
		logger.info("ExceptionCaught from {}", clientIp);// let camera reconnect
		ctx.channel().close();
	}

	
	private String getCameraIp(ChannelHandlerContext ctx) {
		return ((InetSocketAddress)ctx.channel().remoteAddress()).getAddress().getHostAddress();
	}

	private ByteArrayOutputStream drawFaceBox(FDCameraData data){
        Image image = Toolkit.getDefaultToolkit().createImage(data.mJpgData);
        // This code ensures that all the pixels in the image are loaded
        image = new ImageIcon(image).getImage();
        BufferedImage bimage = null;
        // Create a buffered image using the default color model
        int type = BufferedImage.TYPE_INT_RGB;
        bimage = new BufferedImage(image.getWidth(null),
                image.getHeight(null), type);
        // Copy image to buffered image
        Graphics g = bimage.getGraphics();
        Graphics2D g2d=(Graphics2D)g;
        // Paint the image onto the buffered image
        g2d.drawImage(image, 0, 0, null);
        Stroke stroke=new BasicStroke(4.0f);//设置线宽为3.0
        g2d.setStroke(stroke);
        g2d.setColor(Color.green);//画笔颜色
        for(int i=0;i<data.mFaceNum;i++){
            FaceDefine faceDefine = data.mFaceItem[i];
            if(faceDefine!=null){
                int x= (int) (faceDefine.left);
                int y= (int) (faceDefine.top);
                int width = (int) ((faceDefine.right-faceDefine.left));
                int height= (int) ((faceDefine.bottom-faceDefine.top));
                g2d.drawRect(x, y, width, height);
            }

        }

        g2d.dispose();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {

            ImageIO.write(bimage,"jpeg", baos);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
			try {
				baos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return baos;
	}
	
}
