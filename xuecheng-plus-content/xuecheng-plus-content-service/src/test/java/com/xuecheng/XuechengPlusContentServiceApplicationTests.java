package com.xuecheng;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xuecheng.base.model.PageParams;
import com.xuecheng.content.mapper.CourseBaseMapper;
import com.xuecheng.content.model.dto.QueryCourseParamsDto;
import com.xuecheng.content.model.po.CourseBase;
import com.xuecheng.content.service.CourseBaseInfoService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class XuechengPlusContentServiceApplicationTests {

    private CourseBaseMapper courseBaseMapper;
    @Autowired

    private CourseBaseInfoService courseBaseInfoService;


    @Autowired
    public XuechengPlusContentServiceApplicationTests(CourseBaseMapper courseBaseMapper) {
        this.courseBaseMapper = courseBaseMapper;
    }

    /**
     * 测试mapper
     */
    @Test
    void contextLoads() {
        CourseBase courseBase = courseBaseMapper.selectById(22);
        Assertions.assertNull(courseBase);
    }

    /**
     * 测试service
     */
    @Test
    void testCourseBaseInfoService(){
        PageParams pageParams = new PageParams();
        courseBaseInfoService.queryCourseBaseList(pageParams,new QueryCourseParamsDto());
        System.out.println(courseBaseInfoService);
    }

}
