package com.snail.child.repository;

import com.snail.child.model.SuspectedMissingChild;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * User: ZhangXinrui
 * Date: 2019/6/29
 * Description: No Description
 */
public interface SuspectedMissingChildRepository extends JpaRepository<SuspectedMissingChild, String>, JpaSpecificationExecutor<SuspectedMissingChild> {
    SuspectedMissingChild findSuspectedMissingChildById(Integer id);

    SuspectedMissingChild findSuspectedMissingChildByFaceToken(String faceToken);
}
