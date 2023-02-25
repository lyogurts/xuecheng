package com.xuecheng.content.service;


import com.xuecheng.content.model.dto.CourseCategoryTreeDto;

import java.util.List;

public interface CourseCategoryService {
    /**
     *
     * @param id 根节点ID
     * @return 根节点下面的所有子结点
     */
    List<CourseCategoryTreeDto> queryTreeNodes(String id);
}
