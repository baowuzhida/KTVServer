package ycu.ktv.module;

import org.nutz.dao.Cnd;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.GET;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;
import ycu.ktv.dao.GetDao;
import ycu.ktv.entity.Code;
import ycu.ktv.entity.Message;
import ycu.ktv.entity.User;
import ycu.ktv.services.SendMessage;

import java.util.Date;

public class RegisterModule {

    @Ok("json")
    @At("/askcode")
    @GET
    public Message askcode(@Param("phone") String phone){
        Message message = new Message();
        double codenum = (Math.random()*9+1)* 100000;
        Boolean ifsend= SendMessage.SendMessage(phone,(int)codenum);
        if(ifsend){
            Code code = new Code();
            long time = new Date().getTime();
            String time1 = time+2*60*1000+"";

            code.setUser_phone(phone);
            code.setCode_info((int)codenum);
            code.setCode_life(time1);
            System.out.println(code);
            GetDao.getDao().insert(code);
            message.setBody(null);
            message.setMessage("success");
            message.setStatus("1");
            return message;
        }
        message.setBody(null);
        message.setMessage("未知错误");
        message.setStatus("0");
        return message;
    }

    @Ok("json")
    @At("/register")
    @GET
    public Message register(@Param("code")int code,@Param("phone")String phone){
        Message message = new Message();
        User user = new User();
        if(!GetDao.getDao().query(Code.class, Cnd.where("kt_code_info", "=", code)).equals(null)){
            user.setUser_phone(phone);
            GetDao.getDao().insert(user);
            GetDao.getDao().clear(Code.class,Cnd.where("kt_code_info", "=", code));
            message.setBody(user);
            message.setMessage("success");
            message.setStatus("1");
            return message;
        }
        message.setBody(user);
        message.setMessage("验证码错误");
        message.setStatus("2");
        return message;
    }
}
