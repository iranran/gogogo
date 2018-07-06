package hello.programmer.designpatterns.chain;

import java.util.HashMap;
import java.util.Map;

/**
 * @author liwei
 * @Description
 * @date 2017/7/25 13:50
 */
public class JContext {

    private int loanInfoId;

    private int assetId;

    private Map<String,Object> params = new HashMap<>();

    public void put(String key,Object value){
        params.put(key,value);
    }

    public int getLoanInfoId() {
        return loanInfoId;
    }

    public void setLoanInfoId(int loanInfoId) {
        this.loanInfoId = loanInfoId;
    }

    public int getAssetId() {
        return assetId;
    }

    public void setAssetId(int assetId) {
        this.assetId = assetId;
    }


    public <T> T get(String key, Class<T> clazz){
        Object value = params.get(key);
        if(value == null){
            return null;
        }
        return (T)value;
    }

}
