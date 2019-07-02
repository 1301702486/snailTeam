package com.snail.child.service.releaseInfo;


import com.snail.child.enm.MessageXin;
import com.snail.child.model.Address;
import com.snail.child.model.ParentFindChild;
import com.snail.child.model.Result;
import com.snail.child.model.User;
import com.snail.child.repository.ParentFindChildRepository;
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
        if (user.getParentFindChild() == null) {
            byte[] photo = new byte[0];
            try {
                photo = file.getBytes();
            } catch (IOException e) {
                e.printStackTrace();
            }
            parentFindChild.setPhoto(photo);
            user.setParentFindChild(parentFindChild);
            parentFindChildRepository.save(parentFindChild);
            userRepository.save(user);
            return ResultUtils.send(MessageXin.SUCCESS, parentFindChildRepository.save(parentFindChild));
        } else {
            return ResultUtils.send(MessageXin.PARENTFINDCHILD_HAS_EXIST);
        }
    }

    /**
     * 根据id删除发布信息
     *
     * @param id
     * @return
     */
    @Transactional
    public Result deleteParentFindChild(Integer id) {
        ParentFindChild parentFindChild = parentFindChildRepository.findParentFindChildById(id);
        if (parentFindChild != null) {
            User user = userRepository.findUserByParentFindChild(parentFindChild);
            user.setParentFindChild(null);
            parentFindChildRepository.delete(parentFindChild);
            parentFindChildRepository.findAll();
            return ResultUtils.send(MessageXin.SUCCESS, userRepository.save(user));
        } else {
            return ResultUtils.send(MessageXin.PARENTFINDCHILD_NOT_EXIST);
        }
    }

    /**
     * 更新家长找孩子的发布信息
     *
     * @param parentFindChild
     * @return
     */
    public Result updateParentFindChild(ParentFindChild parentFindChild) {
        return ResultUtils.send(MessageXin.SUCCESS, parentFindChildRepository.save(parentFindChild));
    }

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


//
//    @Override
//    public Page<Student> search(final Student student, PageInfo page) {
//        return studentRepository.findAll(new Specification<Student>() {
//            @Override
//            public Predicate toPredicate(Root<Student> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
//
//                Predicate stuNameLike = null;
//                if(null != student && !StringUtils.isEmpty(student.getName())) {
//                    stuNameLike = cb.like(root.<String> get("name"), "%" + student.getName() + "%");
//                }
//
//                Predicate clazzNameLike = null;
//                if(null != student && null != student.getClazz() && !StringUtils.isEmpty(student.getClazz().getName())) {
//                    clazzNameLike = cb.like(root.<String> get("clazz").<String> get("name"), "%" + student.getClazz().getName() + "%");
//                }
//
//                if(null != stuNameLike) query.where(stuNameLike);
//                if(null != clazzNameLike) query.where(clazzNameLike);
//                return null;
//            }
//        }, new PageRequest(page.getPage() - 1, page.getLimit(), new Sort(Direction.DESC, page.getSortName())));
//    }


}
