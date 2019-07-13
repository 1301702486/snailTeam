package com.snail.child.service.releaseInfo;


import com.snail.child.enm.MessageGuo;
import com.snail.child.enm.MessageXin;
import com.snail.child.model.*;
import com.snail.child.repository.ChildFindParentRepository;
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
import java.util.Date;
import java.util.List;

/**
 * User: ZhangXinrui
 * Date: 2019/6/27
 * Description: No Description
 */

@Service
public class ParentFindChildService {

    @Autowired
    ParentFindChildRepository parentFindChildRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userService;
    @Autowired
    FaceDetectService detectService;
    @Autowired
    FaceService faceService;
    @Autowired
    ChildFindParentRepository childFindParentRepository;
    @Autowired
    SuspectedMissingChildRepository suspectedMissingChildRepository;

    /**
     * 发布家长找孩子的信息
     *
     * @param emailAddr       发布信息的用户id
     * @param parentFindChild 发布内容
     * @param file            上传的图片
     * @return 成功: code=0, data=匹配结果  失败: code!=0
     */
    @Transactional
    public Result addParentFindChild(ParentFindChild parentFindChild, String emailAddr, MultipartFile file) {
        User user = userRepository.findUserByEmailAddr(emailAddr);

        if (user.getParentFindChild() == null && !file.isEmpty()) {
            String imageUrl = PhotoUtils.uploadPhoto(file);
            parentFindChild.setPhoto(imageUrl);
            // 获取上传的图片的face_token
            String faceToken = detectService.getFaceToken(imageUrl);
            if (faceToken.equals("No face token")) {
                return ResultUtils.send(MessageGuo.NO_FACE_DETECTED);
            }
            parentFindChild.setFaceToken(faceToken);

            user.setParentFindChild(parentFindChild);
            userRepository.save(user);
            // 获取检索结果
            ArrayList<Object> results = getMatchResults(faceToken);
            // 检索完成后加到pfcFaceSet
            faceService.addToFaceSet(faceToken, "pfcFaceSet");

            if (results != null) {
                return ResultUtils.send(MessageXin.SUCCESS, results);
            } else {
                return ResultUtils.send(MessageXin.NO_MATCH_RESULT);
            }
        } else {
            return ResultUtils.send(MessageXin.PARENTFINDCHILD_HAS_EXIST);
        }
    }

    /**
     * 查询家长找孩子信息
     *
     * @param parentFindChild 查询条件
     * @param page            分页
     * @return 查询结果
     */
    public Result selectParentFindChild(ParentFindChild parentFindChild, Pageable page) {
        if (parentFindChild != null) {
            Specification<ParentFindChild> specification = new Specification<ParentFindChild>() {
                @Override
                public Predicate toPredicate(Root<ParentFindChild> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                    List<Predicate> predicatesList = new ArrayList<>();
                    if (!StringUtils.isEmpty(parentFindChild.getName())) {
                        predicatesList.add(cb.like(root.get("name"), "%" + parentFindChild.getName() + "%"));
                    }
                    if (!StringUtils.isEmpty(parentFindChild.getGender())) {
                        predicatesList.add(cb.like(root.get("gender"), "%" + parentFindChild.getGender() + "%"));
                    }
                    if (!StringUtils.isEmpty(parentFindChild.getBirthday())) {
                        predicatesList.add(cb.equal(root.get("missingDate").as(Date.class), parentFindChild.getMissingDate()));
                    }
                    if (!StringUtils.isEmpty((parentFindChild.getHomeAddress()))) {
                        Join<ParentFindChild, Address> join = root.join("homeAddress", JoinType.LEFT);
                        if (!StringUtils.isEmpty(parentFindChild.getHomeAddress().getProvince())) {
                            predicatesList.add(cb.like(root.get("homeAddress").get("province"), parentFindChild.getMissingAddress().getProvince()));
                            if (!StringUtils.isEmpty(parentFindChild.getHomeAddress().getCity())) {
                                predicatesList.add(cb.like(root.get("homeAddress").get("city"), parentFindChild.getMissingAddress().getCity()));
                                if (!StringUtils.isEmpty(parentFindChild.getHomeAddress().getDistrict())) {
                                    predicatesList.add(cb.like(root.get("homeAddress").get("district"), parentFindChild.getMissingAddress().getDistrict()));
                                }
                            }
                        }
                    }
                    if (!StringUtils.isEmpty(parentFindChild.getMissingAddress())) {
                        if (!StringUtils.isEmpty(parentFindChild.getMissingAddress().getProvince())) {
                            Join<ParentFindChild, Address> join = root.join("missingAddress", JoinType.LEFT);
                            predicatesList.add(cb.like(root.get("missingAddress").get("province"), parentFindChild.getMissingAddress().getProvince()));
                            if (!StringUtils.isEmpty(parentFindChild.getMissingAddress().getCity())) {
                                predicatesList.add(cb.like(root.get("missingAddress").get("city"), parentFindChild.getMissingAddress().getCity()));
                                if (!StringUtils.isEmpty(parentFindChild.getMissingAddress().getDistrict())) {
                                    predicatesList.add(cb.like(root.get("missingAddress").get("district"), parentFindChild.getMissingAddress().getDistrict()));
                                }
                            }
                        }
                    }
                    Predicate[] predicate = new Predicate[predicatesList.size()];
                    query.where(predicatesList.toArray(predicate));
                    return query.getRestriction();
                }
            };
            Page<ParentFindChild> p = parentFindChildRepository.findAll(specification, page);

            Integer totalPage = parentFindChildRepository.findAll(specification).size() / page.getPageSize();
            return ResultUtils.send(MessageXin.SUCCESS, totalPage, p);
        } else {
            Integer totalPage = parentFindChildRepository.findAll().size() / page.getPageSize();
            return ResultUtils.send(MessageXin.SUCCESS, totalPage, parentFindChildRepository.findAll(page));
        }
    }


