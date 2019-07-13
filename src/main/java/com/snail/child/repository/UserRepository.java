package com.snail.child.repository;

import com.snail.child.model.ChildFindParent;
import com.snail.child.model.ParentFindChild;
import com.snail.child.model.SuspectedMissingChild;
import com.snail.child.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

/**
 * Author: 郭瑞景
 * Date: 2019/6/26
 * Description: No description
 */

public interface UserRepository extends JpaRepository<User, String> {
    User findUserByEmailAddr(String emailAddr);

    User findUserByParentFindChild(ParentFindChild parentFindChild);

    User findUserByChildFindParent(ChildFindParent childFindParent);

    User findUserBySuspectedMissingChildren(SuspectedMissingChild suspectedMissingChild);

}
