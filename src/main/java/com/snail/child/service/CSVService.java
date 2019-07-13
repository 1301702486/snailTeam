package com.snail.child.service;

import com.snail.child.model.Address;
import com.snail.child.model.ChildFindParent;
import com.snail.child.model.ParentFindChild;
import com.snail.child.repository.ChildFindParentRepository;
import com.snail.child.repository.ParentFindChildRepository;
import com.snail.child.service.faceRecog.FaceDetectService;
import com.snail.child.service.faceRecog.FaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Author: 郭瑞景
 * Date: 2019/7/7
 * Description: No Description
 */
@Service
public class CSVService {
    @Autowired
    ParentFindChildRepository parentFindChildRepository;

    @Autowired
    ChildFindParentRepository childFindParentRepository;

    @Autowired
    FaceDetectService detectService;

    @Autowired
    FaceService faceService;

    public ArrayList<String[]> readCSV(String path) {
        ArrayList<String[]> list = new ArrayList<>();
        try {
            FileInputStream in = new FileInputStream(path);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, "gb18030"));
            reader.readLine();   // IOException
            String line = null;
            while ((line = reader.readLine()) != null) {
                String[] item = line.split(",");   // CSV格式文件为逗号分隔符文件，这里根据逗号切分

                // int value = Integer.parseInt(last);   //如果是数值，可以转化为数值

                list.add(item);
                // System.out.println(last);
            }
            System.out.println("从CSV中读取到的数据：" + list);
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 把爬下来的信息添加到家寻宝贝数据库
     *
     * @param csvFilePath csv文件路径
     */
//    @Transactional
    public void insertToPFC(String csvFilePath) {
        ArrayList<String[]> arrayList = readCSV(csvFilePath);
        for (String[] itemArray : arrayList) {
            if (itemArray.length != 9) {
                continue;
            }
            if (itemArray[0] != null && itemArray[1] != null) {
                ParentFindChild parentFindChild = new ParentFindChild();
                parentFindChild.setName(itemArray[0]);
                parentFindChild.setGender(itemArray[1]);
                if (itemArray[2] != null) {
                    parentFindChild.setBirthday(stringToDate(itemArray[2]));
                }
                if (itemArray[3] != null) {
                    parentFindChild.setMissingDate(stringToDate(itemArray[3]));
                }
                if (itemArray[4] != null) {
                    Address missingAddr = new Address();
                    missingAddr.setProvince(itemArray[4]);
                    if (itemArray[5] != null) {
                        missingAddr.setCity(itemArray[5]);
                    }
                    if (itemArray[6] != null && itemArray[6].length() <= 255) {
                        missingAddr.setDetail(itemArray[6]);
                    }
                    parentFindChild.setMissingAddress(missingAddr);
                }
                if (itemArray[7] != null && itemArray[7].length() <= 255) {
                    parentFindChild.setDetail(itemArray[7]);
                }
                if (itemArray[8] != null) {
                    String imageUrl = itemArray[8];
                    String[] array = imageUrl.split("/image/");
                    String fileName = array[1];
                    String path = "C:/images/" + fileName;
                    File file = new File(path);
                    if (!file.exists()) {
                        continue;
                    }
                    if (file.length() >= 2097152) {
                        continue;
                    }
                    try {
                        BufferedImage image = ImageIO.read(new FileInputStream(file));
                        if (!(image.getWidth() >= 48 && image.getWidth() <= 4096)) {
                            continue;
                        }
                        if (!(image.getHeight() >= 48 && image.getHeight() <= 4096)) {
                            continue;
                        }
                        parentFindChild.setPhoto(itemArray[8]);
                        String faceToken = detectService.getFaceToken(parentFindChild.getPhoto());
                        if (faceToken.equals("No face token")) {
                            continue;
                        }
                        parentFindChild.setFaceToken(faceToken);
                        faceService.addToFaceSet(faceToken, "pfcFaceSet");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                parentFindChildRepository.save(parentFindChild);
            }
        }
    }

    /**
     * 把爬下来的信息添加到宝贝寻家数据库
     *
     * @param csvFilePath csv文件路径
     */
    @Transactional
    public void insertToCFP(String csvFilePath) {
        ArrayList<String[]> arrayList = readCSV(csvFilePath);
        for (String[] itemArray : arrayList) {
            if (itemArray.length != 9) {
                continue;
            }
            ChildFindParent childFindParent = new ChildFindParent();
            childFindParent.setName(itemArray[0]);
            childFindParent.setGender(itemArray[1]);
            childFindParent.setBirthday(stringToDate(itemArray[2]));
            childFindParent.setMissingDate(stringToDate(itemArray[3]));
            Address missingAddr = new Address();
            missingAddr.setProvince(itemArray[4]);
            missingAddr.setCity(itemArray[5]);
            if (itemArray[6].length() <= 255) {
                missingAddr.setDetail(itemArray[6]);
            }
            childFindParent.setMissingAddress(missingAddr);
            if (itemArray[7].length() <= 255) {
                childFindParent.setDetail(itemArray[7]);
            }
            if (itemArray[8] != null) {
                String imageUrl = itemArray[8];
                String[] array = imageUrl.split("/image/");
                String fileName = array[1];
                String path = "C:/images/" + fileName;
                File file = new File(path);
                if (!file.exists()) {
                    continue;
                }
                if (file.length() >= 2097152) {
                    continue;
                }
                try {
                    BufferedImage image = ImageIO.read(new FileInputStream(file));
                    if (!(image.getWidth() >= 48 && image.getWidth() <= 4096)) {
                        continue;
                    }
                    if (!(image.getHeight() >= 48 && image.getHeight() <= 4096)) {
                        continue;
                    }
                    childFindParent.setPhoto(itemArray[8]);
                    String faceToken = detectService.getFaceToken(childFindParent.getPhoto());
                    if (faceToken.equals("No face token")) {
                        continue;
                    }
                    childFindParent.setFaceToken(faceToken);
                    faceService.addToFaceSet(faceToken, "childrenFaceSet");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            childFindParentRepository.save(childFindParent);
        }
    }


    private Date stringToDate(String dateStr) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        Date date = null;
        try {
            date = formatter.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}
