package cn.ppqing.accountskeeper.db;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.thegrizzlylabs.sardineandroid.DavResource;
import com.thegrizzlylabs.sardineandroid.Sardine;
import com.thegrizzlylabs.sardineandroid.impl.OkHttpSardine;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import cn.ppqing.accountskeeper.Data;

public class WebDAV {
    Sardine sardine;

    String username;
    String password;
    String serverURL;
    String dirURL;
    String fileName="data.json";

    Context mContext;


    public WebDAV(Context context,String username,String password,String serverURL){
        sardine = new OkHttpSardine();
        sardine.setCredentials(username,password);
        this.username=username;
        this.password=password;
        String last=serverURL.substring(serverURL.length()-1,serverURL.length());
        if(!last.equals("/")){
            serverURL+="/";
        }
        Log.d("webdav","server:"+serverURL+" last:"+last);
        this.serverURL=serverURL;
        this.dirURL =serverURL+"AccountsKeeper/";
        mContext=context;
    }

    public boolean upload(List<Data> list)  {
        try {
            sardine.createDirectory(dirURL);
            updateFile(list);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            Log.w("webdav","上传失败");
            return false;
        }
    }


    public boolean readFile() throws IOException {
        List<DavResource> resources = sardine.list(serverURL);

        return true;
    }

    public void updateFile(List<Data> list) throws IOException {
        //InputStream inputStream=new FileInputStream(fileName);
        Gson gson=new Gson();
        String jsonStr=gson.toJson(list);

        byte[] buff=jsonStr.getBytes();
        sardine.put(dirURL+fileName,buff);
    }

    public String download(){
        try {
            InputStream inputStream=null;
            inputStream=sardine.get(dirURL+fileName);

            ByteArrayOutputStream bos = new ByteArrayOutputStream();

            byte[] buffer = new byte[2048];
            int length = 0;
            while((length = inputStream.read(buffer)) != -1) {
                bos.write(buffer, 0, length);//写入输出流
            }
            inputStream.close();//读取完毕，关闭输入流
            String jsonStr=new String(bos.toByteArray(), "UTF-8");
            Log.d("webdav",jsonStr);
            return jsonStr;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
}
