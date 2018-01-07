package ycu.ktv.module;

import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.GET;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;
import ycu.ktv.entity.Message;
import ycu.ktv.services.SendMessage;

public class LoginModule {

    @Ok("json")
    @At("/login")
    @GET
    public Message login(@Param("phone") String phone){
        Message message = new Message();
        double code = (Math.random()*9+1)* 100000;
        Boolean ifsend= SendMessage.SendMessage(phone,(int)code);
        if(ifsend){
            message.setBody(null);
            message.setMessage("success");
            message.setStatus("1");
            return message;
        }
        message.setBody(null);
        message.setMessage("验证码错误");
        message.setStatus("2");
        return message;
    }
}
