package ycu.ktv.module;

import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.POST;
import org.nutz.mvc.annotation.Param;
import ycu.ktv.dao.GetDao;
import ycu.ktv.entity.Code;
import ycu.ktv.entity.Message;
import ycu.ktv.entity.User;
import ycu.ktv.services.TokenControl;

import java.util.List;

public class UserInfoModule {

    @Ok("json")
    @At("/userinfo")
    @POST
    public Message userinfo(@Param("name") String name, @Param("avatar")String avatar,@Param("token") String token){
        Message<User> message = new Message();
//        List<User> users = GetDao.getDao().query(User.class, Cnd.where("kt_user_phone", "=", phone).
//                and("kt_user_password","=",password));
        String user_id=TokenControl.analysisToken(token);
        if(user_id.equals("")){
            message.setBody(null);
            message.setMessage("Token失效");
            message.setStatus("2");
            return message;
        }else if(user_id.equals(null)) {
            message.setBody(null);
            message.setMessage("未知错误");
            message.setStatus("0");
            return message;
        }else if(!user_id.equals("")){
            GetDao.getDao().update(User.class, Chain.make("kt_user_name", name)
                    .add("kt_user_avatar", avatar), Cnd.where("kt_user_id","=", Integer.parseInt(user_id)));
            User user = GetDao.getDao().fetch(User.class,Integer.parseInt(user_id));
            user.setUser_password(null);
            message.setBody(user);
            message.setMessage("success");
            message.setStatus("1");
            return message;
        }
        message.setBody(null);
        message.setMessage("未知错误");
        message.setStatus("0");
        return message;
    }
}
