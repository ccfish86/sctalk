/*
 * Copyright © 2013-2016 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.service.jpa.projection.test;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.blt.talk.service.jpa.entity.IMGroup;
import com.blt.talk.service.jpa.repository.IMGroupRepository;

/**
 * 
 * @author 袁贵
 * @version 1.0
 * @since  1.0
 */
@SpringBootTest
public class GroupServiceTest {

//    @BeforeClass
//    public static void b() {
//        SpringApplication.run(TalkDbProxyApplication.class, "");
//    }
//    
    @Autowired
    private IMGroupRepository groupRepository;
    
    @Test
    public void testSelect() {

        try {
            List<IMGroup> groups = groupRepository.findByUserId(11);
//            SearchCriteria<IMGroup> groupCriteria = new SearchCriteria<IMGroup>();
//            groupCriteria.add(JpaRestrictions.eq("groupMemberList.userId", 10, false));
//            List<IMGroup> groups = groupRepository.findAll(groupCriteria, new Sort(Sort.Direction.DESC, "updated"));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
