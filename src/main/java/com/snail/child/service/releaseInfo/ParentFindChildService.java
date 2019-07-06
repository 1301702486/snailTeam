package com.snail.child.service.releaseInfo;


import com.snail.child.enm.MessageXin;
import com.snail.child.model.*;
import com.snail.child.repository.ChildFindParentRepository;
import com.snail.child.repository.ParentFindChildRepository;
import com.snail.child.repository.SuspectedMissingChildRepository;
import com.snail.child.repository.UserRepository;
import com.snail.child.service.faceRecog.FaceDetectService;
import com.snail.child.service.faceRecog.FaceService;
import com.snail.child.service.user.UserUpdateService;
import com.snail.child.utils.PhotoUtils;
import com.snail.child.utils.ResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.criteria.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

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
    UserUpdateService userService;
    @Autowired
    FaceDetectService detectService;
    @Autowired
    FaceService faceService;
    @Autowired
    ChildFindParentRepository childFindParentRepository;
    @Autowired
    SuspectedMissingChildRepository suspectedMissingChildRepository;

    private final String outerId = "childrenFaceSet";

    /**
     * 添加父母寻找孩子
     *
     * @param parentFindChild
     * @param emailAddr
     * @param file
     * @return
     */
    public Result addParentFindChild(ParentFindChild parentFindChild, String emailAddr, MultipartFile file) {
        User user = userRepository.findUserByEmailAddr(emailAddr);
        if (!userService.infoComplete(user)) {
            return ResultUtils.send(MessageXin.INFO_INCOMPLETE);
        }
        if (user.getParentFindChild() == null && !file.isEmpty()) {
            String imageUrl = PhotoUtils.uploadPhoto(file);
            parentFindChild.setPhoto(imageUrl);
            parentFindChildRepository.save(parentFindChild);
            // 获取上传的图片的face_token
            String faceToken = detectService.getFaceToken(imageUrl);
            parentFindChild.setFaceToken(faceToken);

            user.setParentFindChild(parentFindChild);
            parentFindChildRepository.save(parentFindChild);
            userRepository.save(user);
            // TODO: Debug to see what its content is
            ArrayList<Object> results = getMatchResults(faceToken);
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
     * 根据emailAddr删除发布信息
     *
     * @param emailAddr
     * @return
     */
    @Transactional
    public Result deleteParentFindChild(String emailAddr) {
        User user = userRepository.findUserByEmailAddr(emailAddr);
        ParentFindChild parentFindChild = user.getParentFindChild();
        if (parentFindChild != null) {
            // Remove face token from FaceSet
            faceService.removeFromFaceSet(parentFindChild.getFaceToken(), outerId);

            user.setParentFindChild(null);
            parentFindChildRepository.delete(parentFindChild);
            parentFindChildRepository.findAll();
            return ResultUtils.send(MessageXin.SUCCESS, userRepository.save(user));
        } else {
            return ResultUtils.send(MessageXin.PARENTFINDCHILD_NOT_EXIST);
        }
    }

//    /**
//     * 更新家长找孩子的发布信息
//     *
//     * @param parentFindChild
//     * @return
//     */
//    public Result updateParentFindChild(ParentFindChild parentFindChild, MultipartFile file) {
//        if (!file.isEmpty()) {
//            parentFindChild.setPhoto(PhotoUtils.uploadPhoto(file));
//        }
//        return ResultUtils.send(MessageXin.SUCCESS, parentFindChildRepository.save(parentFindChild));
//    }

    /**
     * 查询
     *
     * @param parentFindChild
     * @param page
     * @return
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
                        }
                        if (!StringUtils.isEmpty(parentFindChild.getHomeAddress().getCity())) {
                            predicatesList.add(cb.like(root.get("homeAddress").get("city"), parentFindChild.getMissingAddress().getCity()));
                        }
                    }
                    if (!StringUtils.isEmpty(parentFindChild.getMissingAddress())) {
                        if (!StringUtils.isEmpty(parentFindChild.getMissingAddress().getProvince())) {
                            Join<ParentFindChild, Address> join = root.join("missingAddress", JoinType.LEFT);
                            predicatesList.add(cb.like(root.get("missingAddress").get("province"), parentFindChild.getMissingAddress().getProvince()));
                            if (!StringUtils.isEmpty(parentFindChild.getMissingAddress().getCity())) {
                                predicatesList.add(cb.like(root.get("missingAddress").get("city"), parentFindChild.getMissingAddress().getCity()));
                            }
                        }
                    }
                    Predicate[] predicate = new Predicate[predicatesList.size()];
                    query.where(predicatesList.toArray(predicate));
                    return query.getRestriction();
                }
            };
            Page<ParentFindChild> p = parentFindChildRepository.findAll(specification, page);
            return ResultUtils.send(MessageXin.SUCCESS, p);
        } else {
            return ResultUtils.send(MessageXin.SUCCESS, parentFindChildRepository.findAll(page));
        }
    }

    /**
     * 找到所有匹配结果
     *
     * @param faceToken
     * @return
     */
    public ArrayList<Object> getMatchResults(String faceToken) {
        // 从childrenFaceSet中获取人脸检索结果的face_tokens
        ArrayList<String> faceTokens = faceService.getfaceTokens(faceToken, outerId);
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

}
