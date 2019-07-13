package com.snail.child.repository;


import com.snail.child.model.ChildFindParent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * User: ZhangXinrui
 * Date: 2019/6/28
 * Description: No Description
 */
public interface ChildFindParentRepository extends JpaRepository<ChildFindParent, Integer>, JpaSpecificationExecutor<ChildFindParent> {

    ChildFindParent findChildFindParentById(Integer Id);

    ChildFindParent findChildFindParentByFaceToken(String faceToken);
}
