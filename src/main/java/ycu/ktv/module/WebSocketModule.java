package ycu.ktv.module;


import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Each;
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

    @Override
    public WsHandler createHandler(Session session, EndpointConfig config) {
        return new MySimpleWsHandler();
    }


    public class MySimpleWsHandler extends SimpleWsHandler {
        public MySimpleWsHandler() {
            super(""); // 覆盖默认前缀
        }

        public void msg2room(final NutMap req) {
            final String room = req.getString("room");
            if (room != null) {
                String _room = room;
                if (this.prefix.length() > 0 && !room.startsWith(this.prefix)) {
                    _room = this.prefix + room;
                }

                this.endpoint.each(_room, new Each<Session>() {
                    public void invoke(int index, Session ele, int length) {
                        NutMap resp = new NutMap("action", "msg");
                        resp.setv("room", room);
                        resp.setv("timestamp", System.currentTimeMillis());
                        resp.setv("msg", req.get("msg"));
                        if (MySimpleWsHandler.this.nickname != null) {
                            resp.setv("nickname", MySimpleWsHandler.this.nickname);
                        }

                        MySimpleWsHandler.this.endpoint.sendJson(ele.getId(), resp);
                    }
                });
            }
        }

    }
}