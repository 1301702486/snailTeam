package com.snail.child.service.faceRecog;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.snail.child.model.ChildFindParent;
import com.snail.child.model.ParentFindChild;
import com.snail.child.model.SuspectedMissingChild;
import com.snail.child.model.User;
import com.snail.child.repository.ChildFindParentRepository;
import com.snail.child.repository.ParentFindChildRepository;
import com.snail.child.repository.SuspectedMissingChildRepository;
import com.snail.child.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.net.ssl.SSLException;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;

/**
 * Author: 郭瑞景
 * Date: 2019/7/3
 * Description: No Description
 */
@Service
public class FaceDetectService {
    @Autowired
    ParentFindChildRepository pfcRepository;

    @Autowired
    ChildFindParentRepository cfpRepository;

    @Autowired
    SuspectedMissingChildRepository smcRepository;

    @Autowired
    UserRepository userRepository;

    private final int CONNECT_TIME_OUT = 30000;
    private final int READ_OUT_TIME = 50000;
    private String boundaryString = getBoundary();

    /**
     * 根据邮箱获取该家长(用户)要寻找的孩子的照片(家寻宝贝)
     *
     * @param emailAddr
     * @return
     */
    public String getPfcPhotoByEmailAddr(String emailAddr) {
        User user = userRepository.findUserByEmailAddr(emailAddr);
        if (user != null) {
            ParentFindChild pRelease = user.getParentFindChild();
            if (pRelease != null) {
                return pRelease.getPhoto();
            }
        }
        return null;
    }

    /**
     * 根据邮箱获取该用户自己的照片(宝贝寻家)
     *
     * @param emailAddr
     * @return
     */
    public String getCfpPhotoByEmailAddr(String emailAddr) {
        User user = userRepository.findUserByEmailAddr(emailAddr);
        if (user != null) {
            ChildFindParent cRelease = user.getChildFindParent();
            if (cRelease != null) {
                return cRelease.getPhoto();
            }
        }
        return null;
    }

    /**
     * 根据用户邮箱和发布id获取照片
     *
     * @param emailAddr
     * @param id
     * @return
     */
    public String getSmcPhotoByEmailAndId(String emailAddr, Integer id) {
        User user = userRepository.findUserByEmailAddr(emailAddr);
        if (user != null) {
            Set<SuspectedMissingChild> children = user.getSuspectedMissingChildren();
            if (!children.isEmpty()) {
                for (SuspectedMissingChild child: children) {
                    if (child.getId().equals(id)) {
                        return child.getPhoto();
                    }
                }
            }
        }
        return null;
    }

    public String getFaceString(String imageUrl) {
        String url = "https://api-cn.faceplusplus.com/facepp/v3/detect";
        HashMap<String, String> map = new HashMap<>();
        HashMap<String, byte[]> byteMap = new HashMap<>();
        map.put("api_key", "GTvcIl8x4HX25ytluaDi7eicC7rv2Ad_");
        map.put("api_secret", "7CMHtPRjOD14UyEft6yb_-EJfDVaJjnj");
//      map.put("return_landmark", "1");
        map.put("return_attributes", "gender,age,smiling,headpose,facequality,blur,eyestatus,emotion,ethnicity,beauty,mouthstatus,eyegaze,skinstatus");
        map.put("image_url", imageUrl);
        String faceString = null;
        try {
            byte[] bacd = post(url, map, byteMap);
            faceString = new String(bacd);
            System.out.println(faceString);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        String faceToken = getFaceToken(faceString);
        return faceString;
    }

    protected byte[] post(String url, HashMap<String, String> map, HashMap<String, byte[]> fileMap) throws Exception {
        HttpURLConnection conne;
        URL url1 = new URL(url);
        conne = (HttpURLConnection) url1.openConnection();
        conne.setDoOutput(true);
        conne.setUseCaches(false);
        conne.setRequestMethod("POST");
        conne.setConnectTimeout(CONNECT_TIME_OUT);
        conne.setReadTimeout(READ_OUT_TIME);
        conne.setRequestProperty("accept", "*/*");
        conne.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundaryString);
        conne.setRequestProperty("connection", "Keep-Alive");
        conne.setRequestProperty("user-agent", "Mozilla/4.0 (compatible;MSIE 6.0;Windows NT 5.1;SV1)");
        DataOutputStream obos = new DataOutputStream(conne.getOutputStream());
        Iterator iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<String, String> entry = (Map.Entry) iter.next();
            String key = entry.getKey();
            String value = entry.getValue();
            obos.writeBytes("--" + boundaryString + "\r\n");
            obos.writeBytes("Content-Disposition: form-data; name=\"" + key
                    + "\"\r\n");
            obos.writeBytes("\r\n");
            obos.writeBytes(value + "\r\n");
        }
        if (fileMap != null && fileMap.size() > 0) {
            Iterator fileIter = fileMap.entrySet().iterator();
            while (fileIter.hasNext()) {
                Map.Entry<String, byte[]> fileEntry = (Map.Entry<String, byte[]>) fileIter.next();
                obos.writeBytes("--" + boundaryString + "\r\n");
                obos.writeBytes("Content-Disposition: form-data; name=\"" + fileEntry.getKey()
                        + "\"; filename=\"" + encode(" ") + "\"\r\n");
                obos.writeBytes("\r\n");
                obos.write(fileEntry.getValue());
                obos.writeBytes("\r\n");
            }
        }
        obos.writeBytes("--" + boundaryString + "--" + "\r\n");
        obos.writeBytes("\r\n");
        obos.flush();
        obos.close();
        InputStream ins = null;
        int code = conne.getResponseCode();
        try {
            if (code == 200) {
                ins = conne.getInputStream();
            } else {
                ins = conne.getErrorStream();
            }
        } catch (SSLException e) {
            e.printStackTrace();
            return new byte[0];
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buff = new byte[4096];
        int len;
        while ((len = ins.read(buff)) != -1) {
            baos.write(buff, 0, len);
        }
        byte[] bytes = baos.toByteArray();
        ins.close();
        return bytes;
    }

    private String getBoundary() {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 32; ++i) {
            sb.append("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_-".charAt(random.nextInt("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_".length())));
        }
        return sb.toString();
    }

    private String encode(String value) throws Exception {
        return URLEncoder.encode(value, "UTF-8");
    }

    /**
     * 获取face_token
     *
     * @param imgUrl
     * @return
     * @throws IOException
     */
    public String getFaceToken(String imgUrl) {
        String faceStr = getFaceString(imgUrl);
        Map<String, Object> map = new Gson().fromJson(faceStr, new TypeToken<HashMap<String, Object>>() {}.getType());
        ArrayList<Object> faces = (ArrayList<Object>) map.get("faces");
        LinkedTreeMap<String, Object> treeMap = (LinkedTreeMap<String, Object>)faces.get(0);
        return treeMap.get("face_token").toString();
    }
}