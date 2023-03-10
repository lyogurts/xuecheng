package com.xuecheng.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xuecheng.base.exception.CommonError;
import com.xuecheng.base.exception.XueChengPlusException;
import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.content.mapper.CourseBaseMapper;
import com.xuecheng.content.mapper.CourseCategoryMapper;
import com.xuecheng.content.mapper.CourseMarketMapper;
import com.xuecheng.content.model.dto.AddCourseDto;
import com.xuecheng.content.model.dto.CourseBaseInfoDto;
import com.xuecheng.content.model.dto.EditCourseDto;
import com.xuecheng.content.model.dto.QueryCourseParamsDto;
import com.xuecheng.content.model.po.CourseBase;
import com.xuecheng.content.model.po.CourseCategory;
import com.xuecheng.content.model.po.CourseMarket;
import com.xuecheng.content.service.CourseBaseInfoService;
import com.xuecheng.content.service.CourseCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Mr.M
 * @version 1.0
 * @description TODO
 * @date 2022/10/8 9:46
 */
@Slf4j
@Service
public class CourseBaseInfoServiceImpl implements CourseBaseInfoService {

    @Autowired
    CourseBaseMapper courseBaseMapper;

    @Autowired
    CourseMarketMapper courseMarketMapper;

    @Autowired
    CourseCategoryMapper courseCategoryMapper;
    @Autowired
    CourseMarketServiceImpl courseMarketService;

    @Override
    public PageResult<CourseBase> queryCourseBaseList(PageParams params, QueryCourseParamsDto queryCourseParamsDto) {

        LambdaQueryWrapper<CourseBase> queryWrapper = new LambdaQueryWrapper<>();

        //拼接查询条件
        //根据课程名称模糊查询  name like '%名称%'
        queryWrapper.like(StringUtils.isNotEmpty(queryCourseParamsDto.getCourseName()),CourseBase::getName,queryCourseParamsDto.getCourseName());

        //根据课程审核状态
        queryWrapper.eq(StringUtils.isNotEmpty(queryCourseParamsDto.getAuditStatus()),CourseBase::getAuditStatus,queryCourseParamsDto.getAuditStatus());

        //根据课程发布状态
        queryWrapper.eq(StringUtils.isNotEmpty(queryCourseParamsDto.getPublishStatus()),CourseBase::getAuditStatus,queryCourseParamsDto.getPublishStatus());


        //分页参数
        Page<CourseBase> page = new Page<>(params.getPageNo(), params.getPageSize());


        //分页查询E page 分页参数, @Param("ew") Wrapper<T> queryWrapper 查询条件
        Page<CourseBase> pageResult = courseBaseMapper.selectPage(page, queryWrapper);

        //数据
        List<CourseBase> items = pageResult.getRecords();
        //总记录数
        long total = pageResult.getTotal();


        //准备返回数据 List<T> items, long counts, long page, long pageSize
        PageResult<CourseBase> courseBasePageResult = new PageResult<>(items, total, params.getPageNo(), params.getPageSize());

        return courseBasePageResult;
    }

