package com.project.schoolmanagment.service.helper;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.awt.print.Pageable;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class PageableHelper {
    public org.springframework.data.domain.Pageable getPageableWithProperties(int size, int page, String type, String sort){
        org.springframework.data.domain.Pageable pageable =  PageRequest.of(page,size, Sort.by(sort).ascending());
        if (Objects.equals(type, "desc")){
            pageable =  PageRequest.of(page,size, Sort.by(sort).descending());
        }
        return pageable;
    }
}
