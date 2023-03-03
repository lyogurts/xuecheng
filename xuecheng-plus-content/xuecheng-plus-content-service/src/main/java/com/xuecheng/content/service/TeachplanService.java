package com.xuecheng.content.service;

import com.xuecheng.content.model.dto.SaveTeachplanDto;
import com.xuecheng.content.model.dto.TeachplanDto;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface TeachplanService {
    public List<TeachplanDto> findTeachplayTree(Long courseId);
    public void saveTeachplan( SaveTeachplanDto teachplanDto);

    }
