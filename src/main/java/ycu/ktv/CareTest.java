package ycu.ktv;

import ycu.ktv.entity.Message;
import ycu.ktv.module.RoomUpDownMai;
import ycu.ktv.services.TokenControl;

public class CareTest {
    public static void main(String[] args) {
        RoomUpDownMai rudm =new RoomUpDownMai();
//        Message message=rudm.roomUpM("999", TokenControl.getToken(77),"999");
        Message message=rudm.getSort("88");
        System.out.println(message.getMessage());

    }
}
