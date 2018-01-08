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
import java.util.List;

public class RegisterModule {

    @Ok("json")
    @At("/askcode")
    @GET
    public Message askcode(@Param("phone") String phone){
        Message message = new Message();
        if(!GetDao.getDao().query(User.class, Cnd.where("kt_user_phone", "=", phone)).equals(null)){
            message.setBody(null);
            message.setMessage("手机号码重复");
            message.setStatus("5");
            return message;
        }

        int codenum = (int)((Math.random()*9+1)* 100000);
        Boolean ifsend= SendMessage.SendMessage(phone,codenum);
        if(ifsend){
            Code code = new Code();
            long time = new Date().getTime();
            String time1 = time+2*60*1000+"";

            code.setUser_phone(phone);
            code.setCode_info(codenum);
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
    public Message register(@Param("code")int code,@Param("password")String password){
        Message message = new Message();
        User user = new User();
        List<Code> codes = GetDao.getDao().query(Code.class, Cnd.where("kt_code_info", "=", code));
        if(!GetDao.getDao().query(Code.class, Cnd.where("kt_code_info", "=", code)).equals(null)){
            user.setUser_phone(codes.get(0).getUser_phone());
            user.setUser_password(password);
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