    @Override
    public CourseBaseInfoDto createCourseBase(Long companyId, AddCourseDto addCourseDto) {

        //参数合法性校验
        if (StringUtils.isBlank(addCourseDto.getName())) {
//    这里是自定义异常
            //            throw new RuntimeException("课程名称为空");
//            这里是用规范的CommonError异常
//      这个跟下面的一样，我更喜欢这个。  throw new XueChengPlusException(CommonError.REQUEST_NULL.getErrMessage());
        XueChengPlusException.cast(CommonError.REQUEST_NULL.getErrMessage());
        }

        if (StringUtils.isBlank(addCourseDto.getMt())) {
            throw new RuntimeException("课程分类为空");
        }

        if (StringUtils.isBlank(addCourseDto.getSt())) {
            throw new RuntimeException("课程分类为空");
        }

        if (StringUtils.isBlank(addCourseDto.getGrade())) {
            throw new RuntimeException("课程等级为空");
        }

        if (StringUtils.isBlank(addCourseDto.getTeachmode())) {
            throw new RuntimeException("教育模式为空");
        }

        if (StringUtils.isBlank(addCourseDto.getUsers())) {
            throw new RuntimeException("适应人群为空");
        }

        if (StringUtils.isBlank(addCourseDto.getCharge())) {
            throw new RuntimeException("收费规则为空");
        }
//对数据进行封装，调用mapper进行数据持久化
        CourseBase courseBase = new CourseBase();
        CourseMarket courseMarket = new CourseMarket();
//        将addCourseDto中和courseBase属性名一样的属性值拷贝到courseBase
        BeanUtils.copyProperties(addCourseDto,courseBase);
//        将addCourseDto中和courseMarket属性名一样的属性值拷贝到courseMarket
        BeanUtils.copyProperties(addCourseDto,courseMarket);
//        设置机构id,(参数传来的)
        courseBase.setCompanyId(companyId);
//          设置创建时间
        courseBase.setCreateDate(LocalDateTime.now());
        //设置审核状态
        courseBase.setAuditStatus("202002");
        //设置发布状态
        courseBase.setStatus("203001");


//        课程基本表插入一条记录
        int insert = courseBaseMapper.insert(courseBase);
        //id自增，然后拿到课程id
        Long id = courseBase.getId();
//        向课程营销表插入一条数据
        int insert1 = courseMarketMapper.insert(courseMarket);
        //课程营销表和课程表一个id,别问，问我也不知道
        courseMarket.setId(id);
        //收费规则
        String charge = addCourseDto.getCharge();
        //收费课程必须写价格且价格大于0
        if(charge.equals("201001")){
            Float price = addCourseDto.getPrice();
            if(price == null || price.floatValue()<=0){
                throw new RuntimeException("课程设置了收费价格不能为空且必须大于0");
            }
        }
        //插入成功判断
        if (insert<=0||insert1<=0){
            throw new RuntimeException("新增课程基本信息失败");
        }

//        组装返回的结果
//        调用下面的方法，根据id查出来两个库，然后组合。方法返回的对象就是需要的组合结果
        CourseBaseInfoDto courseBaseInfoDto = getCourseBaseInfo(id);
        return courseBaseInfoDto;
    }





//根据课程id查询课程基本信息，包括基本信息和营销信息
public CourseBaseInfoDto getCourseBaseInfo(long courseId){

    CourseBase courseBase = courseBaseMapper.selectById(courseId);
    CourseMarket courseMarket = courseMarketMapper.selectById(courseId);

    if(courseBase == null){
        return null;
    }
    CourseBaseInfoDto courseBaseInfoDto = new CourseBaseInfoDto();
    BeanUtils.copyProperties(courseBase,courseBaseInfoDto);
    if(courseMarket != null){
        BeanUtils.copyProperties(courseMarket,courseBaseInfoDto);
    }

    //查询分类名称
    CourseCategory courseCategoryBySt = courseCategoryMapper.selectById(courseBase.getSt());
    courseBaseInfoDto.setStName(courseCategoryBySt.getName());
    CourseCategory courseCategoryByMt = courseCategoryMapper.selectById(courseBase.getMt());
    courseBaseInfoDto.setMtName(courseCategoryByMt.getName());

    return courseBaseInfoDto;

}

    @Override
    public CourseBaseInfoDto updateCourseBase(Long companyId, EditCourseDto dto) {
        //校验

        // 对课程id的校验
        Long id = dto.getId();
        CourseBase courseBase = courseBaseMapper.selectById(id);
        if (courseBase==null)throw new XueChengPlusException("课程不存在");
        //校验本机构只能修改本机构的课程
        if (!courseBase.getCompanyId().equals(companyId))throw new XueChengPlusException("校验本机构只能修改本机构的课程");

        //封装数据EditCourseDto(它比AddCourseDto多了一个id，继承关系) =封装课程基本信息CourseBase+封装课程营销信息CourseMarket
       //copy属性,封装基本信息courseBase
        BeanUtils.copyProperties(dto,courseBase);
       //  修改修改时间
        courseBase.setChangeDate(LocalDateTime.now());
        //更新数据库
        int i = courseBaseMapper.updateById(courseBase);
        //封装课程营销信息，courseMarket
        CourseMarket courseMarket = new CourseMarket();
        BeanUtils.copyProperties(dto,courseMarket);

        //这里算是把前台的数据传递给后台， 你想一下，这里是不是还有一次关于课程收费价格的校验呢
        //收费课程必须写价格且价格大于0
        String charge = courseMarket.getCharge();
        if(charge.equals("201001")){
            Float price = courseMarket.getPrice();
            if(price == null || price.floatValue()<=0){
                throw new RuntimeException("课程设置了收费价格不能为空且必须大于0");
            }
        }
        //更新数据库
        boolean b = courseMarketService.saveOrUpdate(courseMarket);

        //请求数据库,id是前面获取到的id,相当于更新两个数据库， 调用下面这个方法一次查询两个数据库
        //这个方法在impl写着呢。
        CourseBaseInfoDto courseBaseInfo = getCourseBaseInfo(id);
        return courseBaseInfo;
    }
}