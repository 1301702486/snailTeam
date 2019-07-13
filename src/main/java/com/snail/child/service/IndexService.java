package com.snail.child.service;

/**
 * User: ZhangXinrui
 * Date: 2019/7/6
 * Description: No Description
 */


import com.snail.child.enm.MessageXin;
import com.snail.child.model.*;
import com.snail.child.repository.ChildFindParentRepository;
import com.snail.child.repository.ParentFindChildRepository;
import com.snail.child.repository.SuspectedMissingChildRepository;
import com.snail.child.repository.UserRepository;
import com.snail.child.utils.EmailUtils;
import com.snail.child.utils.ResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class IndexService {

    @Autowired
    ParentFindChildRepository parentFindChildRepository;
    @Autowired
    ChildFindParentRepository childFindParentRepository;
    @Autowired
    SuspectedMissingChildRepository suspectedMissingChildRepository;
    @Autowired
    UserRepository userRepository;

    /**
     * 查询最新走失信息
     *
     * @param province 走失省份
     * @param pageNum  页码
     * @param size     每页显示数量
     * @return 成功: code=0, data=[总页数, 每页内容]
     */
    public Result selectLatestChild(String province, int pageNum, int size) {
        Specification<ParentFindChild> specification = new Specification<ParentFindChild>() {
            @Override
            public Predicate toPredicate(Root<ParentFindChild> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicatesList = new ArrayList<>();
                if (!StringUtils.isEmpty(province)) {
                    Join<ParentFindChild, Address> join = root.join("missingAddress", JoinType.LEFT);
                    predicatesList.add(cb.like(root.get("missingAddress").get("province"), province));
                }
                Predicate[] predicate = new Predicate[predicatesList.size()];
                query.where(predicatesList.toArray(predicate));
                return query.getRestriction();
            }
        };
        Sort sort = new Sort(Sort.Direction.DESC, "missingDate");
        Pageable pageable = PageRequest.of(pageNum - 1, size, sort);
        Page<ParentFindChild> page = parentFindChildRepository.findAll(specification, pageable);
        Integer totalPage = parentFindChildRepository.findAll(specification).size() / pageable.getPageSize();
        return ResultUtils.send(MessageXin.SUCCESS, totalPage, page);

    }

    /**
     * 联系发布人
     *
     * @param emailAddr  用户的联系邮箱
     * @param id         发布id
     * @param name       用户名称
     * @param contentStr 邮件内容
     * @param title      主题
     * @return 成功 code=0,失败:code=408, 提示发布用户不存在
     * @author 郭瑞景
     */
    public Result contactUser(String emailAddr, Integer id, String name, String contentStr, String title) {
        User user;
        ParentFindChild parentFindChild = parentFindChildRepository.findParentFindChildById(id);
        ChildFindParent childFindParent = childFindParentRepository.findChildFindParentById(id);
        SuspectedMissingChild suspectedMissingChild = suspectedMissingChildRepository.findSuspectedMissingChildById(id);
        if (parentFindChild != null) {
            user = userRepository.findUserByParentFindChild(parentFindChild);
        } else {
            if (childFindParent != null) {
                user = userRepository.findUserByChildFindParent(childFindParent);
            } else {
                user = userRepository.findUserBySuspectedMissingChildren(suspectedMissingChild);
            }
        }
        if (user == null) {
            return ResultUtils.send(MessageXin.RELEASEUSER_NOT_EXIST);
        }
        String to = user.getEmailAddr();
        EmailUtils emailUtil = new EmailUtils();
        String content = "来自：" + name + "<br>" + contentStr + "<br>" + "联系邮箱：" + emailAddr;
        emailUtil.sendMessage(to, "【联系】" + title, content);
        return ResultUtils.send(MessageXin.SUCCESS);
    }

}