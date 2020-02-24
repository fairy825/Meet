package com.meet.pojo;

import com.meet.service.CategoryService;

import javax.persistence.*;

public class Category {
    @Id
    private Integer id;

    private String name;
    private String type;

    public String getTypeDesc() {
        String desc = "";
        switch(type){
            case CategoryService.literature:
                desc="文学";
                break;
            case CategoryService.popular:
                desc="流行";
                break;
            case CategoryService.business:
                desc="经管";
                break;
            case CategoryService.culture:
                desc="文化";
                break;
            case CategoryService.life:
                desc="生活";
                break;
            case CategoryService.science:
                desc="科技";
                break;
            default:
                desc="未知";
        }
        return desc;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }
}