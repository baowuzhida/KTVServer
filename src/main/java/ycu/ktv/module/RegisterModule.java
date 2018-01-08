package ycu.ktv.module;

import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.mvc.annotation.*;
import sun.awt.SunHints;
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
    @POST
    public Message askcode(@Param("phone") String phone){
        Message<String> message = new Message();
        int codenum = (int)((Math.random()*9+1)* 100000);
        if(!GetDao.getDao().query(User.class, Cnd.where("kt_user_phone", "=", phone)).isEmpty()){
            message.setBody(null);
            message.setMessage("手机号码重复");
            message.setStatus("5");
            return message;
        }else if(!GetDao.getDao().query(Code.class, Cnd.where("kt_user_phone", "=", phone)).isEmpty()){

            Boolean ifsend= SendMessage.SendMessage(phone,codenum);
            if(ifsend){
                String time = new Date().getTime()+2*60*1000+"";

                GetDao.getDao().update(Code.class, Chain.makeSpecial("kt_code_info", codenum).add("kt_code_life", time), Cnd.where("kt_user_phone","=", phone));

                message.setBody(null);
                message.setMessage("success");
                message.setStatus("1");
                return message;
            }
        }else{
            Boolean ifsend= SendMessage.SendMessage(phone,codenum);
            if(ifsend) {
                Code code = new Code();
                String time = new Date().getTime() + 2 * 60 * 1000 + "";
                code.setUser_phone(phone);
                code.setCode_info(codenum);
                code.setCode_life(time);
                GetDao.getDao().insert(code);

                message.setBody(null);
                message.setMessage("success");
                message.setStatus("1");
                return message;
            }
        }
        message.setBody(null);
        message.setMessage("未知错误");
        message.setStatus("0");
        return message;
    }

    @Ok("json")
    @At("/verify")
    @POST
    public Message register(@Param("code")int code,@Param("password")String password,@Param("phone")String phone){
        Message message = new Message();
        User user = new User();

        List<Code> codes = GetDao.getDao().query(Code.class, Cnd.where("kt_user_phone", "=", phone));
        long time = new Date().getTime();
        long life = Long.parseLong(codes.get(0).getCode_life());
        if(time>life){
            message.setBody(user);
            message.setMessage("验证码超时");
            message.setStatus("7");
            return message;
        }
        else if(!codes.isEmpty()){
            user.setUser_phone(phone);
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