    /**
     * 根据face_token找到所有匹配结果
     *
     * @param faceToken 目标face_token
     * @return 匹配的发布信息
     * @author 郭瑞景
     */
    public ArrayList<Object> getMatchResults(String faceToken) {
        // 从childrenFaceSet中获取人脸检索结果的face_tokens
        ArrayList<String> faceTokens = faceService.getFaceTokens(faceToken, "childrenFaceSet");
        // 根据face_tokens找到已存在的child find parent和suspected missing child发布信息
        ArrayList<Object> results = new ArrayList<>();
        for (String token : faceTokens) {
            ChildFindParent childFindParent = childFindParentRepository.findChildFindParentByFaceToken(token);
            if (childFindParent != null) {
                results.add(childFindParent);
            }
            SuspectedMissingChild suspectedMissingChild = suspectedMissingChildRepository.findSuspectedMissingChildByFaceToken(token);
            if (suspectedMissingChild != null) {
                results.add(suspectedMissingChild);
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
    public Result getPfcMatchResult(Integer id1, Integer id2, Integer id3, Integer id4, Integer id5) {
        ArrayList<Object> results = new ArrayList<>();
        ChildFindParent c1 = childFindParentRepository.findChildFindParentById(id1);
        SuspectedMissingChild s1 = suspectedMissingChildRepository.findSuspectedMissingChildById(id1);
        ChildFindParent c2 = childFindParentRepository.findChildFindParentById(id2);
        SuspectedMissingChild s2 = suspectedMissingChildRepository.findSuspectedMissingChildById(id2);
        ChildFindParent c3 = childFindParentRepository.findChildFindParentById(id3);
        SuspectedMissingChild s3 = suspectedMissingChildRepository.findSuspectedMissingChildById(id3);
        ChildFindParent c4 = childFindParentRepository.findChildFindParentById(id4);
        SuspectedMissingChild s4 = suspectedMissingChildRepository.findSuspectedMissingChildById(id4);
        ChildFindParent c5 = childFindParentRepository.findChildFindParentById(id5);
        SuspectedMissingChild s5 = suspectedMissingChildRepository.findSuspectedMissingChildById(id5);
        if (c1 != null) {
            results.add(c1);
        }
        if (c2 != null) {
            results.add(c2);
        }
        if (c3 != null) {
            results.add(c3);
        }
        if (c4 != null) {
            results.add(c4);
        }
        if (c5 != null) {
            results.add(c5);
        }
        if (s1 != null) {
            results.add(s1);
        }
        if (s2 != null) {
            results.add(s2);
        }
        if (s3 != null) {
            results.add(s3);
        }
        if (s4 != null) {
            results.add(s4);
        }
        if (s5 != null) {
            results.add(s5);
        }
        return ResultUtils.send(MessageXin.SUCCESS, results);
    }

    public ParentFindChild getPfcById(Integer id) {
        return parentFindChildRepository.findParentFindChildById(id);
    }
}
