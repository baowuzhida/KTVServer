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

import java.util.List;

public class LoginModule {

    @Ok("json")
    @At("/login")
    @GET
    public Message login(@Param("phone") String phone,@Param("password")String password){
        Message message = new Message();
//        GetDao.getDao().query(User.class, Cnd.where("kt_user_phone", "=", phone).and("kt_user_password","=",password));
        if(!GetDao.getDao().query(User.class, Cnd.where("kt_user_phone", "=", phone).and("kt_user_password","=",password)).equals(null)){

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
