package ycu.ktv.module;

import org.nutz.dao.Cnd;
import org.nutz.mvc.annotation.*;
import ycu.ktv.dao.GetDao;
import ycu.ktv.entity.Message;
import ycu.ktv.entity.User;
import ycu.ktv.services.RedisServices;
import ycu.ktv.services.TokenControl;

import java.util.List;
//        .......................................................
//        .......................................................
//        ........................../@@............./@@@@@\......
//        .......................]@@@@@@........../@@@O[@@@^.....
//        ....................../@@@/@@@\]]]]]]*./@@@..=@@@......
//        ..................,]/@@@/[oO@@@@@@@@@@@@@@^...@@@^.....
//        ...............,O@@@@@@O*..**[[*.....,[[`***.=@@@^.....
//        .............,@@@@/`..........................\@@@@]...
//        ............/@@@^.............../@@@O^.........*,O@@O..
//        .........*/@@@/..../@@@^....,],O@O[\Oo............\@@O.
//        .........,@@@^.....,..\@@@o,O@@@@@@@O^............*@@@^
//        .........@@@^.......,@@@O/.....[[[[............. ..\@@^
//        .......*@@@^.............. ....... ................=@@^
//        .......*@@@...........,]...*,]/O@O`.................@@O
//        .......=@@O..........=OOOO@@@@@^..oOOo^.............O@/
//        .......=@@O.........*,...*oOOO]`./OO`*.............=@@^
//        ........@@@...........,@@@@@@@@@@@OOo]*...........,@@@^
//        ........\@@\`......../@@@@[.,[[[[[O/o/ooOo`......=@@@`.
//        .........O@@@`......*[*..]OO@@@@OOOOO/[[.......,@@@O...
//        ..........,@@@`...........\O/[[............]]@@@@/`....
//        ............,@@@@@@@@@^..................=@@@@@`.......
//        ...............*.[@@@/....................[\@@@\.......
//        .................O@@/.......................,@@@\......
//        .................@@@O@@`..............*O@@`...@@@^.....
//        ................=@@@O@@\*..............O@@O...O@@@.....
//        ................=@@OO@@^................@@@^..,@@@.....
//        ................=@@O/@@^................=@@O...O@@^....
//        ................=@@OO@@^................=@@O*..O@@^....
//                             此图送给客户端的鶸

public class LoginModule {

    @Ok("json")
    @At("/login")
    @POST
    public Message login(@Param("phone") String phone,@Param("password")String password){
        Message<User> message = new Message();
        List<User> users = GetDao.getDao().query(User.class, Cnd.where("kt_user_phone", "=", phone).
                and("kt_user_password","=",password));
        try {
            if (!users.isEmpty()) {
                String token = TokenControl.getToken(users.get(0).getId());
                users.get(0).setUser_password(null);
                User user =users.get(0);
                message.setBody(user);
                message.setMessage(token);
                message.setStatus("1");
                return message;
            } else {
                message.setBody(null);
                message.setMessage("密码错误");
                message.setStatus("2");
                return message;
            }
        }catch (Exception e){
            message.setBody(null);
            message.setMessage("未知错误");
            message.setStatus("0");
            return message;
        }
    }

    @Ok("json")
    @At("/logout")
    @POST
    public Message logout(@Param("token") String token){
        Message<User> message = new Message();
        Boolean ifdel = RedisServices.DelToken(token);
        try {
            if (ifdel) {
                message.setBody(null);
                message.setMessage("success");
                message.setStatus("1");
                return message;
            } else {
                message.setBody(null);
                message.setMessage("token错误");
                message.setStatus("2");
                return message;
            }
        }catch (Exception e){
            message.setBody(null);
            message.setMessage("未知错误");
            message.setStatus("0");
            return message;
        }
    }
}
