package com.snail.child.service.releaseInfo;

import com.snail.child.enm.MessageXin;

import com.snail.child.model.*;
import com.snail.child.repository.ChildFindParentRepository;
import com.snail.child.repository.ParentFindChildRepository;
import com.snail.child.repository.UserRepository;
import com.snail.child.service.faceRecog.FaceDetectService;
import com.snail.child.service.faceRecog.FaceService;
import com.snail.child.service.user.UserUpdateService;
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
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

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
    UserUpdateService userService;

    @Autowired
    ParentFindChildRepository parentFindChildRepository;


    private final String outerId = "pfcFaceSet";

    /**
     * 添加孩子寻找父母的信息
     *
     * @param childFindParent
     * @param emailAddr
     * @param file
     * @return
     */
    public Result addChildFindParent(ChildFindParent childFindParent, String emailAddr, MultipartFile file) {
        User user = userRepository.findUserByEmailAddr(emailAddr);
        if (!userService.infoComplete(user)) {
            return ResultUtils.send(MessageXin.INFO_INCOMPLETE);
        }
        if (user.getChildFindParent() == null) {
            String imageUrl = PhotoUtils.uploadPhoto(file);
            childFindParent.setPhoto(imageUrl);
            // 获取上传的图片的face_token
            String faceToken = detectService.getFaceToken(imageUrl);
            childFindParent.setFaceToken(faceToken);

            user.setChildFindParent(childFindParent);
            childFindParentRepository.save(childFindParent);
            userRepository.save(user);

            ArrayList<ParentFindChild> results = getMatchResults(faceToken);
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
     * 删除家长寻找孩子的发布信息
     *
     * @param emailAddr
     * @return
     */
    @Transactional
    public Result deleteChildFindParent(String emailAddr) {
        User user = userRepository.findUserByEmailAddr(emailAddr);
        ChildFindParent childFindParent = user.getChildFindParent();
        if (childFindParent != null) {
            // Remove face token from FaceSet
            faceService.removeFromFaceSet(childFindParent.getFaceToken(), outerId);

            user.setChildFindParent(null);
            childFindParentRepository.delete(childFindParent);
            return ResultUtils.send(MessageXin.SUCCESS, userRepository.save(user));
        } else {
            return ResultUtils.send(MessageXin.CHILDFINDPARENT_NOT_EXIST);
        }
    }

//    /**
//     * 更新孩子找父母的信息
//     *
//     * @param childFindParent
//     * @param file
//     * @return
//     */
//    public Result updateChildFindParent(ChildFindParent childFindParent, MultipartFile file) {
//        if (!file.isEmpty()) {
//            childFindParent.setPhoto(PhotoUtils.uploadPhoto(file));
//        }
//        return ResultUtils.send(MessageXin.SUCCESS, childFindParentRepository.save(childFindParent));
//    }

    /**
     * 查询孩子找父母信息
     *
     * @param childFindParent
     * @param page
     * @return
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
                            }
                        }
                    }
                    if (!StringUtils.isEmpty(childFindParent.getMissingAddress())) {
                        Join<ChildFindParent, Address> join = root.join("missingAddress", JoinType.LEFT);
                        if (!StringUtils.isEmpty(childFindParent.getMissingAddress().getProvince())) {
                            predicateList.add(criteriaBuilder.like(root.get("missingAddr").get("province"), childFindParent.getMissingAddress().getCity()));
                            if (!StringUtils.isEmpty(childFindParent.getMissingAddress().getCity())) {
                                predicateList.add(criteriaBuilder.like(root.get("missingAddress").get("city"), childFindParent.getMissingAddress().getCity()));
                            }
                        }
                    }

                    Predicate[] predicate = new Predicate[predicateList.size()];
                    criteriaQuery.where(predicateList.toArray(predicate));
                    return criteriaQuery.getRestriction();
                }
            };
            Page<ChildFindParent> p = childFindParentRepository.findAll(specification, page);
            return ResultUtils.send(MessageXin.SUCCESS, p);
        } else {
            return ResultUtils.send(MessageXin.SUCCESS, childFindParentRepository.findAll(page));
        }
    }

    /**
     * 找到所有匹配结果
     *
     * @param faceToken
     * @return
     */
    public ArrayList<ParentFindChild> getMatchResults(String faceToken) {
        // 从pfcFaceSet中获取人脸检索结果的face_tokens
        ArrayList<String> faceTokens = faceService.getfaceTokens(faceToken, outerId);
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

}