package com.xuecheng.content.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.content.mapper.CourseBaseMapper;
import com.xuecheng.content.model.dto.AddCourseDto;
import com.xuecheng.content.model.dto.CourseBaseInfoDto;
import com.xuecheng.content.model.dto.EditCourseDto;
import com.xuecheng.content.model.dto.QueryCourseParamsDto;
import com.xuecheng.content.model.po.CourseBase;

/**
 * @description 课程基本信息管理业务接口
 * @author Mr.M
 * @date 2022/9/6 21:42
 * @version 1.0
 */
public interface CourseBaseInfoService  {

    /**
     * @description 课程查询接口
     * @param pageParams 分页参数
     * @param queryCourseParamsDto 条件条件
     * @return com.xuecheng.base.model.PageResult<com.xuecheng.content.model.po.CourseBase>
     * @author Mr.M
     * @date 2022/9/6 21:44
     */
    PageResult<CourseBase> queryCourseBaseList(PageParams pageParams, QueryCourseParamsDto queryCourseParamsDto);

    /**
     *
     * @param companyId 培训机构id
     * @param addCourseDto  新增课程信息
     * @return 课程信息包括基本信息，营销信息
     */
    CourseBaseInfoDto createCourseBase(Long companyId, AddCourseDto addCourseDto);
//根据id查询，CourseBaseInfoDto=CourseBase+CourseMarket
    CourseBaseInfoDto getCourseBaseInfo(long courseId);

//    修改
    public CourseBaseInfoDto updateCourseBase(Long companyId, EditCourseDto dto);
}