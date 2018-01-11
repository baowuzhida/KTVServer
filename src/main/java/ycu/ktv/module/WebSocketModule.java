package ycu.ktv.module;


import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.util.NutMap;
import org.nutz.plugins.mvc.websocket.AbstractWsEndpoint;
import org.nutz.plugins.mvc.websocket.NutWsConfigurator;
import org.nutz.plugins.mvc.websocket.WsHandler;
import org.nutz.plugins.mvc.websocket.handler.SimpleWsHandler;

import javax.websocket.EndpointConfig;
import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

// ServerEndpoint是websocket的必备注解, value是映射路径, configurator是配置类.
@ServerEndpoint(value = "/websocket", configurator = NutWsConfigurator.class)
@IocBean // 使用NutWsConfigurator的必备条件
public class WebSocketModule extends AbstractWsEndpoint {

}