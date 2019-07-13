package com.snail.child.service.releaseInfo;

import com.snail.child.enm.MessageGuo;
import com.snail.child.enm.MessageXin;

import com.snail.child.model.*;
import com.snail.child.repository.ChildFindParentRepository;
import com.snail.child.repository.ParentFindChildRepository;
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
 * Date: 2019/6/28
 * Description: No Description
 */

@Service
public class ChildFindParentService {

    @Autowired
    ChildFindParentRepository childFindParentRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    FaceDetectService detectService;

    @Autowired
    FaceService faceService;

    @Autowired
    UserService userService;

    @Autowired
    ParentFindChildRepository parentFindChildRepository;


    /**
     * 发布宝贝寻家信息
     *
     * @param childFindParent 接收前端传过来的发布信息
     * @param emailAddr       用户id
     * @param file            用户上传的图片
     * @return 成功: code=0, data=匹配结果  失败: code!=0
     */
    @Transactional
    public Result addChildFindParent(ChildFindParent childFindParent, String emailAddr, MultipartFile file) {
        User user = userRepository.findUserByEmailAddr(emailAddr);
        if (user.getChildFindParent() == null) {
            String imageUrl = PhotoUtils.uploadPhoto(file);
            childFindParent.setPhoto(imageUrl);
            // 获取上传的图片的face_token
            String faceToken = detectService.getFaceToken(imageUrl);
            if (faceToken.equals("No face token")) {
                return ResultUtils.send(MessageGuo.NO_FACE_DETECTED);
            }
            childFindParent.setFaceToken(faceToken);

            user.setChildFindParent(childFindParent);
            childFindParentRepository.save(childFindParent);
            userRepository.save(user);
            // 获取检索结果
            ArrayList<ParentFindChild> results = getMatchResults(faceToken);
            // 检索完成后加到childrenFaceSet
            faceService.addToFaceSet(faceToken, "childrenFaceSet");

            if (results != null) {
                return ResultUtils.send(MessageXin.SUCCESS, results);
            } else {
                return ResultUtils.send(MessageXin.NO_MATCH_RESULT);
            }
        } else {
            return ResultUtils.send(MessageXin.CHILDFINDPARENT_HAS_EXIST);
        }
    }

    /**
     * 查询宝贝寻家信息
     *
     * @param childFindParent 查询条件
     * @param page            分页
     * @return 查询结果
     */
    public Result selectChildFindParent(ChildFindParent childFindParent, Pageable page) {
        if (childFindParent != null) {
            Specification<ChildFindParent> specification = new Specification<ChildFindParent>() {
                @Override
                public Predicate toPredicate(Root<ChildFindParent> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                    List<Predicate> predicateList = new ArrayList<>();
                    if (!StringUtils.isEmpty(childFindParent.getName())) {
                        predicateList.add(criteriaBuilder.like(root.get("name"), childFindParent.getName()));
                    }
                    if (!StringUtils.isEmpty(childFindParent.getGender())) {
                        predicateList.add(criteriaBuilder.like(root.get("gender"), childFindParent.getGender()));
                    }
                    if (!StringUtils.isEmpty(childFindParent.getMissingDate())) {
                        predicateList.add(criteriaBuilder.equal(root.get("missingDate").as(Date.class), childFindParent.getMissingDate()));
                    }
                    if (!StringUtils.isEmpty(childFindParent.getHomeAddress())) {
                        Join<ChildFindParent, Address> join = root.join("homeAddress", JoinType.LEFT);
                        if (!StringUtils.isEmpty(childFindParent.getHomeAddress().getProvince())) {
                            predicateList.add(criteriaBuilder.like(root.get("homeAddress").get("province"), childFindParent.getHomeAddress().getProvince()));
                            if (!StringUtils.isEmpty(childFindParent.getHomeAddress().getCity())) {
                                predicateList.add(criteriaBuilder.like(root.get("homeAddress").get("city"), childFindParent.getHomeAddress().getCity()));
                                if (!StringUtils.isEmpty(childFindParent.getHomeAddress().getDistrict())) {
                                    predicateList.add(criteriaBuilder.like(root.get("homeAddress").get("district"), childFindParent.getHomeAddress().getDistrict()));
                                }
                            }
                        }
                    }
                    if (!StringUtils.isEmpty(childFindParent.getMissingAddress())) {
                        Join<ChildFindParent, Address> join = root.join("missingAddress", JoinType.LEFT);
                        if (!StringUtils.isEmpty(childFindParent.getMissingAddress().getProvince())) {
                            predicateList.add(criteriaBuilder.like(root.get("missingAddr").get("province"), childFindParent.getMissingAddress().getCity()));
                            if (!StringUtils.isEmpty(childFindParent.getMissingAddress().getCity())) {
                                predicateList.add(criteriaBuilder.like(root.get("missingAddress").get("city"), childFindParent.getMissingAddress().getCity()));
                                if (!StringUtils.isEmpty(childFindParent.getMissingAddress().getDistrict())) {
                                    predicateList.add(criteriaBuilder.like(root.get("missingAddress").get("district"), childFindParent.getMissingAddress().getDistrict()));
                                }
                            }
                        }
                    }

                    Predicate[] predicate = new Predicate[predicateList.size()];
                    criteriaQuery.where(predicateList.toArray(predicate));
                    return criteriaQuery.getRestriction();
                }
            };
            Integer totalPage = childFindParentRepository.findAll(specification).size() / page.getPageSize();
            Page<ChildFindParent> p = childFindParentRepository.findAll(specification, page);
            return ResultUtils.send(MessageXin.SUCCESS, totalPage, p);
        } else {
            Integer totalPage = childFindParentRepository.findAll().size() / page.getPageSize();
            return ResultUtils.send(MessageXin.SUCCESS, totalPage, childFindParentRepository.findAll(page));
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
    public Result getCfpMatchResult(Integer id1, Integer id2, Integer id3, Integer id4, Integer id5) {
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

    public ChildFindParent getCfpById(Integer id) {
        return childFindParentRepository.findChildFindParentById(id);
    }
}