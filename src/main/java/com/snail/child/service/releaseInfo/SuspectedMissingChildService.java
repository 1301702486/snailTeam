package com.snail.child.service.releaseInfo;

import com.snail.child.enm.MessageGuo;
import com.snail.child.enm.MessageXin;
import com.snail.child.model.*;
import com.snail.child.repository.ParentFindChildRepository;
import com.snail.child.repository.SuspectedMissingChildRepository;
import com.snail.child.repository.UserRepository;
import com.snail.child.service.faceRecog.FaceDetectService;
import com.snail.child.service.faceRecog.FaceService;
import com.snail.child.service.user.UserService;
import com.snail.child.utils.PhotoUtils;
import com.snail.child.utils.ResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

/**
 * User: ZhangXinrui
 * Date: 2019/6/29
 * Description: No Description
 */

@Service
public class SuspectedMissingChildService {
    @Autowired
    SuspectedMissingChildRepository suspectedMissingChildRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @Autowired
    FaceService faceService;

    @Autowired
    FaceDetectService detectService;

    @Autowired
    ParentFindChildRepository parentFindChildRepository;


    /**
     * 添加疑似流浪儿童信息
     *
     * @param emailAddr             用户id
     * @param suspectedMissingChild 接收发布信息
     * @param file                  用户上传的图片
     * @return 成功: code=0, data=匹配结果  失败: code!=0
     */
    @Transactional
    public Result addSuspectedMissingChild(SuspectedMissingChild suspectedMissingChild, String emailAddr, MultipartFile file) {
        User user = userRepository.findUserByEmailAddr(emailAddr);
        String imageUrl = PhotoUtils.uploadPhoto(file);
        if (!file.isEmpty()) {
            suspectedMissingChild.setPhoto(imageUrl);
            // 获取上传的图片的face_token
            String faceToken = detectService.getFaceToken(imageUrl);
            if (faceToken.equals("No face token")) {
                return ResultUtils.send(MessageGuo.NO_FACE_DETECTED);
            }
            suspectedMissingChild.setFaceToken(faceToken);

            user.addSuspectedMissingChild(suspectedMissingChild);
            userRepository.save(user);
            // 获取检索结果
            ArrayList<ParentFindChild> results = getMatchResults(faceToken);
            // 检索完成后加到childrenFaceSet
            faceService.addToFaceSet(faceToken, "childrenFaceSet");

            if (results != null) {
                return ResultUtils.send(MessageXin.SUCCESS, getMatchResults(faceToken));
            } else {
                return ResultUtils.send(MessageXin.NO_MATCH_RESULT);
            }
        } else {
            return ResultUtils.send(MessageXin.NO_PHOTO);
        }
    }

    /**
     * 查询疑似流浪儿童信息
     *
     * @param suspectedMissingChild 查询条件
     * @param pageable              分页
     * @return 查询结果
     */
    public Result selectSuspectedMissingChild(SuspectedMissingChild suspectedMissingChild, Pageable pageable) {
        if (suspectedMissingChild != null) {
            Specification specification = new Specification() {
                @Override
                public Predicate toPredicate(Root root, CriteriaQuery criteriaQuery, CriteriaBuilder criteriaBuilder) {

                    List<Predicate> predicateList = new ArrayList<>();

                    if (suspectedMissingChild != null) {
                        if (!StringUtils.isEmpty(suspectedMissingChild.getHeight())) {
                            predicateList.add(criteriaBuilder.equal(root.get("height"), suspectedMissingChild.getHeight()));
                        }
                        if (!StringUtils.isEmpty(suspectedMissingChild.getMissingAddress())) {
                            Join<SuspectedMissingChild, Address> join = root.join("missingAddress", JoinType.LEFT);
                            if (!StringUtils.isEmpty(suspectedMissingChild.getMissingAddress().getProvince())) {
                                predicateList.add(criteriaBuilder.like(root.get("missingAddress").get("province"), suspectedMissingChild.getMissingAddress().getProvince()));
                                if (!StringUtils.isEmpty(suspectedMissingChild.getMissingAddress().getCity())) {
                                    predicateList.add(criteriaBuilder.like(root.get("missingAddress").get("city"), suspectedMissingChild.getMissingAddress().getCity()));
                                    if (!StringUtils.isEmpty(suspectedMissingChild.getMissingAddress().getDistrict())) {
                                        predicateList.add(criteriaBuilder.like(root.get("missingAddress").get("district"), suspectedMissingChild.getMissingAddress().getDistrict()));
                                    }
                                }
                            }
                        }
                    }
                    Predicate[] predicate = new Predicate[predicateList.size()];
                    criteriaQuery.where(predicateList.toArray(predicate));
                    return criteriaQuery.getRestriction();
                }
            };
            Integer totalPage = suspectedMissingChildRepository.findAll(specification).size() / pageable.getPageSize();
            Page<SuspectedMissingChild> page = suspectedMissingChildRepository.findAll(specification, pageable);
            return ResultUtils.send(MessageXin.SUCCESS, totalPage, page);
        } else {
            Integer totalPage = suspectedMissingChildRepository.findAll().size() / pageable.getPageSize();
            return ResultUtils.send(MessageXin.SUCCESS, suspectedMissingChildRepository.findAll(pageable));
        }
    }


    /**
     * 根据face_token找到所有匹配结果
     *
     * @param faceToken 目标face_token
     * @return 匹配的发布信息
     * @author 郭瑞景
     */
    public ArrayList<ParentFindChild> getMatchResults(String faceToken) {
        // 从pfcFaceSet中获取人脸检索结果的face_tokens
        ArrayList<String> faceTokens = faceService.getFaceTokens(faceToken, "pfcFaceSet");
        // 根据face_tokens找到已存在的parent find child发布信息
        ArrayList<ParentFindChild> results = new ArrayList<>();
        for (String token : faceTokens) {
            ParentFindChild parentFindChild = parentFindChildRepository.findParentFindChildByFaceToken(token);
            if (parentFindChild != null) {
                results.add(parentFindChild);
            }
        }
        return results;
    }

    /**
     * 根据匹配结果的发布id查找对应的发布信息
     * id的值由前端传递过来
     * 最多有5个匹配结果, 少于5个其余id值为-1
     *
     * @param id1
     * @param id2
     * @param id3
     * @param id4
     * @param id5
     * @return 根据id查到的发布信息
     */
    public Result getSmcMatchResult(Integer id1, Integer id2, Integer id3, Integer id4, Integer id5) {
        ArrayList<ParentFindChild> results = new ArrayList<>();
        ParentFindChild result1 = parentFindChildRepository.findParentFindChildById(id1);
        ParentFindChild result2 = parentFindChildRepository.findParentFindChildById(id2);
        ParentFindChild result3 = parentFindChildRepository.findParentFindChildById(id3);
        ParentFindChild result4 = parentFindChildRepository.findParentFindChildById(id4);
        ParentFindChild result5 = parentFindChildRepository.findParentFindChildById(id5);
        if (result1 != null) {
            results.add(result1);
        }
        if (result2 != null) {
            results.add(result2);
        }
        if (result3 != null) {
            results.add(result3);
        }
        if (result4 != null) {
            results.add(result4);
        }
        if (result5 != null) {
            results.add(result5);
        }
        return ResultUtils.send(MessageXin.SUCCESS, results);
    }

    public SuspectedMissingChild getSmcById(Integer id) {
        return suspectedMissingChildRepository.findSuspectedMissingChildById(id);
    }
}
