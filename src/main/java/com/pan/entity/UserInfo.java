package com.pan.entity;

import java.util.Date;

public class UserInfo {
    private String address;
    private float salary;
    private String name;
    private int age;
    private String remark;
    private String birthDate;
    private Date createTime;


    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setSalary(float salary) {
        this.salary = salary;
    }

    public float getSalary() {
        return salary;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getAge() {
        return age;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRemark() {
        return remark;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "address='" + address + '\'' +
                ", salary=" + salary +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", remark='" + remark + '\'' +
                ", birthDate='" + birthDate + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}
