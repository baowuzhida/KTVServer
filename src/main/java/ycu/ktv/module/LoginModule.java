package ycu.ktv.module;

import org.nutz.lang.util.NutMap;
import org.nutz.mvc.annotation.*;
import ycu.ktv.services.SendMessage;

public class LoginModule {
    @Ok("json")
    @At("/verify")
    @GET
    public void login(@Param("phone") String phone) {

        SendMessage.SendMessage(phone, 321654);
    }
}
