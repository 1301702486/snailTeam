package com.snail.child.repository;


import com.snail.child.model.ParentFindChild;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * User: ZhangXinrui
 * Date: 2019/6/27
 * Description: No Description
 */
public interface ParentFindChildRepository extends JpaRepository<ParentFindChild, Integer>, JpaSpecificationExecutor<ParentFindChild> {
    ParentFindChild findParentFindChildById(Integer id);

}
