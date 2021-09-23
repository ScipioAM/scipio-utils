package com.test;

import com.github.ScipioAM.scipio_utils_doc.excel.annotations.ExcelMapping;

/**
 * @date 2021/9/18
 */
public class TestBean {

    @ExcelMapping(cellIndex = 0)
    private Integer id;
    @ExcelMapping(cellIndex = 1)
    private String name;
    @ExcelMapping(cellIndex = 2)
    private String descCn;

    public TestBean() {}

    public TestBean(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public TestBean(Integer id, String name, String descCn) {
        this.id = id;
        this.name = name;
        this.descCn = descCn;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescCn() {
        return descCn;
    }

    public void setDescCn(String descCn) {
        this.descCn = descCn;
    }

    @Override
    public String toString() {
        return "TestBean{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", descCn='" + descCn + '\'' +
                '}';
    }
}
