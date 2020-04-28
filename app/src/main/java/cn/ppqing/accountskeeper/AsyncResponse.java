package cn.ppqing.accountskeeper;
import java.util.*;
public interface AsyncResponse {
    void onDataReceivedSuccess(String data);
    void onDataReceivedFailed();
}
