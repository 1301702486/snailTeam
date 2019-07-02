package com.snail.child.service.releaseInfo;

import com.snail.child.enm.MessageXin;
import com.snail.child.model.Address;
import com.snail.child.model.Result;
import com.snail.child.model.SuspectedMissingChild;
import com.snail.child.model.User;
import com.snail.child.repository.SuspectedMissingChildRepository;
import com.snail.child.repository.UserRepository;
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
import java.io.IOException;
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


    /**
     * 添加疑似流浪儿童信息
     *
     * @param suspectedMissingChild
     * @param emailAddr
     * @param file
     * @return
     */

    @Transactional
    public Result addSuspectedMissingChild(SuspectedMissingChild suspectedMissingChild, String emailAddr, MultipartFile file) {
        User user = userRepository.findUserByEmailAddr(emailAddr);
        byte[] photo = new byte[0];
        try {
            photo = file.getBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }
        suspectedMissingChild.setPhoto(photo);
        user.addSuspectedMissingChild(suspectedMissingChild);
        userRepository.save(user);
        return ResultUtils.send(MessageXin.SUCCESS, suspectedMissingChild);
    }

    /**
     * 删除疑似流浪儿童信息
     *
     * @param id
     * @return
     */
    @Transactional
    public Result deleteSuspectedMissingParent(Integer id,String emailAddr) {
        SuspectedMissingChild suspectedMissingChild = suspectedMissingChildRepository.findSuspectedMissingChildById(id);
        if (suspectedMissingChild != null) {
            User user=userRepository.findUserByEmailAddr(emailAddr);
           // User user = suspectedMissingChild.getUser();
            user.getSuspectedMissingChildren().remove(suspectedMissingChild);
            suspectedMissingChildRepository.delete(suspectedMissingChild);
            return ResultUtils.send(MessageXin.SUCCESS, userRepository.save(user));
        } else {
            return ResultUtils.send(MessageXin.SUSPECTED_NOT_EXIST);
        }
    }

    /**
     * 查询疑似流浪儿童信息
     * @param suspectedMissingChild
     * @param pageable
     * @return
     */
    public Result selectSuspectedMissingChild(SuspectedMissingChild suspectedMissingChild, Pageable pageable){
        if(suspectedMissingChild!=null) {
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
                                }
                            }
                        }
                    }
                    Predicate[] predicate = new Predicate[predicateList.size()];
                    criteriaQuery.where(predicateList.toArray(predicate));
                    return criteriaQuery.getRestriction();
                }
            };
            Page<SuspectedMissingChild> page = suspectedMissingChildRepository.findAll(specification, pageable);
            return ResultUtils.send(MessageXin.SUCCESS, page);
        }else{
            return ResultUtils.send(MessageXin.SUCCESS, suspectedMissingChildRepository.findAll());
        }
    }
}
