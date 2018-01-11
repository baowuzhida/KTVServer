import org.nutz.json.Json;
import ycu.ktv.entity.Message;
import ycu.ktv.module.RoomUpDownMai;
import ycu.ktv.services.TokenControl;

public class CareTest {
    public static void main(String[] args){
        RoomUpDownMai rudm =new RoomUpDownMai();
//        Message message =rudm.roomUpM("9876", TokenControl.getToken(99),"6","6","6","6","aaaaaaaaaaaaa");
 //       System.out.println(message.getMessage());
//        String a = TokenControl.getToken(101);
//        System.out.println(a);
        Message message =
//        rudm.roomDownM("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxMDEifQ.ehF6xEkcciR4SicMbgoDLTeVJDmwOS4xjM7wSfcvAwe4gnUkSzVPJbaurfvtWqGH3IDMwGFaV9mEK4psAmoXiA");
        rudm.getSort("88");



    }
}
