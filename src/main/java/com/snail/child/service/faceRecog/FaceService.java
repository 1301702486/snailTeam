package com.snail.child.service.faceRecog;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.snail.child.enm.MessageXin;
import com.snail.child.model.ChildFindParent;
import com.snail.child.model.ParentFindChild;
import com.snail.child.model.Result;
import com.snail.child.model.SuspectedMissingChild;
import com.snail.child.repository.ChildFindParentRepository;
import com.snail.child.repository.ParentFindChildRepository;
import com.snail.child.repository.SuspectedMissingChildRepository;
import com.snail.child.utils.ResultUtils;
import javafx.scene.Parent;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.http.HttpEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: 郭瑞景
 * Date: 2019/7/3
 * Description: No Description
 */

@Service
public class FaceService {

    private final String apiKey = "GTvcIl8x4HX25ytluaDi7eicC7rv2Ad_";
    private final String apiSecret = "7CMHtPRjOD14UyEft6yb_-EJfDVaJjnj";

    /**
     * 发送 post请求访问本地应用并根据传递参数不同返回不同结果
     *
     * @param formparams 请求参数
     * @param url        请求接口
     * @return 请求结果
     */
    public String post(List<BasicNameValuePair> formparams, String url) {
        // 创建默认的httpClient实例.
        CloseableHttpClient httpclient = HttpClients.createDefault();
        // 创建httppost
        HttpPost httppost = new HttpPost(url);
        UrlEncodedFormEntity uefEntity;
        String faceSetResult = null;
        try {
            uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
            httppost.setEntity(uefEntity);
            System.out.println("executing request " + httppost.getURI());
            CloseableHttpResponse response = httpclient.execute(httppost);
            try {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    faceSetResult = EntityUtils.toString(entity, "UTF-8");
                    System.out.println("--------------------------------------");
                    System.out.println("Response content: " + faceSetResult);
                    System.out.println("--------------------------------------");
                }
            } finally {
                response.close();
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭连接,释放资源
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return faceSetResult;
    }

    /**
     * 创建一个FaceSet
     *
     * @param outerId 自定义FaceSet标识
     * @return 请求结果
     */
    public String createFaceSet(String outerId) {
        String urlCreate = "https://api-cn.faceplusplus.com/facepp/v3/faceset/create";
        // 创建参数队列
        List<BasicNameValuePair> formParams = new ArrayList<>();
        formParams.add(new BasicNameValuePair("api_key", apiKey));
        formParams.add(new BasicNameValuePair("api_secret", apiSecret));
        formParams.add(new BasicNameValuePair("outer_id", outerId));
        // 发送请求
        return post(formParams, urlCreate);
    }

    /**
     * 添加face_tokens到FaceSet
     *
     * @param faceTokens 要添加的face_token字符串
     * @param outerId    添加到的FaceSet标识
     * @return 请求结果
     */
    public String addToFaceSet(String faceTokens, String outerId) {
        String urlAdd = "https://api-cn.faceplusplus.com/facepp/v3/faceset/addface";
        // 创建参数队列
        List<BasicNameValuePair> formParams = new ArrayList<>();
        formParams.add(new BasicNameValuePair("api_key", apiKey));
        formParams.add(new BasicNameValuePair("api_secret", apiSecret));
        formParams.add(new BasicNameValuePair("outer_id", outerId));
        formParams.add(new BasicNameValuePair("face_tokens", faceTokens));
        // 发送请求
        return post(formParams, urlAdd);
    }

    /**
     * 删除一个FaceSet
     *
     * @param checkEmpty 0: FaceSet不为空时仍然删除; 1: FaceSet不为空时不删除
     * @param outerId    要删除的FaceSet标识
     * @return 请求结果
     */
    public String deleteAFaceSet(Integer checkEmpty, String outerId) {
        String urlDelete = "https://api-cn.faceplusplus.com/facepp/v3/faceset/delete";
        List<BasicNameValuePair> formParams = new ArrayList<>();
        formParams.add(new BasicNameValuePair("api_key", apiKey));
        formParams.add(new BasicNameValuePair("api_secret", apiSecret));
        formParams.add(new BasicNameValuePair("outer_id", outerId));
        if (checkEmpty != null) {
            formParams.add(new BasicNameValuePair("check_empty", checkEmpty.toString()));
        }
        return post(formParams, urlDelete);
    }

    /**
     * 从FaceSet中删除一个face_token
     *
     * @param faceTokens 要删除的face_token
     * @param outerId    从哪个FaceSet删除
     * @return 请求结果
     */
    public String removeFromFaceSet(String faceTokens, String outerId) {
        String urlRemove = "https://api-cn.faceplusplus.com/facepp/v3/faceset/removeface";
        List<BasicNameValuePair> formParams = new ArrayList<>();
        formParams.add(new BasicNameValuePair("api_key", apiKey));
        formParams.add(new BasicNameValuePair("api_secret", apiSecret));
        formParams.add(new BasicNameValuePair("outer_id", outerId));
        formParams.add(new BasicNameValuePair("face_tokens", faceTokens));
        return post(formParams, urlRemove);
    }


    /**
     * 人脸检索
     * 被getFaceTokens调用
     *
     * @param targetFaceToken 目标face_token
     * @param outerId         目标FaceSet
     * @return 请求结果
     */
    private String faceSearch(String targetFaceToken, String outerId) {
        String urlCompare = "https://api-cn.faceplusplus.com/facepp/v3/search";
        // 创建参数队列
        List<BasicNameValuePair> formParams = new ArrayList<>();
        formParams.add(new BasicNameValuePair("api_key", apiKey));
        formParams.add(new BasicNameValuePair("api_secret", apiSecret));
        formParams.add(new BasicNameValuePair("face_token", targetFaceToken));
        formParams.add(new BasicNameValuePair("outer_id", outerId));
        formParams.add(new BasicNameValuePair("return_result_count", "5"));
        // 发送请求
        return post(formParams, urlCompare);
    }

    /**
     * 获取人脸检索结果的face_token数组
     *
     * @param targetFaceToken 目标face_token
     * @param outerId         目标FaceSet
     * @return 和目标face_token匹配的(结果)face_tokens
     */
    public ArrayList<String> getFaceTokens(String targetFaceToken, String outerId) {
        String resultStr = faceSearch(targetFaceToken, outerId);
        ArrayList<String> faceTokens = new ArrayList<>();
        Map<String, Object> map = new Gson().fromJson(resultStr, new TypeToken<HashMap<String, Object>>() {
        }.getType());
        ArrayList<Object> results = (ArrayList<Object>) map.get("results");
        for (Object obj : results) {
            LinkedTreeMap<String, Object> treeMap = (LinkedTreeMap<String, Object>) obj;
            faceTokens.add(treeMap.get("face_token").toString());
        }
        return faceTokens;
    }

    /**
     * 获取FaceSet详情
     *
     * @param outerId FaceSet标识
     * @return 请求结果
     */
    public String getDetail(String outerId) {
        String urlCompare = "https://api-cn.faceplusplus.com/facepp/v3/faceset/getdetail";
        // 创建参数队列
        List<BasicNameValuePair> formParams = new ArrayList<>();
        formParams.add(new BasicNameValuePair("api_key", apiKey));
        formParams.add(new BasicNameValuePair("api_secret", apiSecret));
        formParams.add(new BasicNameValuePair("outer_id", outerId));
        // 发送请求
        return post(formParams, urlCompare);
    }
}
