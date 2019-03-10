package hello.work;

import com.alibaba.fastjson.JSON;
import com.google.gson.JsonObject;
import lombok.Data;

import javax.persistence.Column;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
public class ProtoGen {

    @Column(name = "id")
    private Long id;

    @Column(name = "org_id")
    private Long orgId;

    @Column(name = "name")
    private String name;

    @Column(name = "fee_rate")
    private BigDecimal feeRate;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "main_account_id")
    private Long mainAccountId;

    @Column(name = "max_income_rate")
    private BigDecimal maxIncomeRate;

    @Column(name = "min_outgo_rate")
    private BigDecimal minOutgoRate;

    @Column(name = "currency")
    private String currency;

    @Column(name = "token_type")
    private String tokenType;

    @Column(name = "created")
    private Long created;

    @Column(name = "updated")
    private Long updated;

    public static boolean isUpperCase(String word){
        for(int i = 0; i < word.length(); i++){
            char c = word.charAt(i);
            if (Character.isUpperCase(c)){
                return true;
            }
        }
        return false;
    }

    private static void genProtoBuf(){
        int counter = 0;
        Field[] fields = ProtoGen.class.getDeclaredFields();
        for (Field field : fields){
            StringBuilder content = new StringBuilder();

            Class<?> clz = field.getType();
            if (clz.getName().contains("Long")) {
                content.append("int64 ");
            } else if (clz.getName().contains("Timestamp")) {
                content.append("int64 ");
            } else if (clz.getName().contains("Integer")) {
                content.append("int32 ");
            } else {
                content.append("string ");
            }
            String name = field.getName();

            for(int i = 0; i < name.length(); i++){
                char c = name.charAt(i);
                if (Character.isUpperCase(c)){
                    content.append("_").append(String.valueOf(c).toLowerCase());
                }
                else {
                    content.append(c);
                }
            }

            content.append(" = ").append(++counter).append(";");
            System.out.println(content);
        }
    }

    private static void genDefaultJson() throws Exception{
        int counter = 0;
        ProtoGen gen = new ProtoGen();
        Field[] fields = ProtoGen.class.getDeclaredFields();
        for (Field field : fields){
            StringBuilder content = new StringBuilder();

            Class<?> clz = field.getType();
            if (clz.getName().contains("Long")) {
                field.setLong(gen,1l);
            } else if (clz.getName().contains("Timestamp")) {
                content.append("int64 ");
            } else if (clz.getName().contains("Integer")) {
                field.setInt(gen, 1);
            } else {
                field.set(gen, "h");
            }
            String name = field.getName();

            content.append(" = ").append(name).append(";");
            System.out.println(gen);
        }
    }
    public static void main(String[] args) throws Exception{
       genProtoBuf();

        // genDefaultJson();
    }
}
